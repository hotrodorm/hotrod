package empusambcore.persistence;

import java.sql.SQLException;

import org.apache.ibatis.session.SqlSession;

public interface Updatable<T> {

  int update() throws SQLException;

  int update(final SqlSession sqlSession) throws SQLException;

}
