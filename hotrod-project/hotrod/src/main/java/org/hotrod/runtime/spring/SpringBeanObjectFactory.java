package org.hotrod.runtime.spring;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.logging.Logger;

import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.hotrod.runtime.livesql.Row;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration(proxyBeanMethods = false)
public class SpringBeanObjectFactory extends DefaultObjectFactory implements ApplicationContextAware {

  private static final long serialVersionUID = 1L;

  private static Logger log = Logger.getLogger(SpringBeanObjectFactory.class.getName());

  private ApplicationContext applicationContext;

  @Value("${hotrod.vo.instantiation:bean}")
  private String voInstantiation;

  private enum VOInstantiationType {
    BEAN, POJO
  };

  private VOInstantiationType voInstantiationType;

  @Override
  public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;

    // parse voInstantiation

    if (this.voInstantiationType == null) {
      if ("bean".equals(this.voInstantiation)) {
        this.voInstantiationType = VOInstantiationType.BEAN;
      } else if ("pojo".equals(this.voInstantiation)) {
        this.voInstantiationType = VOInstantiationType.POJO;
      } else {
        throw new RuntimeException("Invalid value '" + this.voInstantiation
            + "' in property 'hotrod.vo.instantiation'. Must be either 'bean' (default) or 'pojo'.");
      }
      log.info(
          "[HotRod object factory type: " + this.voInstantiationType + " -- @" + System.identityHashCode(this) + " ]");
    }

  }

  @Override
  public <T> T create(final Class<T> type) {
    if (this.voInstantiationType == VOInstantiationType.BEAN) {
      // Try to instantiate as a bean
      if (this.isCollection(type) || type == List.class || type == Collection.class || type == Iterable.class
          || type == Map.class || type == SortedSet.class || type == Set.class || type == Row.class) {
        // Cannot be instantiated as a bean. Will be a POJO anyway
        return super.create(type);
      } else {
        // Instantiate a bean
        return this.applicationContext.getBean(type);
      }
    } else {
      // will always instantiate as a POJO, per configuration setting
      return super.create(type);
    }
  }

}
