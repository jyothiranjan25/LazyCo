package com.example.lazyco.backend.core.WebMVC;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * Utility class to access Spring ApplicationContext and retrieve beans programmatically.
 *
 * <p>Usage: SpringContext.getBean(YourBeanClass.class);
 *
 * <p>SpringContext.getBean("yourBeanName");
 *
 * <p>use only when you cannot use dependency injection.(ex. in static methods)
 *
 * <p>DO NOT ABUSE IT.
 */
@Component
public class BeanProvider implements ApplicationContextAware {

  private static ApplicationContext context;

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    context = applicationContext;
  }

  public static <T> T getBean(Class<T> beanClass) {
    return context.getBean(beanClass);
  }

  public static Object getBean(String beanName) {
    return context.getBean(beanName);
  }

  public static ApplicationContext getApplicationContext() {
    return context;
  }

  public static <T> T getObjectProvider(Class<T> beanClass) {
    return context.getBeanProvider(beanClass).getIfAvailable();
  }

  public static ApplicationEventPublisher getPublisher() {
    return context;
  }
}
