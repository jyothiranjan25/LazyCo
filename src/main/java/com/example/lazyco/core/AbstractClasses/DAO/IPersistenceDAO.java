package com.example.lazyco.core.AbstractClasses.DAO;

import com.example.lazyco.core.AbstractClasses.Entity.AbstractModel;

public interface IPersistenceDAO<E extends AbstractModel> {

  E save(E entity);

  E update(E entity);

  E delete(E entity);

  E findById(Class<E> clazz, Long id);

  boolean isNestedTransaction();

  boolean isTransactionActive();

  void flush();
}
