package app.test.dynmapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.builder.SqlSourceBuilder;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.MappedStatement.Builder;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;

public class DynMapper {

  private SqlSession session;

  private List<Object> executeSelect(String sqliteStatementId, Map<String, Object> params) {
    return session.selectList(sqliteStatementId, params);
  }

  private static class MyParameterTypes {

  }

  private void createSelect(String statementId, String sql, Class<?> resultType) {
    Configuration ibatisConfig = session.getConfiguration();
    Map<String, Object> params = new HashMap<>();
    SqlSource sqlSource = new SqlSourceBuilder(ibatisConfig).parse(sql, MyParameterTypes.class, params);
    Builder statement = new MappedStatement.Builder(ibatisConfig, statementId, sqlSource, SqlCommandType.SELECT);

    List<ResultMapping> resultMapList = new ArrayList<ResultMapping>();

    ResultMap resultMap = new ResultMap.Builder(ibatisConfig, statementId, resultType, resultMapList, true).build();
    ibatisConfig.addResultMap(resultMap);

    List<ResultMap> resultMaps = new ArrayList<ResultMap>();
    resultMaps.add(resultMap);
    statement.resultMaps(resultMaps);

    ibatisConfig.addMappedStatement(statement.build());
  }

}
