package com.example.lazyco.backend.core.AsyncExecutorConf;

import static com.example.lazyco.backend.core.WebMVC.BeanProvider.getBean;

import com.example.lazyco.backend.core.AbstractAction;
import com.example.lazyco.backend.entities.UserManagement.AppUser.AppUserDTO;
import com.example.lazyco.backend.entities.UserManagement.UserRole.UserRoleDTO;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import org.slf4j.MDC;
import org.springframework.security.concurrent.DelegatingSecurityContextCallable;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

public final class AsyncContextCallableWrapper<T> implements Callable<T> {

  private final Callable<T> delegate;
  private final RequestAttributes taskRequestContext;
  private final boolean isBypassRBAC;
  private final AppUserDTO appUserDTO;
  private final UserRoleDTO userRoleDTO;
  private final Map<String, String> taskMappedDiagnosticContext;

  AsyncContextCallableWrapper(Callable<T> delegate) {
    this(delegate, null);
  }

  private AsyncContextCallableWrapper(Callable<T> delegate, SecurityContext securityContext) {
    // Set the delegate Callable with security context
    if (securityContext != null) {
      this.delegate = new DelegatingSecurityContextCallable<>(delegate, securityContext);
    } else {
      this.delegate = new DelegatingSecurityContextCallable<>(delegate);
    }
    // Store the current request context
    this.taskRequestContext = RequestContextHolder.getRequestAttributes();
    // Store Abstract Action context
    AbstractAction abstractAction = getBean(AbstractAction.class);
    this.isBypassRBAC = abstractAction.isBypassRBAC();
    this.appUserDTO = (AppUserDTO) abstractAction.getLoggedAppUser().clone();
    this.userRoleDTO = (UserRoleDTO) abstractAction.getLoggedUserRole().clone();
    // Store the current async system job value
    Map<String, String> mdc = MDC.getCopyOfContextMap();
    this.taskMappedDiagnosticContext = mdc != null ? new HashMap<>(mdc) : new HashMap<>();
  }

  public T call() throws Exception {
    // ThreadLocal variables to store the original values
    RequestAttributes originalRequestContext = RequestContextHolder.getRequestAttributes();
    Map<String, String> originalMappedDiagnosticContext = MDC.getCopyOfContextMap();
    // Store Abstract Action original context
    AbstractAction action = getBean(AbstractAction.class);
    boolean originalBypassRBAC = action.isBypassRBAC();
    AppUserDTO originalAppUserDTO = action.getLoggedAppUser();
    UserRoleDTO originalUserRoleDTO = action.getLoggedUserRole();

    T var1;
    try {
      RequestContextHolder.setRequestAttributes(this.taskRequestContext);
      MDC.setContextMap(this.taskMappedDiagnosticContext);
      // Set Abstract Action context for the async task
      action.setBypassRBAC(this.isBypassRBAC);
      action.setLoggedAppUser(this.appUserDTO);
      action.setLoggedUserRole(this.userRoleDTO);
      var1 = this.delegate.call();
    } finally {
      // Restore the original values
      RequestContextHolder.setRequestAttributes(originalRequestContext);
      MDC.setContextMap(originalMappedDiagnosticContext);
      // Restore Abstract Action original context
      action.setBypassRBAC(originalBypassRBAC);
      action.setLoggedAppUser(originalAppUserDTO);
      action.setLoggedUserRole(originalUserRoleDTO);
    }
    return (T) var1;
  }

  public String toString() {
    return this.delegate.toString();
  }

  public static <V> Callable<V> create(Callable<V> delegate, SecurityContext securityContext) {
    return securityContext != null
        ? new AsyncContextCallableWrapper<>(delegate, securityContext)
        : new AsyncContextCallableWrapper<>(delegate);
  }

  static <V> Callable<V> create(Callable<V> delegate) {
    return new AsyncContextCallableWrapper<>(delegate);
  }
}
