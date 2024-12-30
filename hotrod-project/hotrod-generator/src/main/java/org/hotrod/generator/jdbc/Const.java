package org.hotrod.generator.jdbc;

import org.hotrod.utils.AbstractClassWriter.ExternalClass;

public class Const {

  public static final ExternalClass AUTOWIRED = ExternalClass
      .of("org.springframework.beans.factory.annotation.Autowired");
  public static final ExternalClass COMPONENT = ExternalClass.of("org.springframework.stereotype.Component");
  public static final ExternalClass SCOPE = ExternalClass.of("org.springframework.context.annotation.Scope");
  public static final ExternalClass CONFIGURABLE_BEAN_FACTORY = ExternalClass
      .of("org.springframework.beans.factory.config.ConfigurableBeanFactory");

  public static final ExternalClass APPLICATION_CONTEXT_AWARE = ExternalClass
      .of("org.springframework.context.ApplicationContextAware");
  public static final ExternalClass QUALIFIER = ExternalClass
      .of("org.springframework.beans.factory.annotation.Qualifier");
  public static final ExternalClass SQL_SESSION = ExternalClass.of("org.apache.ibatis.session.SqlSession");

  public static final ExternalClass BEANS_EXCEPTION = ExternalClass.of("org.springframework.beans.BeansException");
  public static final ExternalClass LAZY = ExternalClass.of("org.springframework.context.annotation.Lazy");
  public static final ExternalClass VALUE = ExternalClass.of("org.springframework.beans.factory.annotation.Value");
  public static final ExternalClass APPLICATION_CONTEXT = ExternalClass
      .of("org.springframework.context.ApplicationContext");
  public static final ExternalClass POST_CONSTRUCT = ExternalClass.of("javax.annotation.PostConstruct");

  public static final ExternalClass SQL_EXCEPTION = ExternalClass.of("java.sql.SQLException");
  public static final ExternalClass CALLABLE_STATEMENT = ExternalClass.of("java.sql.CallableStatement");
  public static final ExternalClass PREPARED_STATEMENT = ExternalClass.of("java.sql.PreparedStatement");
  public static final ExternalClass RESULT_SET = ExternalClass.of("java.sql.ResultSet");

  public static final ExternalClass JDBC_TYPE = ExternalClass.of("org.apache.ibatis.type.JdbcType");
  public static final ExternalClass TYPE_HANDLER = ExternalClass.of("org.apache.ibatis.type.TypeHandler");

  public static final ExternalClass ASPECT = ExternalClass.of("org.aspectj.lang.annotation.Aspect");
  public static final ExternalClass BEFORE = ExternalClass.of("org.aspectj.lang.annotation.Before");
  public static final ExternalClass CONFIGURATION = ExternalClass
      .of("org.springframework.context.annotation.Configuration");

  public static final ExternalClass ORDER = ExternalClass.of("org.springframework.core.annotation.Order");

  public static final ExternalClass JOIN_POINT = ExternalClass.of("org.aspectj.lang.JoinPoint");
  public static final ExternalClass SPRING_BEAN_OBJECT_FACTORY = ExternalClass
      .of("org.hotrod.spring.SpringBeanObjectFactory");

  public static final ExternalClass HOTROD_TYPE_HANDLER = ExternalClass
      .of("org.hotrod.runtime.livesql.queries.typesolver.TypeHandler");

//public static final ExternalClass = ExternalClass.of("");
//public static final ExternalClass = ExternalClass.of("");
//public static final ExternalClass = ExternalClass.of("");

}
