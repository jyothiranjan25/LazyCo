package com.example.lazyco.entities.Document;

import com.example.lazyco.core.AbstractClasses.Service.CommonAbstractService;
import com.example.lazyco.core.Exceptions.ApplicationException;
import org.springframework.stereotype.Service;

@Service
public class DocumentService extends CommonAbstractService<DocumentDTO, Document> {
  protected DocumentService(DocumentMapper documentMapper) {
    super(documentMapper);
  }

  @Override
  protected void validateBeforeCreate(DocumentDTO requestDTO) {
    if (requestDTO.getDocumentType() == null) {
      throw new ApplicationException(DocumentMessage.DOCUMENT_TYPE_REQUIRED);
    }

    if (requestDTO.getCode() == null) {
      throw new ApplicationException(DocumentMessage.DOCUMENT_NAME_REQUIRED);
    }

    validateUniqueCode(requestDTO, DocumentMessage.DUPLICATE_DOCUMENT_NAME);
  }

  @Override
  protected void validateBeforeUpdate(DocumentDTO requestDTO) {
    if (requestDTO.getCode() != null) {
      validateUniqueCode(requestDTO, DocumentMessage.DUPLICATE_DOCUMENT_NAME);
    }
  }

  @Override
  protected void makeUpdates(DocumentDTO source, Document target) {
    if (source.getDocumentType() != null) {
      target.setDocumentType(source.getDocumentType());
    }
  }
}
