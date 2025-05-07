package app;

import java.sql.Connection;
import java.sql.SQLException;

import org.hotrod.converter.TypeConverter;

import app.AccountTypeConverter.AccountType;

public class AccountTypeConverter implements TypeConverter<String, AccountType> {

  public static enum AccountType {

    PEN, CHK, SAV, INV;

    public static AccountType parse(String raw) {
      for (AccountType t : AccountType.values()) {
        if (t.name().equals(raw)) {
          return t;
        }
      }
      throw new RuntimeException("Could not decode database value: Account type '" + raw + "' does not exist.");
    }
  }

  @Override
  public AccountType decode(String raw, Connection conn) throws SQLException {
    if (raw == null) {
      return null;
    }
    return AccountType.parse(raw);
  }

  @Override
  public String encode(AccountType domain, Connection conn) throws SQLException {
    return domain == null ? null : domain.name();
  }

}
