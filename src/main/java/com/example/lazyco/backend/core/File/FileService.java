package com.example.lazyco.backend.core.File;

import static org.apache.pdfbox.io.IOUtils.createTempFileOnlyStreamCache;

import com.example.lazyco.backend.core.Logger.ApplicationLogger;
import com.example.lazyco.backend.core.Utils.CommonConstrains;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.io.IOUtils;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FileService {

  // Helper method to create a temporary file with specified prefix and type.
  private static File createTempFile(String prefix, FileTypeEnum type) throws IOException {
    return File.createTempFile(prefix, type.getExtension(), new File(CommonConstrains.TEMP_DIR));
  }

  /**
   * Zips multiple files into a single zip file.
   *
   * @param files List of FileDTO objects representing the files to be zipped.
   * @param fileName Desired name for the output zip file (without extension). If null or empty, a
   *     default name will be used.
   * @return A FileDTO object representing the created zip file.
   * @throws IOException If an I/O error occurs during zipping.
   */
  public static FileDTO zipFiles(List<FileDTO> files, String fileName) throws IOException {
    String tempFilePrefix = fileName != null && !fileName.isEmpty() ? fileName : "files_";
    File outputFilePath = File.createTempFile(tempFilePrefix, FileTypeEnum.ZIP.getExtension());
    try (ZipArchiveOutputStream zipOut = new ZipArchiveOutputStream(outputFilePath)) {
      for (FileDTO file : files) {
        ZipArchiveEntry entry = new ZipArchiveEntry(file.getName());
        zipOut.putArchiveEntry(entry);
        try (InputStream fis = new FileInputStream(file.getFile())) {
          IOUtils.copy(fis, zipOut);
        }
        zipOut.closeArchiveEntry();
      }
      zipOut.finish();
    }
    return new FileDTO(outputFilePath);
  }

  /**
   * Unzips a zip file into its constituent files.
   *
   * @param zipFile A FileDTO object representing the zip file to be unzipped.
   * @return A list of FileDTO objects representing the extracted files.
   * @throws IOException If an I/O error occurs during unzipping.
   */
  public static List<FileDTO> unzipFile(FileDTO zipFile) throws IOException {
    List<FileDTO> extractedFiles = new ArrayList<>();

    try (ZipFile zip =
        ZipFile.builder()
            .setPath(zipFile.getFile().toPath())
            .setCharset(StandardCharsets.UTF_8)
            .get()) {
      Enumeration<ZipArchiveEntry> entries = zip.getEntries();
      while (entries.hasMoreElements()) {
        ZipArchiveEntry entry = entries.nextElement();
        if (entry.isDirectory()) continue;

        File outFile = File.createTempFile("unzipped_", "_" + entry.getName());
        try (InputStream is = zip.getInputStream(entry);
            OutputStream os = new FileOutputStream(outFile)) {
          IOUtils.copy(is, os);
        }
        extractedFiles.add(new FileDTO(outFile));
      }
    }
    return extractedFiles;
  }

  /**
   * Merges multiple PDF files into a single PDF file.
   *
   * @param pdfFiles List of FileDTO objects representing the PDF files to be merged.
   * @param fileName Desired name for the output PDF file (without extension). If null or empty, a
   *     default name will be used.
   * @return A FileDTO object representing the merged PDF file.
   * @throws IOException If an I/O error occurs during merging.
   */
  public static FileDTO mergePdfs(List<FileDTO> pdfFiles, String fileName) throws IOException {
    PDFMergerUtility merger = new PDFMergerUtility();
    String tempFilePrefix = fileName != null && !fileName.isEmpty() ? fileName : "files_";
    File outputFilePath = File.createTempFile(tempFilePrefix, FileTypeEnum.PDF.getExtension());
    for (FileDTO pdf : pdfFiles) {
      if (!isValidPdf(pdf.getFile())) {
        ApplicationLogger.warn("⚠️ Skipping non-PDF file: " + pdf.getName());
        continue;
      }
      merger.addSource(pdf.getFile());
    }

    merger.setDestinationFileName(outputFilePath.getAbsolutePath());
    merger.mergeDocuments(createTempFileOnlyStreamCache());
    return new FileDTO(outputFilePath);
  }

  // Helper method to validate if a file is a valid PDF.
  private static boolean isValidPdf(File file) {
    try (PDDocument doc = Loader.loadPDF(file)) {
      return true;
    } catch (IOException e) {
      return false;
    }
  }

  /**
   * Splits a PDF file into individual pages.
   *
   * @param pdfFile A FileDTO object representing the PDF file to be split.
   * @return A list of FileDTO objects representing the individual pages.
   * @throws IOException If an I/O error occurs during splitting.
   */
  public static List<FileDTO> splitPdf(FileDTO pdfFile) throws IOException {
    List<FileDTO> pages = new ArrayList<>();
    try (PDDocument doc = Loader.loadPDF(pdfFile.getFile())) {
      Splitter splitter = new Splitter();
      List<PDDocument> splitDocs = splitter.split(doc);
      int i = 1;
      for (PDDocument page : splitDocs) {
        File out = File.createTempFile("page_" + i + "_", FileTypeEnum.PDF.getExtension());
        page.save(out);
        pages.add(new FileDTO(out));
        page.close();
        i++;
      }
    }
    return pages;
  }

  /**
   * Encrypts a PDF file with a password.
   *
   * @param pdfFile A FileDTO object representing the PDF file to be encrypted.
   * @param password The password to encrypt the PDF file.
   * @return A FileDTO object representing the encrypted PDF file.
   * @throws IOException If an I/O error occurs during encryption.
   */
  public static FileDTO encryptPdf(FileDTO pdfFile, String password) throws IOException {
    File outFile = File.createTempFile("encrypted_", FileTypeEnum.PDF.getExtension());

    try (PDDocument doc = Loader.loadPDF(pdfFile.getFile())) {
      AccessPermission ap = new AccessPermission();
      StandardProtectionPolicy spp = new StandardProtectionPolicy(password, password, ap);
      spp.setEncryptionKeyLength(128);
      spp.setPermissions(ap);
      doc.protect(spp);
      doc.save(outFile);
    }
    return new FileDTO(outFile);
  }

  /**
   * Decrypts a password-protected PDF file.
   *
   * @param pdfFile A FileDTO object representing the encrypted PDF file.
   * @param password The password to decrypt the PDF file.
   * @return A FileDTO object representing the decrypted PDF file.
   * @throws IOException If an I/O error occurs during decryption.
   */
  public static FileDTO decryptPdf(FileDTO pdfFile, String password) throws IOException {
    File outFile = File.createTempFile("decrypted_", FileTypeEnum.PDF.getExtension());
    try (PDDocument doc = Loader.loadPDF(pdfFile.getFile(), password)) {
      doc.setAllSecurityToBeRemoved(true);
      doc.save(outFile);
    } catch (InvalidPasswordException e) {
      throw new IOException("Incorrect PDF password", e);
    }
    return new FileDTO(outFile);
  }

  /**
   * Generates a QR code image from the given text.
   *
   * @param text The text to encode in the QR code.
   * @param width The width of the generated QR code image.
   * @param height The height of the generated QR code image.
   * @param fileName Desired name for the output QR code file (without extension). If null or empty,
   *     a default name will be used.
   * @return A FileDTO object representing the generated QR code image.
   * @throws IOException If an I/O error occurs during QR code generation.
   */
  public static FileDTO generateQrCode(String text, int width, int height, String fileName)
      throws IOException {
    try {
      BitMatrix bitMatrix = createQrMatrix(text, width, height);
      File outFile =
          File.createTempFile(
              (fileName != null && !fileName.isEmpty()) ? fileName : "qr_",
              FileTypeEnum.PNG.getExtension());
      Path path = FileSystems.getDefault().getPath(outFile.getAbsolutePath());
      MatrixToImageWriter.writeToPath(bitMatrix, FileTypeEnum.PNG.name(), path);
      return new FileDTO(outFile);
    } catch (WriterException e) {
      throw new IOException("Failed to generate QR code", e);
    }
  }

  /**
   * Generates a QR code image from the given text and returns it as a Base64-encoded string.
   *
   * @param text The text to encode in the QR code.
   * @param width The width of the generated QR code image.
   * @param height The height of the generated QR code image.
   * @param fileName Desired name for the output QR code file (without extension). If null or empty,
   *     a default name will be used.
   * @return A Base64-encoded string representing the generated QR code image.
   * @throws IOException If an I/O error occurs during QR code generation.
   */
  public static String generateQrCodeString(String text, int width, int height, String fileName)
      throws IOException {
    try (ByteArrayOutputStream bas = new ByteArrayOutputStream()) {
      BitMatrix matrix = createQrMatrix(text, width, height);
      MatrixToImageWriter.writeToStream(matrix, FileTypeEnum.PNG.name(), bas);
      return Base64.getEncoder().encodeToString(bas.toByteArray());
    } catch (WriterException e) {
      throw new IOException("Failed to generate QR code", e);
    }
  }

  // Helper method to create a QR code BitMatrix.
  private static BitMatrix createQrMatrix(String text, int width, int height)
      throws WriterException {
    QRCodeWriter writer = new QRCodeWriter();
    Map<EncodeHintType, Object> hints = new HashMap<>();
    hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
    hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
    hints.put(EncodeHintType.MARGIN, 1);
    return writer.encode(text, BarcodeFormat.QR_CODE, width, height, hints);
  }
}
