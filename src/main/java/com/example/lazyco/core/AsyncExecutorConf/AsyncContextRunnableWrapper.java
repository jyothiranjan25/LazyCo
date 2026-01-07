package com.example.lazyco.core.AsyncExecutorConf;

import static com.example.lazyco.core.WebMVC.BeanProvider.getBean;

import com.example.lazyco.core.AbstractAction;
import com.example.lazyco.entities.UserManagement.AppUser.AppUserDTO;
import com.example.lazyco.entities.UserManagement.UserRole.UserRoleDTO;
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
  private final boolean isBypassRBAC;
  private final AppUserDTO appUserDTO;
  private final UserRoleDTO userRoleDTO;
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
    // Store Abstract Action context
    AbstractAction abstractAction = getBean(AbstractAction.class);
    this.isBypassRBAC = abstractAction.isBypassRBAC();
    this.appUserDTO = (AppUserDTO) abstractAction.getLoggedAppUser().clone();
    this.userRoleDTO = (UserRoleDTO) abstractAction.getLoggedUserRole().clone();
    // Store the current mapped diagnostic context
    Map<String, String> mdc = MDC.getCopyOfContextMap();
    this.taskMappedDiagnosticContext = mdc != null ? new HashMap<>(mdc) : new HashMap<>();
  }

  public void run() {
    // ThreadLocal variables to store the original values
    RequestAttributes originalRequestContext = RequestContextHolder.getRequestAttributes();
    Map<String, String> originalMappedDiagnosticContext = MDC.getCopyOfContextMap();
    // Store Abstract Action original context
    AbstractAction action = getBean(AbstractAction.class);
    boolean originalBypassRBAC = action.isBypassRBAC();
    AppUserDTO originalAppUserDTO = action.getLoggedAppUser();
    UserRoleDTO originalUserRoleDTO = action.getLoggedUserRole();
    try {
      RequestContextHolder.setRequestAttributes(this.taskRequestContext);
      MDC.setContextMap(this.taskMappedDiagnosticContext);
      // Set Abstract Action context for the async task
      action.setBypassRBAC(this.isBypassRBAC);
      action.setLoggedAppUser(this.appUserDTO);
      action.setLoggedUserRole(this.userRoleDTO);
      this.delegate.run();
    } finally {
      // Restore the original values
      RequestContextHolder.setRequestAttributes(originalRequestContext);
      MDC.setContextMap(originalMappedDiagnosticContext);
      // Restore Abstract Action original context
      action.setBypassRBAC(originalBypassRBAC);
      action.setLoggedAppUser(originalAppUserDTO);
      action.setLoggedUserRole(originalUserRoleDTO);
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
