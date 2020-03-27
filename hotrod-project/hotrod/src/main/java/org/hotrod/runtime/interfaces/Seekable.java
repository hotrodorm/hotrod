package org.hotrod.runtime.interfaces;

import java.sql.SQLException;

import org.apache.ibatis.session.SqlSession;

public interface Seekable<T, O> extends Persistable<T, O> {

  public int updateByPK() throws SQLException;

  public int updateByPK(final SqlSession sqlSession) throws SQLException;

  public int deleteByPK() throws SQLException;

  public int deleteByPK(final SqlSession sqlSession) throws SQLException;

}
