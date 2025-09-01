package com.company.utils;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringBeanRetriever {

  private static final String SPRING_CONFIGURATION_FILE = "spring-configuration.xml";

  private static SpringBeanRetriever instance = null;

  private ApplicationContext context;

  private SpringBeanRetriever() {
    this.context = new ClassPathXmlApplicationContext(SPRING_CONFIGURATION_FILE);
  }

  private static synchronized SpringBeanRetriever getInstance() {
    if (instance == null) {
      instance = new SpringBeanRetriever();
    }
    return instance;
  }

  public static <T> T getBean(final String name, final Class<T> c) {
    SpringBeanRetriever instance = SpringBeanRetriever.getInstance();
    return c.cast(instance.context.getBean(name));
  }

  @SuppressWarnings("unchecked")
  public static <T> T getBean(final String name) {
    SpringBeanRetriever instance = SpringBeanRetriever.getInstance();
    return (T) instance.context.getBean(name);
  }

}
