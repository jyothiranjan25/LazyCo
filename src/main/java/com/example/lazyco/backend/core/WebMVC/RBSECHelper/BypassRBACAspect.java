package com.example.lazyco.backend.core.WebMVC.RBSECHelper;

import com.example.lazyco.backend.core.AbstractAction;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class BypassRBACAspect {

  private final AbstractAction abstractAction;

  public BypassRBACAspect(AbstractAction abstractAction) {
    this.abstractAction = abstractAction;
  }

  @Around("@within(BypassRBAC) || @annotation(BypassRBAC)")
  public Object bypassRBAC(ProceedingJoinPoint joinPoint) throws Throwable {
    boolean alreadyBypassed = abstractAction.isBypassRBAC();
    if (!alreadyBypassed) {
      abstractAction.setBypassRBAC(true);
    }
    try {
      return joinPoint.proceed();
    } finally {
      if (!alreadyBypassed) {
        abstractAction.setBypassRBAC(false);
      }
    }
  }
}
