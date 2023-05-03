package org.hotrod.runtime.spring;

import java.util.Collection;
import java.util.List;
import java.util.Properties;

import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringBeanObjectFactory extends DefaultObjectFactory implements ApplicationContextAware {

  private static final long serialVersionUID = 1L;

  private ApplicationContext applicationContext;

  @Override
  public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }

  @Override
  public <T> T create(Class<T> type) {
    if (this.isCollection(type)) {
      return super.create(type);
    } else {
      T bean = this.applicationContext.getBean(type);
      return bean;
    }
  }

  @Override
  public <T> T create(Class<T> type, List<Class<?>> constructorArgTypes, List<Object> constructorArgs) {
    if (this.isCollection(type)) {
      return super.create(type, constructorArgTypes, constructorArgs);
    } else {
      return this.applicationContext.getBean(type, constructorArgs);
    }
  }

  @Override
  public void setProperties(Properties properties) {
    super.setProperties(properties);
  }

  @Override
  public <T> boolean isCollection(Class<T> type) {
    return Collection.class.isAssignableFrom(type);
  }

}
