package app;

import java.util.Map;

import org.apache.ibatis.annotations.Select;

public interface MyMapper {

  @Select("select * from dt where id = 1")
  Map<String, Object> getDT();

}