package com.example.lazyco.backend.core;

import com.example.lazyco.backend.core.Logger.ApplicationLogger;
import com.example.lazyco.backend.core.Utils.CommonConstrains;
import com.example.lazyco.backend.entities.UserManagement.AppUser.AppUserDTO;
import com.example.lazyco.backend.entities.UserManagement.UserGroup.UserGroupDTO;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class AbstractAction implements CommonConstrains {

  @Value("${app.environment:development}")
  private String environment;

  /** ThreadLocal storage to indicate if RBAC checks should be bypassed for the current thread. */
  private final ThreadLocal<Boolean> BYPASS_RBAC = ThreadLocal.withInitial(() -> false);

  public void setBypassRBAC(boolean bypassRBAC) {
    BYPASS_RBAC.set(bypassRBAC);
  }

  public boolean isBypassRBAC() {
    return BYPASS_RBAC.get();
  }

  /** ThreadLocal storage to indicate if the current job is a system job. */
  private final ThreadLocal<Boolean> SYSTEM_JOB = ThreadLocal.withInitial(() -> false);

  public void setSystemJob(boolean systemJob) {
    SYSTEM_JOB.set(systemJob);
  }

  public boolean isSystemJob() {
    return SYSTEM_JOB.get();
  }

  /** Configuration properties loaded from application settings */
  private volatile Properties properties = new Properties();

  public String getConfigProperties(String key) {
    return properties.getProperty(key, null);
  }

  private void setProperties(Properties properties) {
    this.properties = properties;
  }

  /*  Environment Checkers */
  public boolean isTestEnvironment() {
    return TEST_MODE.equals(System.getProperty("environment", "").toLowerCase());
  }

  public boolean isDevelopmentEnvironment() {
    return DEV_MODE.equals(System.getProperty("environment", "").toLowerCase());
  }

  /** ThreadLocal storage for per-thread properties */
  private final ThreadLocal<Properties> threadLocalProperties =
      ThreadLocal.withInitial(Properties::new);

  public void setThreadProperty(String key, String value) {
    threadLocalProperties.get().setProperty(key, value);
  }

  public String getThreadProperty(String key) {
    return threadLocalProperties.get().getProperty(key);
  }

  public void clearThreadLocals() {
    threadLocalProperties.remove();
    BYPASS_RBAC.remove();
    SYSTEM_JOB.remove();
  }

  @EventListener(ContextRefreshedEvent.class)
  public void initializeSystemProps() {
    setBypassRBAC(true);
    try {
      Properties properties = new Properties();
      properties.setProperty("environment", environment);
      // @TODO Load properties from configuration source
      setProperties(properties);
    } finally {
      ApplicationLogger.info("Initializing application properties...");
      setBypassRBAC(false);
    }
  }

  public AppUserDTO getLoggedInUser() {
    AppUserDTO appUserDTO = new AppUserDTO();
    appUserDTO.setUserId("JO");
    return appUserDTO;
  }

  public UserGroupDTO loggedInUserGroup() {
    UserGroupDTO userGroupDTO = new UserGroupDTO();
    userGroupDTO.setFullyQualifiedName("DEFAULT");
    return userGroupDTO;
  }
}
