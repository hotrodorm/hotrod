package app;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@Component
public class TH {

  @MappedTypes(LocalDate.class)
  public class LocalDateTypeHandler extends BaseTypeHandler<LocalDate> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, LocalDate parameter, JdbcType jdbcType)
        throws SQLException {
      System.out.println("####### 1");
      // TODO Auto-generated method stub
    }

    @Override
    public LocalDate getNullableResult(ResultSet rs, String columnName) throws SQLException {
      System.out.println("####### 2");
      // TODO Auto-generated method stub
      return null;
    }

    @Override
    public LocalDate getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
      System.out.println("####### 3");
      // TODO Auto-generated method stub
      return null;
    }

    @Override
    public LocalDate getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
      System.out.println("####### 4");
      // TODO Auto-generated method stub
      return null;
    }

  }

  @MappedTypes(LocalDateTime.class)
  public class LocalDateTimeTypeHandler extends BaseTypeHandler<LocalDateTime> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, LocalDateTime parameter, JdbcType jdbcType)
        throws SQLException {
      System.out.println("####### 1");
      // TODO Auto-generated method stub
    }

    @Override
    public LocalDateTime getNullableResult(ResultSet rs, String columnName) throws SQLException {
      System.out.println("####### 2");
      // TODO Auto-generated method stub
      return null;
    }

    @Override
    public LocalDateTime getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
      System.out.println("####### 3");
      // TODO Auto-generated method stub
      return null;
    }

    @Override
    public LocalDateTime getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
      System.out.println("####### 4");
      // TODO Auto-generated method stub
      return null;
    }

  }

}
