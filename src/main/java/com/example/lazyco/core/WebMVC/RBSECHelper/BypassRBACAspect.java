package com.example.lazyco.core.WebMVC.RBSECHelper;

import com.example.lazyco.core.AbstractAction;
import com.example.lazyco.core.Logger.ApplicationLogger;
import java.lang.reflect.Method;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class BypassRBACAspect {

  private final AbstractAction abstractAction;

  public BypassRBACAspect(AbstractAction abstractAction) {
    this.abstractAction = abstractAction;
  }

  /**
   * Aspect to bypass RBAC checks for methods or classes annotated with @BypassRBAC. It temporarily
   * sets the bypass flag in AbstractAction to true during the method execution.
   */
  @Around("@within(BypassRBAC) || @annotation(BypassRBAC)")
  public Object bypassRBAC(ProceedingJoinPoint joinPoint) throws Throwable {

    ApplicationLogger.info(
        "AOP BypassRBACAspect triggered for method: " + joinPoint.getSignature().toShortString());

    // Determine new state
    boolean bypassValue = isBypassValue(joinPoint);

    ApplicationLogger.info(
        "Setting bypassRBAC to "
            + bypassValue
            + " for method: "
            + joinPoint.getSignature().toShortString());

    abstractAction.pushBypassRBAC(bypassValue);

    try {
      return joinPoint.proceed();
    } finally {
      abstractAction.popBypassRBAC();
      ApplicationLogger.info(
          "Restoring bypassRBAC to "
              + abstractAction.isBypassRBAC()
              + " for method: "
              + joinPoint.getSignature().toShortString()
              + " after method execution");
    }
  }

  private static boolean isBypassValue(ProceedingJoinPoint joinPoint) {
    boolean bypassValue = true;

    // 1️⃣ Method-level annotation
    MethodSignature signature = (MethodSignature) joinPoint.getSignature();
    Method method = signature.getMethod();
    BypassRBAC methodAnnotation = method.getAnnotation(BypassRBAC.class);
    if (methodAnnotation != null) {
      bypassValue = methodAnnotation.value();
    } else {
      // 2️⃣ Class-level annotation
      Class<?> targetClass = joinPoint.getTarget().getClass();
      BypassRBAC classAnnotation = targetClass.getAnnotation(BypassRBAC.class);
      if (classAnnotation != null) {
        bypassValue = classAnnotation.value();
      }
    }
    return bypassValue;
  }
}
