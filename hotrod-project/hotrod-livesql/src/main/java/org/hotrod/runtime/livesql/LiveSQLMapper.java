package org.hotrod.runtime.livesql;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.cursor.Cursor;

@Mapper
public interface LiveSQLMapper {

  @Select("${sql}")
  public List<Row> select(Map<String, Object> parameters);

  @Select("${sql}")
  public Cursor<Row> selectCursor(Map<String, Object> parameters);

}
