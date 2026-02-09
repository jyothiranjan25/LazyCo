package com.example.lazyco.core.AbstractClasses.DAO;

import com.example.lazyco.core.AbstractClasses.Entity.AbstractModel;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.NoTransactionException;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.transaction.support.DefaultTransactionStatus;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Repository
public class PersistenceDAO<E extends AbstractModel> implements IPersistenceDAO<E> {

  private final SessionFactory sessionFactory;

  public PersistenceDAO(SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
  }

  public E save(E entity) {
    try {
      getCurrentSession().persist(entity);
      getCurrentSession().refresh(entity);
      return entity;
    } catch (Exception e) {
      // CRITICAL FIX: Clear session state after failed operations in nested transactions on flush
      if (isNestedTransaction()) {
        getCurrentSession().clear();
      }
      throw e;
    }
  }

  @Override
  public E saveAndFlush(E entity) {
    try {
      getCurrentSession().persist(entity);
      flush();
      getCurrentSession().refresh(entity);
      return entity;
    } catch (Exception e) {
      // CRITICAL FIX: Clear session state after failed operations in nested transactions on flush
      if (isNestedTransaction()) {
        getCurrentSession().clear();
      }
      throw e;
    }
  }

  public E update(E entity) {
    try {
      E mergedEntity = getCurrentSession().merge(entity);
      getCurrentSession().refresh(mergedEntity);
      return mergedEntity;
    } catch (Exception e) {
      // CRITICAL FIX: Clear session state after failed operations in nested transactions on flush
      if (isNestedTransaction()) {
        getCurrentSession().clear();
      }
      throw e;
    }
  }

  @Override
  public E updateAndFlush(E entity) {
    try {
      E mergedEntity = getCurrentSession().merge(entity);
      flush();
      getCurrentSession().refresh(mergedEntity);
      return mergedEntity;
    } catch (Exception e) {
      // CRITICAL FIX: Clear session state after failed operations in nested transactions on flush
      if (isNestedTransaction()) {
        getCurrentSession().clear();
      }
      throw e;
    }
  }

  public E delete(E entity) {
    try {
      // Ensure entity is managed before removal
      E managedEntity = getCurrentSession().merge(entity);
      getCurrentSession().remove(managedEntity);
      return managedEntity;
    } catch (Exception e) {
      // CRITICAL FIX: Clear session state after failed operations in nested transactions on flush
      if (isNestedTransaction()) {
        getCurrentSession().clear();
      }
      throw e;
    }
  }

  @Override
  public E deleteAndFlush(E entity) {
    try {
      // Ensure entity is managed before removal
      E managedEntity = getCurrentSession().merge(entity);
      getCurrentSession().remove(managedEntity);
      flush();
      return managedEntity;
    } catch (Exception e) {
      // CRITICAL FIX: Clear session state after failed operations in nested transactions on flush
      if (isNestedTransaction()) {
        getCurrentSession().clear();
      }
      throw e;
    }
  }

  public E findById(Class<E> clazz, Long id) {
    return getCurrentSession().find(clazz, id);
  }

  // Get the current Hibernate session
  public Session getCurrentSession() {
    return SessionManager.getCurrentSession();
  }

  // Check if the current transaction is nested
  public boolean isNestedTransaction() {
    if (!TransactionSynchronizationManager.isActualTransactionActive()) {
      return false;
    }
    try {
      // Get the current transaction status bound to this thread
      var status = TransactionAspectSupport.currentTransactionStatus();
      if (status instanceof DefaultTransactionStatus defaultStatus) {
        return defaultStatus.hasSavepoint(); // true if NESTED
      }
    } catch (NoTransactionException e) {
      return false;
    }
    return false;
  }

  // Simplified transaction checking - removed dangerous session clearing
  public boolean isTransactionActive() {
    return TransactionSynchronizationManager.isActualTransactionActive();
  }

  // Flush session manually when needed (controlled by caller)
  public void flush() {
    if (isTransactionActive()) {
      getCurrentSession().flush();
    }
  }
}
