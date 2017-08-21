package sessionfactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class DatabaseSessionFactory {

  private static final String RESOURCE = //
      "gen/hotrod/mappers/persistence/primitives/mybatis-configuration.xml";

  private static DatabaseSessionFactory instance = null;

  private SqlSessionFactory sqlSessionFactory = null;

  private DatabaseSessionFactory() throws SQLException {
    InputStream is = null;
    try {
      // InputStream is = Resources.getResourceAsStream(RESOURCE);
      File f = new File(RESOURCE);
      is = new FileInputStream(f);
      this.sqlSessionFactory = new SqlSessionFactoryBuilder().build(is);
    } catch (IOException e) {
      throw new SQLException("Could not read MyBatis configuration.", e);
    } finally {
      if (is != null) {
        try {
          is.close();
        } catch (IOException e) {
          throw new SQLException("Could not close MyBatis configuration.", e);
        }
      }
    }
  }

  public static synchronized DatabaseSessionFactory getInstance() throws SQLException {
    if (instance == null) {
      instance = new DatabaseSessionFactory();
    }
    return instance;
  }

  public SqlSessionFactory getSqlSessionFactory() {
    return sqlSessionFactory;
  }

}
