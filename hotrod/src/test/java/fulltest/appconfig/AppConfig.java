package fulltest.appconfig;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.session.SqlSession;

public class AppConfig {

  // DAO Properties (database columns)

  protected java.lang.Integer configId = null;
  protected java.lang.String configName = null;
  protected java.lang.String configValue = null;

  public AppConfig() {
  }

  // static select

  public static List<AppConfig> select() throws SQLException {
    SqlSession sqlSession = null;
    try {
      sqlSession = SimpleSessionFactory.getInstance().getSqlSessionFactory()
          .openSession();
      return sqlSession
          .selectList("simple.appConfig.selectAll");
    } finally {
      if (sqlSession != null)
        sqlSession.close();
    }
  }

  // getters & setters

  public final java.lang.Integer getConfigId() {
    return this.configId;
  }

  public final void setConfigId(final java.lang.Integer configId) {
    this.configId = configId;
  }

  public final java.lang.String getConfigName() {
    return this.configName;
  }

  public final void setConfigName(final java.lang.String configName) {
    this.configName = configName;
  }

  public final java.lang.String getConfigValue() {
    return this.configValue;
  }

  public final void setConfigValue(final java.lang.String configValue) {
    this.configValue = configValue;
  }

  // to string

  public String toString() {
    java.lang.StringBuilder sb = new java.lang.StringBuilder();
    sb.append("[");
    sb.append(this.configId + ", ");
    sb.append(this.configName + ", ");
    sb.append(this.configValue);
    sb.append("]");
    return sb.toString();
  }

}
