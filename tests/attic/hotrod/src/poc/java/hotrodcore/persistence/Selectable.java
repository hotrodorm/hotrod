package empusambcore.persistence;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.session.SqlSession;

public interface Selectable<T, O> {

  List<T> select(O... orderBies) throws SQLException;

  List<T> select(SqlSession sqlSession, O... orderBies) throws SQLException;

}
