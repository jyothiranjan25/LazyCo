package com.example.lazyco.backend.core.AsyncExecutorConf;

import java.util.HashMap;
import java.util.Map;
import org.slf4j.MDC;
import org.springframework.security.concurrent.DelegatingSecurityContextRunnable;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

public final class AsyncContextRunnableWrapper implements Runnable {

  private final Runnable delegate;
  private final RequestAttributes taskRequestContext;
  private final Map<String, String> taskMappedDiagnosticContext;

  AsyncContextRunnableWrapper(Runnable delegate) {
    this(delegate, null);
  }

  private AsyncContextRunnableWrapper(Runnable delegate, SecurityContext securityContext) {
    // Set the delegate Runnable with security context
    if (securityContext != null) {
      this.delegate = new DelegatingSecurityContextRunnable(delegate, securityContext);
    } else {
      this.delegate = new DelegatingSecurityContextRunnable(delegate);
    }
    // Store the current request context
    this.taskRequestContext = RequestContextHolder.getRequestAttributes();
    // Store the current mapped diagnostic context
    Map<String, String> mdc = MDC.getCopyOfContextMap();
    this.taskMappedDiagnosticContext = mdc != null ? new HashMap<>(mdc) : new HashMap<>();
  }

  public void run() {
    // ThreadLocal variables to store the original values
    RequestAttributes originalRequestContext = RequestContextHolder.getRequestAttributes();
    Map<String, String> originalMappedDiagnosticContext = MDC.getCopyOfContextMap();
    try {
      RequestContextHolder.setRequestAttributes(this.taskRequestContext);
      MDC.setContextMap(this.taskMappedDiagnosticContext);
      this.delegate.run();
    } finally {
      // Restore the original values
      RequestContextHolder.setRequestAttributes(originalRequestContext);
      MDC.setContextMap(originalMappedDiagnosticContext);
    }
  }

  public String toString() {
    return this.delegate.toString();
  }

  public static Runnable create(Runnable delegate, SecurityContext securityContext) {
    return securityContext != null
        ? new AsyncContextRunnableWrapper(delegate, securityContext)
        : new AsyncContextRunnableWrapper(delegate);
  }

  static Runnable create(Runnable delegate) {
    return new AsyncContextRunnableWrapper(delegate);
  }
}
