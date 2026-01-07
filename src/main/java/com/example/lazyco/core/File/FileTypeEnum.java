package com.example.lazyco.core.File;

public enum FileTypeEnum {
  PDF,
  ZIP,
  CSV,
  PNG,
  JPG,
  JPEG,
  HTML,
  JASPER;

  public String getExtension() {
    return switch (this) {
      case PDF -> getExtension(PDF);
      case ZIP -> getExtension(ZIP);
      case CSV -> getExtension(CSV);
      case HTML -> getExtension(HTML);
      case PNG -> getExtension(PNG);
      case JPG -> getExtension(JPG);
      case JPEG -> getExtension(JPEG);
      case JASPER -> getExtension(JASPER);
    };
  }

  public String getContentType() {
    return switch (this) {
      case PDF -> "application/pdf";
      case ZIP -> "application/zip";
      case CSV -> "text/csv";
      case HTML -> "text/html";
      case PNG -> "image/png";
      case JPG, JPEG -> "image/jpeg";
      case JASPER -> "application/jasper";
    };
  }

  public static String getExtension(FileTypeEnum fileTypeEnum) {
    return "." + fileTypeEnum.name().toLowerCase();
  }

  public static boolean isSupported(String extension) {
    for (FileTypeEnum type : FileTypeEnum.values()) {
      if (type.name().equalsIgnoreCase(extension)) {
        return true;
      }
    }
    return false;
  }
}
