package empusambcore.persistence;

import java.sql.SQLException;

import org.apache.ibatis.session.SqlSession;

public interface Insertable<T> {

  int insert() throws SQLException;

  int insert(final SqlSession sqlSession) throws SQLException;

}
