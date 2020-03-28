package empusambcore.persistence;

import java.sql.SQLException;

import org.apache.ibatis.session.SqlSession;

public interface Deletable<T> {

  int delete() throws SQLException;

  int delete(final SqlSession sqlSession) throws SQLException;

}
