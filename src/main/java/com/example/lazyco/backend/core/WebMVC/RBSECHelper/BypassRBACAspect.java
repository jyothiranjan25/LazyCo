package com.example.lazyco.backend.core.WebMVC.RBSECHelper;

import com.example.lazyco.backend.core.AbstractAction;
import java.lang.reflect.Method;
import java.util.ArrayDeque;
import java.util.Deque;
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

  private final ThreadLocal<Deque<Boolean>> BYPASS_STACK = ThreadLocal.withInitial(ArrayDeque::new);

  /**
   * Aspect to bypass RBAC checks for methods or classes annotated with @BypassRBAC. It temporarily
   * sets the bypass flag in AbstractAction to true during the method execution.
   */
  @Around("@within(BypassRBAC) || @annotation(BypassRBAC)")
  public Object bypassRBAC(ProceedingJoinPoint joinPoint) throws Throwable {
    // 1️⃣ Save previous state
    boolean previous = abstractAction.isBypassRBAC();
    BYPASS_STACK.get().push(previous);

    // 2️⃣ Apply THIS method’s desired value
    boolean bypassValue = isBypassValue(joinPoint);

    abstractAction.setBypassRBAC(bypassValue);
    try {
      return joinPoint.proceed();
    } finally {
      // 3️⃣ Restore previous state
      Deque<Boolean> stack = BYPASS_STACK.get();
      boolean restore = stack.pop();
      if (stack.isEmpty()) {
        BYPASS_STACK.remove();
      }
      abstractAction.setBypassRBAC(restore);
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
