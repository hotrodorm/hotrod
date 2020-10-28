package org.hotrod.runtime.livesql;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.cursor.Cursor;

@Mapper
public interface LiveSQLMapper {

  @Select("${sql}")
  public List<Map<String, Object>> select(Map<String, Object> parameters);

  @Select("${sql}")
  public Cursor<Map<String, Object>> selectCursor(Map<String, Object> parameters);

}
