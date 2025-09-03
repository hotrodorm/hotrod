package app;

import java.sql.Connection;

import org.hotrod.converter.TypeConverter;

import app.AccountTypeConverter.AccountType;

public class AccountTypeConverter implements TypeConverter<String, AccountType> {

  public static enum AccountType {

    PEN1, CHK2, SAV3, INV4;

    public static AccountType parse(String raw) {
      for (AccountType t : AccountType.values()) {
        if (t.name().substring(0, 3).equals(raw)) {
          return t;
        }
      }
      throw new RuntimeException("Could not decode database value: Account type '" + raw + "' does not exist.");
    }
  }

  @Override
  public AccountType decode(String raw, Connection conn) {
    if (raw == null) {
      return null;
    }
    return AccountType.parse(raw);
  }

  @Override
  public String encode(AccountType domain, Connection conn) {
    return domain == null ? null : domain.name();
  }

}
