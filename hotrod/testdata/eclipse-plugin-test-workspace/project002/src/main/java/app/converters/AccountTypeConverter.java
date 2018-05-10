package app.converters;

import org.hotrod.runtime.converter.TypeConverter;

import app.enums.AccountType;

public class AccountTypeConverter implements TypeConverter<Short, AccountType> {

  private static final short CHECKING_CODE = 1;
  private static final short SAVINGS_CODE = 2;
  private static final short INVESTMENTS_CODE = 3;
  private static final short RETIREMENT_CODE = 4;

  @Override
  public AccountType decode(final Short value) {
    if (value == null) {
      return null;
    }
    switch (value.shortValue()) {
    case CHECKING_CODE:
      return AccountType.CHECKING;
    case SAVINGS_CODE:
      return AccountType.SAVINGS;
    case INVESTMENTS_CODE:
      return AccountType.INVESTMENTS;
    case RETIREMENT_CODE:
      return AccountType.RETIREMENT;
    default:
      throw new IllegalArgumentException("Invalid code " + value + " for Account Type.");
    }
  }

  @Override
  public Short encode(final AccountType value) {
    if (value == null) {
      return null;
    }
    switch (value) {
    case CHECKING:
      return CHECKING_CODE;
    case SAVINGS:
      return SAVINGS_CODE;
    case INVESTMENTS:
      return INVESTMENTS_CODE;
    default:
      return RETIREMENT_CODE;
    }
  }

}
