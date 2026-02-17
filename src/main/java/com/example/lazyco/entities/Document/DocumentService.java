package com.example.lazyco.entities.Document;

import com.example.lazyco.core.AbstractClasses.Service.CommonAbstractService;
import com.example.lazyco.core.Exceptions.ApplicationException;
import com.example.lazyco.core.Utils.CommonUtils;
import org.springframework.stereotype.Service;

@Service
public class DocumentService extends CommonAbstractService<DocumentDTO, Document> {
  protected DocumentService(DocumentMapper documentMapper) {
    super(documentMapper);
  }

  @Override
  protected void validateBeforeCreate(DocumentDTO request) {
    if (request.getDocumentType() == null) {
      throw new ApplicationException(DocumentMessage.DOCUMENT_TYPE_REQUIRED);
    }

    if (request.getName() == null) {
      throw new ApplicationException(DocumentMessage.DOCUMENT_NAME_REQUIRED);
    } else {
      request.setKey(CommonUtils.toSnakeCase(request.getName()));
      validateUniqueCode(request, DocumentMessage.DUPLICATE_DOCUMENT_KEY);
    }
  }

  @Override
  protected void validateBeforeUpdate(DocumentDTO request) {
    if (request.getName() != null) {
      request.setKey(CommonUtils.toSnakeCase(request.getName()));
      validateUniqueCode(request, DocumentMessage.DUPLICATE_DOCUMENT_KEY);
    }
  }

  @Override
  protected void makeUpdates(DocumentDTO source, Document target) {
    if (source.getDocumentType() != null) {
      target.setDocumentType(source.getDocumentType());
    }
  }
}
