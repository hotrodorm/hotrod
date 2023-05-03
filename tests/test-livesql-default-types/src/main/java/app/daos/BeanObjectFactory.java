package app.daos;

import java.util.Collection;
import java.util.List;
import java.util.Properties;

import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class BeanObjectFactory extends DefaultObjectFactory implements ApplicationContextAware {

  private static final long serialVersionUID = 1L;

  private ApplicationContext applicationContext;

  @Override
  public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
    System.out.println("setApplicationContext() BEAN_OBJECT_FACTORY - applicationContext=" + applicationContext);
    this.applicationContext = applicationContext;
  }

  @Override
  public <T> T create(Class<T> type) {
    System.out.println("%%% bof 1 - this.applicationContext=" + this.applicationContext);
    System.out.println("%%% bof 1 - create: " + type);
    if (this.isCollection(type)) {
      return super.create(type);
    } else {
      T bean;
      bean = this.applicationContext.getBean(type);
      System.out.println("%%% bof 1 - bean=" + bean);
      return bean;
    }
  }

  @Override
  public <T> T create(Class<T> type, List<Class<?>> constructorArgTypes, List<Object> constructorArgs) {
    System.out.println("%%% bof 2 - create: " + type);
    if (this.isCollection(type)) {
      return super.create(type, constructorArgTypes, constructorArgs);
    } else {
      for (Class<?> p : constructorArgTypes) {
        System.out.println("%%% bof 2 - p: " + p.getName());
      }
      return this.applicationContext.getBean(type, constructorArgs);
    }
  }

  @Override
  public void setProperties(Properties properties) {
    System.out.println("%%% bof 3");
    super.setProperties(properties);
  }

  @Override
  public <T> boolean isCollection(Class<T> type) {
    System.out.println("%%% bof 4");
    return Collection.class.isAssignableFrom(type);
  }

}