package com.example.lazyco.backend.core.AbstractClasses.DAO;

import com.example.lazyco.backend.core.Exceptions.ExceptionWrapper;
import com.example.lazyco.backend.core.Logger.ApplicationLogger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.SessionHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Component
public class TransactionalWrapper {

  private static SessionFactory sessionFactory;

  @Autowired
  public void injectDependence(SessionFactory sessionFactory) {
    TransactionalWrapper.sessionFactory = sessionFactory;
  }

  public static Session getCurrentSession() {
    try {
      if (sessionFactory == null) {
        throw new ExceptionWrapper("SessionFactory is not initialized");
      }

      // First try to get the session directly from SessionFactory
      Session session = sessionFactory.getCurrentSession();

      // Validate the session is open and usable
      if (session != null && session.isOpen()) {
        return session;
      } else {
        throw new HibernateException("Session is null or closed");
      }

    } catch (HibernateException e) {
      ApplicationLogger.error(
          "No session bound to thread - ensure method is @Transactional: " + e.getMessage());

      // Fallback: try to get session from Spring's transaction synchronization
      try {
        SessionHolder sessionHolder =
            (SessionHolder) TransactionSynchronizationManager.getResource(sessionFactory);
        if (sessionHolder != null) {
          sessionHolder.getSession();
          Session session = sessionHolder.getSession();
          if (session.isOpen()) {
            ApplicationLogger.debug(
                "Retrieved session from TransactionSynchronizationManager fallback");
            return session;
          }
        }
      } catch (Exception fallbackException) {
        ApplicationLogger.error(
            "Fallback session retrieval also failed: " + fallbackException.getMessage());
      }

      // If we get here, no session is available
      String errorMessage =
          "No active transaction found. Ensure the calling method is annotated with @Transactional. "
              + "Thread: "
              + Thread.currentThread().getName()
              + ", Transaction active: "
              + TransactionSynchronizationManager.isActualTransactionActive()
              + ", Synchronization active: "
              + TransactionSynchronizationManager.isSynchronizationActive();

      ApplicationLogger.error(errorMessage);
      throw new ExceptionWrapper(errorMessage, e);
    }
  }

  /** Check if a transaction is currently active. */
  public static boolean isTransactionActive() {
    return TransactionSynchronizationManager.isSynchronizationActive()
        && TransactionSynchronizationManager.isActualTransactionActive();
  }

  /** Check if a session is currently open and bound to the current thread. */
  public static boolean isSessionAvailable() {
    try {
      Session session = getCurrentSession();
      return session.isOpen();
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * Flush the current session if available. Note: Spring will automatically flush at transaction
   * commit.
   */
  public static void flush() {
    try {
      Session session = getCurrentSession();
      if (session.isOpen()) {
        session.flush();
        ApplicationLogger.debug("Session flushed successfully");
      }
    } catch (Exception e) {
      ApplicationLogger.error("Error flushing session: " + e.getMessage());
      throw e;
    }
  }
}
