package app;

import java.sql.Connection;

import org.hotrod.converter.TypeConverter;
import org.springframework.stereotype.Component;

@Component
public class IntegerActiveConverter implements TypeConverter<Integer, ActiveEnum> {

  @Override
  public ActiveEnum decode(Integer raw, Connection conn) {
    if (raw == null) {
      return null;
    }
    return raw == 0 ? ActiveEnum.INACTIVE : ActiveEnum.ACTIVE;
  }

  @Override
  public Integer encode(ActiveEnum domain, Connection conn) {
    return domain == ActiveEnum.ACTIVE ? 1 : 0;
  }

}
