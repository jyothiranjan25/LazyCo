package com.example.lazyco.core;

import static com.example.lazyco.core.WebMVC.BeanProvider.getBean;

import com.example.lazyco.core.ConfigurationMaster.ConfigurationMasterDTO;
import com.example.lazyco.core.ConfigurationMaster.ConfigurationMasterService;
import com.example.lazyco.core.ConfigurationMaster.SystemSettingsMetaData.SystemSettingsKeys;
import com.example.lazyco.core.Logger.ApplicationLogger;
import com.example.lazyco.core.Utils.CommonConstants;
import com.example.lazyco.core.WebMVC.RBSECHelper.BypassRBAC;
import com.example.lazyco.entities.UserManagement.AppUser.AppUserDTO;
import com.example.lazyco.entities.UserManagement.UserGroup.UserGroupDTO;
import com.example.lazyco.entities.UserManagement.UserRole.UserRoleDTO;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Properties;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class AbstractAction implements CommonConstants {

  @Value("${app.environment:production}")
  private String environment;

  /** ThreadLocal storage to indicate if RBAC checks should be bypassed for the current thread. */
  private final ThreadLocal<Boolean> BYPASS_RBAC = ThreadLocal.withInitial(() -> false);

  public void setBypassRBAC(boolean bypassRBAC) {
    BYPASS_RBAC.set(bypassRBAC);
  }

  public boolean isBypassRBAC() {
    return BYPASS_RBAC.get();
  }

  /** ThreadLocal storage for System Jobs */
  private final ThreadLocal<Deque<SystemJobContext>> SYSTEM_JOB_CONTEXT_STACK =
      ThreadLocal.withInitial(ArrayDeque::new);

  public void setSystemJobUserContext(String userId, String userGroup) {
    SYSTEM_JOB_CONTEXT_STACK.get().push(new SystemJobContext(userId, userGroup));
  }

  public boolean isSystemJob() {
    return !SYSTEM_JOB_CONTEXT_STACK.get().isEmpty();
  }

  public String getSystemJobUserId() {
    Deque<SystemJobContext> stack = SYSTEM_JOB_CONTEXT_STACK.get();
    return stack.isEmpty() ? null : stack.peek().getUserId();
  }

  public String getSystemJobUserGroup() {
    Deque<SystemJobContext> stack = SYSTEM_JOB_CONTEXT_STACK.get();
    return stack.isEmpty() ? null : stack.peek().getUserGroup();
  }

  @Getter
  public static class SystemJobContext {
    private final String userId;
    private final String userGroup;

    public SystemJobContext(String userId, String userGroup) {
      this.userId = userId;
      this.userGroup = userGroup;
    }
  }

  public void popSystemJobUserContext() {
    Deque<SystemJobContext> stack = SYSTEM_JOB_CONTEXT_STACK.get();
    if (!stack.isEmpty()) {
      stack.pop();
    }
    if (stack.isEmpty()) {
      SYSTEM_JOB_CONTEXT_STACK.remove();
    }
  }

  /** ThreadLocal storage for logged-in user information */
  private final ThreadLocal<AppUserDTO> THREAD_LOCAL_USER = new ThreadLocal<>();

  public void setLoggedAppUser(AppUserDTO appUserDTO) {
    THREAD_LOCAL_USER.set(appUserDTO);
  }

  public AppUserDTO getLoggedAppUser() {
    return THREAD_LOCAL_USER.get();
  }

  /** ThreadLocal storage for logged-in user role information */
  private final ThreadLocal<UserRoleDTO> THREAD_LOCAL_USER_ROLE = new ThreadLocal<>();

  public void setLoggedUserRole(UserRoleDTO userRoleDTO) {
    THREAD_LOCAL_USER_ROLE.set(userRoleDTO);
  }

  public UserRoleDTO getLoggedUserRole() {
    return THREAD_LOCAL_USER_ROLE.get();
  }

  /** Configuration properties loaded from application settings */
  @Getter private volatile Properties properties = new Properties();

  public static String getConfigProperties(SystemSettingsKeys key) {
    return getBean(AbstractAction.class).getConfigProperties(key.getValue());
  }

  public String getConfigProperties(String key) {
    return properties.getProperty(key, null);
  }

  private void setProperties(Properties properties) {
    this.properties = properties;
  }

  /*  Environment Checkers */
  public boolean isTestEnvironment() {
    return TEST_MODE.equalsIgnoreCase(environment) || DEV_MODE.equalsIgnoreCase(environment);
  }

  /** ThreadLocal storage for per-thread properties */
  private final ThreadLocal<Properties> THREAD_LOCAL_PROPERTIES =
      ThreadLocal.withInitial(Properties::new);

  public void setThreadProperty(String key, String value) {
    THREAD_LOCAL_PROPERTIES.get().setProperty(key, value);
  }

  public String getThreadProperty(String key) {
    return THREAD_LOCAL_PROPERTIES.get().getProperty(key);
  }

  public void clearThreadLocals() {
    ApplicationLogger.info("Cleaning ThreadLocal Variables to prevent memory leaks...");
    ApplicationLogger.info(
        "BYPASS_RBAC: {}, SYSTEM_JOB: {}, THREAD_LOCAL_USER: {}, THREAD_LOCAL_USER_ROLE: {}, THREAD_LOCAL_PROPERTIES: {}",
        isBypassRBAC(),
        isSystemJob(),
        getLoggedInUser() != null ? getLoggedInUser().getUserId() : null,
        getLoggedInUserRole() != null ? getLoggedInUserRole().getRole().getRoleName() : null,
        THREAD_LOCAL_PROPERTIES.get());
    BYPASS_RBAC.remove();
    THREAD_LOCAL_USER.remove();
    THREAD_LOCAL_USER_ROLE.remove();
    THREAD_LOCAL_PROPERTIES.remove();
    SYSTEM_JOB_CONTEXT_STACK.remove();
  }

  @BypassRBAC
  @EventListener(ContextRefreshedEvent.class)
  public void initializeSystemProps() {
    Properties properties = new Properties();
    // Load application properties from classpath
    try {
      ApplicationLogger.info("Loading application properties from classpath...");
      properties.load(
          Thread.currentThread()
              .getContextClassLoader()
              .getResourceAsStream(APPLICATION_PROPERTIES));

    } catch (Exception e) {
      ApplicationLogger.error("Error loading application properties: ", e);
    }

    // load Configuration Master properties from database
    try {
      ApplicationLogger.info("Loading Configuration Master properties from database...");
      ConfigurationMasterService configurationMasterService =
          getBean(ConfigurationMasterService.class);
      List<ConfigurationMasterDTO> configMasterProperties =
          configurationMasterService.get(new ConfigurationMasterDTO());

      for (ConfigurationMasterDTO configMasterDTO : configMasterProperties) {
        String configKey = configMasterDTO.getConfigKey();
        String configValue;
        if (Boolean.TRUE.equals(configMasterDTO.getSensitive())) {
          configValue = configMasterDTO.getSensitiveConfigValue();
        } else {
          configValue = configMasterDTO.getConfigValue();
        }
        if (configKey != null && configValue != null) properties.put(configKey, configValue);
      }
    } catch (Exception e) {
      ApplicationLogger.error("Error loading Configuration Master properties: ", e);
    }
    setProperties(properties);
  }

  public AppUserDTO getLoggedInUser() {
    return getLoggedAppUser();
  }

  public UserRoleDTO getLoggedInUserRole() {
    return getLoggedUserRole();
  }

  public UserGroupDTO getLoggedInUserGroup() {
    UserRoleDTO userRoleDTO = getLoggedInUserRole();
    if (userRoleDTO == null) {
      return null;
    }
    return userRoleDTO.getUserGroup();
  }

  public boolean isAdminUser() {
    AppUserDTO appUserDTO = getLoggedInUser();
    return appUserDTO != null && Boolean.TRUE.equals(appUserDTO.getIsAdmin());
  }
}
