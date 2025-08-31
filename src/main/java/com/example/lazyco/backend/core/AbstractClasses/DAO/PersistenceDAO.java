package com.example.lazyco.backend.core.AbstractClasses.DAO;

import com.example.lazyco.backend.core.AbstractClasses.Entity.AbstractModel;
import org.hibernate.Session;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Transactional(propagation = Propagation.NESTED)
public class PersistenceDAO<E extends AbstractModel> implements IPersistenceDAO<E> {

  public E save(E entity) {
    try {
      getCurrentSession().persist(entity);
      flush();
      return entity;
    } catch (Exception e) {
      getCurrentSession().clear();
      throw e;
    }
  }

  public E update(E entity) {
    try {
      getCurrentSession().merge(entity);
      flush();
      return entity;
    } catch (Exception e) {
      getCurrentSession().clear();
      throw e;
    }
  }

  public E delete(E entity) {
    try {
      getCurrentSession().remove(entity);
      flush();
      return entity;
    } catch (Exception e) {
      getCurrentSession().clear();
      throw e;
    }
  }

  public Session getCurrentSession() {
    return TransactionalWrapper.getCurrentSession();
  }

  public void flush() {
    TransactionalWrapper.flush();
  }

  public E findById(Class<E> clazz, Long id) {
    return getCurrentSession().find(clazz, id);
  }
}