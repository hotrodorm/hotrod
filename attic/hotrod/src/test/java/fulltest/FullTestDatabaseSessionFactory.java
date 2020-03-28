package fulltest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class FullTestDatabaseSessionFactory {

  private static final String RESOURCE = //
  "gen/hotrod/mappers/persistence/primitives/mybatis-configuration.xml";

  private static FullTestDatabaseSessionFactory instance = null;

  private SqlSessionFactory sqlSessionFactory = null;

  private FullTestDatabaseSessionFactory() throws SQLException {
    try {
      // InputStream is = Resources.getResourceAsStream(RESOURCE);
      File f = new File(RESOURCE);
      InputStream is = new FileInputStream(f);
      this.sqlSessionFactory = new SqlSessionFactoryBuilder().build(is);
    } catch (IOException e) {
      throw new SQLException("Could not read MyBatis configuration.", e);
    }
  }

  public static synchronized FullTestDatabaseSessionFactory getInstance()
      throws SQLException {
    if (instance == null) {
      instance = new FullTestDatabaseSessionFactory();
    }
    return instance;
  }

  public SqlSessionFactory getSqlSessionFactory() {
    return sqlSessionFactory;
  }

}
