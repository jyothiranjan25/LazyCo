package com.example.lazyco.backend.core.AspectConf;

import com.example.lazyco.backend.core.Logger.ApplicationLogger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Aspect
@Component
public class TransactionTrackingAspect {

  @Around(
      "@annotation(org.springframework.transaction.annotation.Transactional(value=\"transactionManager\")) && "
          + "!@annotation(org.springframework.transaction.annotation.Transactional(value=\"mongoTransactionManager\"))")
  public Object trackTransaction(ProceedingJoinPoint joinPoint) throws Throwable {
    Object result = null;

    Class<?> targetClass = joinPoint.getTarget().getClass();
    String className = targetClass.getSimpleName(); // You can use getName() for fully qualified
    String methodName = joinPoint.getSignature().getName();

    boolean isTransactionActiveBefore =
        TransactionSynchronizationManager.isActualTransactionActive();
    ApplicationLogger.debug(
        String.format(
            "Starting method '%s' in class '%s'. Transaction active before method: %s",
            methodName, className, isTransactionActiveBefore));

    // Register a listener to track transaction commit/rollback
    TransactionSynchronizationManager.registerSynchronization(
        new TransactionSynchronization() {

          @Override
          public void beforeCommit(boolean readOnly) {
            ApplicationLogger.debug(
                String.format(
                    "Transaction for method '%s' in class '%s' is about to commit. Read-only: %s",
                    methodName, className, readOnly));
          }

          @Override
          public void afterCommit() {
            ApplicationLogger.debug(
                String.format(
                    "Transaction for method '%s' in class '%s' has been committed successfully",
                    methodName, className));
          }

          @Override
          public void afterCompletion(int status) {
            if (status == STATUS_COMMITTED) {
              ApplicationLogger.debug(
                  String.format(
                      "Transaction for method '%s' in class '%s' completed successfully",
                      methodName, className));
            } else if (status == STATUS_ROLLED_BACK) {
              ApplicationLogger.debug(
                  String.format(
                      "Transaction for method '%s' in class '%s' was rolled back",
                      methodName, className));
            }
          }

          @Override
          public void flush() {
            // called before flushing to the database
            ApplicationLogger.debug(
                String.format(
                    "Flushing changes in method '%s' of class '%s'", methodName, className));
          }

          @Override
          public void suspend() {
            ApplicationLogger.debug(
                String.format(
                    "Transaction suspended in method '%s' of class '%s'", methodName, className));
          }

          @Override
          public void resume() {
            ApplicationLogger.debug(
                String.format(
                    "Transaction resumed in method '%s' of class '%s'", methodName, className));
          }

          @Override
          public void savepoint(Object savepoint) {
            ApplicationLogger.debug(
                String.format(
                    "Savepoint( '%s' ) created in method '%s' of class '%s'",
                    savepoint, methodName, className));
          }

          @Override
          public void savepointRollback(Object savepoint) {
            ApplicationLogger.debug(
                String.format(
                    "Rolled back to savepoint( '%s' ) in method '%s' of class '%s'",
                    savepoint, methodName, className));
          }
        });

    try {
      result = joinPoint.proceed();
    } catch (Throwable t) {
      ApplicationLogger.debug(
          String.format(
              "Exception occurred in method '%s' of class '%s'. Transaction may be rolled back.",
              methodName, className));
      throw t; // Rethrow to let Spring handle rollback
    }

    ApplicationLogger.debug(
        String.format(
            "Finished executing method '%s' in class '%s'. Transaction is still active inside method: %s",
            methodName, className, TransactionSynchronizationManager.isActualTransactionActive()));

    return result;
  }
}
