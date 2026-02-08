package com.example.lazyco.core.AbstractClasses.DAO;

import com.example.lazyco.core.Logger.ApplicationLogger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * Centralized access to the Spring-managed Hibernate Session.
 *
 * <p>Rules:
 *
 * <ul>
 *   <li>Must be called inside a @Transactional method
 *   <li>Never opens or closes Sessions manually
 *   <li>Fails fast when misused
 * </ul>
 *
 * <p>Designed to work correctly with nested transactions (savepoint).
 */
@Component
public class SessionManager {

  /**
   * Static reference is intentional to allow legacy/static access. Marked volatile for safe
   * publication.
   */
  private static volatile SessionFactory sessionFactory;

  @Autowired
  public void setSessionFactory(SessionFactory sessionFactory) {
    SessionManager.sessionFactory = sessionFactory;
  }

  /**
   * Obtain the current Hibernate Session bound to the calling thread.
   *
   * @return the current Session
   * @throws IllegalStateException if no transaction/session is active
   */
  public static Session getCurrentSession() {
    if (sessionFactory == null) {
      throw new IllegalStateException("SessionFactory is not initialized");
    }

    try {
      Session session = sessionFactory.getCurrentSession();

      if (session == null) {
        throw new IllegalStateException("No Hibernate Session bound to current thread");
      }

      if (!session.isOpen()) {
        throw new IllegalStateException("Hibernate Session is closed");
      }

      return session;

    } catch (HibernateException ex) {
      ApplicationLogger.warn(
          "No Hibernate Session available. Ensure the calling method is annotated with @Transactional. "
              + "Thread="
              + Thread.currentThread().getName()
              + ", txActive="
              + TransactionSynchronizationManager.isActualTransactionActive(),
          ex);
      throw ex;
    }
  }

  /**
   * Check whether a real (database-backed) transaction is active.
   *
   * @return true if an actual transaction is active
   */
  public static boolean isTransactionActive() {
    return TransactionSynchronizationManager.isActualTransactionActive();
  }

  /** Check whether a usable Hibernate Session is available. Safe to call outside a transaction. */
  public static boolean isSessionAvailable() {
    try {
      return isTransactionActive() && getCurrentSession().isOpen();
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * Flush the current Hibernate Session if a transaction is active.
   *
   * <p>Normally unnecessary, as Spring flushes automatically at commit. Useful when:
   *
   * <ul>
   *   <li>you need constraint violations early
   *   <li>you rely on DB-generated values mid-transaction
   *   <li>working with savepoint
   * </ul>
   */
  public static void flush() {
    if (!isTransactionActive()) {
      return;
    }

    try {
      Session session = getCurrentSession();
      session.flush();
      ApplicationLogger.debug("Hibernate session flushed successfully");
    } catch (Exception e) {
      throw new RuntimeException(
          "Failed to flush Hibernate Session. txActive="
              + TransactionSynchronizationManager.isActualTransactionActive(),
          e);
    }
  }
}
