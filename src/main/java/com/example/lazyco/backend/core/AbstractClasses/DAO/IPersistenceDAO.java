package com.example.lazyco.backend.core.AbstractClasses.DAO;

import com.example.lazyco.backend.core.AbstractClasses.Entity.AbstractModel;

public interface IPersistenceDAO<E extends AbstractModel> {

  E save(E entity);

  E update(E entity);

  E delete(E entity);

  E findById(Class<E> clazz, Long id);
}
