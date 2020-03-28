package com.company.sessionfactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class SessionFactory {

  private static final String RESOURCE = 
    "src/mybatis/mappers/primitives/mybatis-configuration.xml";

  private static SessionFactory instance = null;

  private SqlSessionFactory sqlSessionFactory = null;

  private SessionFactory() throws SQLException {
    try {
      File f = new File(RESOURCE);
      InputStream is = new FileInputStream(f);
      this.sqlSessionFactory = new SqlSessionFactoryBuilder().build(is);
    } catch (IOException e) {
      throw new SQLException("Could not read MyBatis configuration.", e);
    }
  }

  public static synchronized SessionFactory getInstance()
      throws SQLException {
    if (instance == null) {
      instance = new SessionFactory();
    }
    return instance;
  }

  public SqlSessionFactory getSqlSessionFactory() {
    return sqlSessionFactory;
  }

}
