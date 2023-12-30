package org.hotrod.runtime.livesql;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.cursor.Cursor;

@Mapper
public interface LiveSQLMapper {

  @Select("${sql}")
  public List<Row> select(Map<String, Object> parameters);

  @Select("${sql}")
  public Cursor<Row> selectCursor(Map<String, Object> parameters);

  @Delete("${sql}")
  public int delete(Map<String, Object> parameters);

  @Update("${sql}")
  public int update(Map<String, Object> parameters);

  @Update("${sql}")
  public int insert(Map<String, Object> parameters);

}
