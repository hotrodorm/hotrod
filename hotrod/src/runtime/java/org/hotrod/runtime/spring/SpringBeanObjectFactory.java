package org.hotrod.runtime.spring;

import java.util.List;
import java.util.Properties;

import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SpringBeanObjectFactory implements ObjectFactory, ApplicationContextAware {

  private DefaultObjectFactory defaultObjectFactory = new DefaultObjectFactory();
  private ApplicationContext applicationContext = null;

  @Override
  public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
    System.out.println("$$$ HotRodObjectFactory::setApplicationContext(applicationContext)");
    this.applicationContext = applicationContext;
  }

  @Override
  public <T> T create(final Class<T> type) {
    System.out.println("$$$ HotRodObjectFactory::create(" + type.getName() + ")");
    try {
      return this.applicationContext.getBean(type);
    } catch (NoSuchBeanDefinitionException e) {
      return this.defaultObjectFactory.create(type);
    }
  }

  @Override
  public <T> T create(final Class<T> type, final List<Class<?>> constructorArgTypes,
      final List<Object> constructorArgs) {
    System.out.println("$$$ HotRodObjectFactory::create(type, constructorArgTypes, constructorArgs)");
    try {
      return this.applicationContext.getBean(type, constructorArgs.toArray());
    } catch (NoSuchBeanDefinitionException e) {
      return this.defaultObjectFactory.create(type, constructorArgTypes, constructorArgs);
    }
  }

  @Override
  public <T> boolean isCollection(final Class<T> type) {
    System.out.println("$$$ HotRodObjectFactory::isCollection(type)");
    return this.defaultObjectFactory.isCollection(type);
  }

  @Override
  public void setProperties(final Properties properties) {
    System.out.println("$$$ HotRodObjectFactory::setProperties(properties)");
  }

}
