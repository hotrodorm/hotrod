package selects.converters;

import org.hotrod.runtime.converter.TypeConverter;

import selects.enums.AccountStatus;

public class AccountStatusConverter implements TypeConverter<Short, AccountStatus> {

  private static final short INACTIVE_CODE = 0;
  private static final short ACTIVE_CODE = 1;

  @Override
  public AccountStatus decode(final Short value) {
    if (value == null) {
      return null;
    }
    return value.shortValue() == INACTIVE_CODE ? AccountStatus.INACTIVE : AccountStatus.ACTIVE;
  }

  @Override
  public Short encode(final AccountStatus value) {
    if (value == null) {
      return null;
    }
    return value == AccountStatus.ACTIVE ? ACTIVE_CODE : INACTIVE_CODE;
  }

}
