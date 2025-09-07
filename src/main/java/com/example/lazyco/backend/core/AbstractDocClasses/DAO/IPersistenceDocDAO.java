package com.example.lazyco.backend.core.AbstractDocClasses.DAO;

import com.example.lazyco.backend.core.AbstractDocClasses.Entity.AbstractModel;

public interface IPersistenceDocDAO<E extends AbstractModel> {

  E save(E entity);

  E update(E entity);

  E delete(E entity);

  E findById(Class<E> clazz, String id);
}
