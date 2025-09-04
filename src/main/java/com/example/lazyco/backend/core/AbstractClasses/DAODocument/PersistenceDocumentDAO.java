package com.example.lazyco.backend.core.AbstractClasses.DAODocument;

import com.example.lazyco.backend.core.AbstractClasses.EntityDocument.AbstractDocumentModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PersistenceDocumentDAO<E extends AbstractDocumentModel>
    implements IPersistenceDocumentDAO<E> {

    @Autowired
    private MongoTemplate mongoTemplate;

  public E save(E entity) {
      return mongoTemplate.insert(entity);
  }

  @Override
  public E update(E entity) {
    return mongoTemplate.save(entity);
  }

  @Override
  public E delete(E entity) {
    mongoTemplate.remove(entity);
    return entity;
  }

  @Override
  public E findById(Class<E> clazz, String id) {
    return mongoTemplate.findById(id, clazz);
  }
}