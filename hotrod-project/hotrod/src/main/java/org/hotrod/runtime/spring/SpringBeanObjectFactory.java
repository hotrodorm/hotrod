package org.hotrod.runtime.spring;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.SortedSet;

import org.apache.ibatis.reflection.ReflectionException;
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
    } else if (type == List.class || type == Collection.class || type == Iterable.class || type == Map.class
        || type == SortedSet.class || type == Set.class) {
      Class<?> clazz = super.resolveInterface(type);
      try {
        return (T) clazz.newInstance();
      } catch (InstantiationException | IllegalAccessException e) {
        throw new ReflectionException("Error instantiating " + type + " as a " + clazz.getName() + ". Cause: " + e, e);
      }
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
