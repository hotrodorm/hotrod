package helpers;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class Database1SessionFactory {

  private static final String RESOURCE = //
  "gen/empusamb/mappers/persistence/primitives/mybatis-configuration.xml";

  private static Database1SessionFactory instance = null;

  private SqlSessionFactory sqlSessionFactory = null;

  private Database1SessionFactory() throws SQLException {
    try {
      InputStream is = Resources.getResourceAsStream(RESOURCE);
      // File f = new File(resource);
      // InputStream is = new FileInputStream(f);
      this.sqlSessionFactory = new SqlSessionFactoryBuilder().build(is);
    } catch (IOException e) {
      throw new SQLException("Could not read MyBatis configuration.", e);
    }
  }

  public static synchronized Database1SessionFactory getInstance()
      throws SQLException {
    if (instance == null) {
      instance = new Database1SessionFactory();
    }
    return instance;
  }

  public SqlSessionFactory getSqlSessionFactory() {
    return sqlSessionFactory;
  }

}
