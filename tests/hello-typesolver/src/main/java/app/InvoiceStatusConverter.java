package app;

import java.sql.Connection;

import org.hotrod.converter.TypeConverter;
import org.springframework.stereotype.Component;

@Component
public class InvoiceStatusConverter implements TypeConverter<Integer, InvoiceStatus> {

  @Override
  public InvoiceStatus decode(Integer raw, Connection conn) {
    if (raw == null)
      return null;
    for (InvoiceStatus domain : InvoiceStatus.values()) {
      if (domain.getCode() == raw) {
        return domain;
      }
    }
    throw new RuntimeException("Could not convert raw-value '" + raw + "' to a InvoiceStatus value");
  }

  @Override
  public Integer encode(InvoiceStatus domain, Connection conn) {
    if (domain == null)
      return null;
    return domain.getCode();
  }

}
