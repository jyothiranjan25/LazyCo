package com.example.lazyco.core.AbstractClasses.DAO;

import com.example.lazyco.core.Logger.ApplicationLogger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.hibernate.SessionHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * Spring-managed SessionManager that works with @Transactional annotations. All transaction
 * management is now handled declaratively by Spring.
 */
@Component
public class SessionManager {

  private static SessionFactory sessionFactory;

  @Autowired
  public void setSessionFactoryObject(SessionFactory sessionFactory) {
    SessionManager.sessionFactory = sessionFactory;
  }

  /**
   * Get the current Hibernate session managed by Spring's @Transactional. This session is
   * automatically managed by Spring's transaction infrastructure.
   */
  public static Session getCurrentSession() {
    try {
      if (sessionFactory == null) {
        throw new IllegalStateException("SessionFactory is not initialized");
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
      throw new IllegalStateException(errorMessage, e);
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
      throw new RuntimeException("Failed to flush session", e);
    }
  }
}
