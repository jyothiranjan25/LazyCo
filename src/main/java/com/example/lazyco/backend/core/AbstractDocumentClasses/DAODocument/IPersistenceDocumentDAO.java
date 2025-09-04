package com.example.lazyco.backend.core.AbstractDocumentClasses.DAODocument;

import com.example.lazyco.backend.core.AbstractDocumentClasses.EntityDocument.AbstractDocumentModel;

public interface IPersistenceDocumentDAO<E extends AbstractDocumentModel> {

  E save(E entity);

  E update(E entity);

  E delete(E entity);

  E findById(Class<E> clazz, String id);
}
