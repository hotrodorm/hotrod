package sessionfactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class DatabaseSessionFactory {

  private static final String RESOURCE = //
      "gen/hotrod/mappers/persistence/primitives/mybatis-configuration.xml";

  private static DatabaseSessionFactory instance = null;

  private SqlSessionFactory sqlSessionFactory = null;

  private DatabaseSessionFactory() {
    InputStream is = null;
    try {
      File f = new File(RESOURCE);
      is = new FileInputStream(f);
      this.sqlSessionFactory = new SqlSessionFactoryBuilder().build(is);
    } catch (IOException e) {
      throw new PersistenceException("Could not read MyBatis configuration.", e);
    } finally {
      if (is != null) {
        try {
          is.close();
        } catch (IOException e) {
          throw new PersistenceException("Could not close MyBatis configuration.", e);
        }
      }
    }
  }

  public static synchronized DatabaseSessionFactory getInstance() {
    if (instance == null) {
      instance = new DatabaseSessionFactory();
    }
    return instance;
  }

  public SqlSessionFactory getSqlSessionFactory() {
    return sqlSessionFactory;
  }

}
