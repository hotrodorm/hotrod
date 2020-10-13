package org.hotrod.runtime.livesql;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface LiveSQLMapper {

  @Select("${sql}")
  public List<Map<String, Object>> select(@Param("sql") String sql);

}
