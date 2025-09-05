package com.example.lazyco.backend.core.AbstractDocClasses.DAO;

import com.example.lazyco.backend.core.AbstractDocClasses.Entity.AbstractModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PersistenceDocDAO<E extends AbstractModel> implements IPersistenceDocDAO<E> {

  @Autowired private MongoTemplate mongoTemplate;

  @Override
  public E save(E entity) {
    return mongoTemplate.save(entity);
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
