package fulltest.appconfig;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class SimpleSessionFactory {

  private static final String RESOURCE = "src/poctests/mappers/mybatis-config.xml";

  private static SimpleSessionFactory instance = null;

  private SqlSessionFactory sqlSessionFactory = null;

  private SimpleSessionFactory() throws SQLException {
    try {
      File f = new File(RESOURCE);
      InputStream is = new FileInputStream(f);
      this.sqlSessionFactory = new SqlSessionFactoryBuilder().build(is);
    } catch (IOException e) {
      throw new SQLException("Could not read MyBatis configuration.", e);
    }
  }

  public static synchronized SimpleSessionFactory getInstance()
      throws SQLException {
    if (instance == null) {
      instance = new SimpleSessionFactory();
    }
    return instance;
  }

  public SqlSessionFactory getSqlSessionFactory() {
    return sqlSessionFactory;
  }

}
