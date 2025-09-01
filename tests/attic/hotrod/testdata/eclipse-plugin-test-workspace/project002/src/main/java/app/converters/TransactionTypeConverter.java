package app.converters;

import org.hotrod.runtime.converter.TypeConverter;

import app.enums.TransactionType;

public class TransactionTypeConverter implements TypeConverter<Short, TransactionType> {

  private static final short CASHIER_CODE = 1;
  private static final short ONLINE_CODE = 2;
  private static final short ATM_CODE = 3;
  private static final short THIRD_PARTY_CODE = 4;

  @Override
  public TransactionType decode(final Short value) {
    if (value == null) {
      return null;
    }
    switch (value.shortValue()) {
    case CASHIER_CODE:
      return TransactionType.CASHIER;
    case ONLINE_CODE:
      return TransactionType.ONLINE;
    case ATM_CODE:
      return TransactionType.ATM;
    case THIRD_PARTY_CODE:
      return TransactionType.THIRD_PARTY;
    default:
      throw new IllegalArgumentException("Invalid code " + value + " for Transaction Type.");
    }
  }

  @Override
  public Short encode(final TransactionType value) {
    if (value == null) {
      return null;
    }
    switch (value) {
    case CASHIER:
      return CASHIER_CODE;
    case ONLINE:
      return ONLINE_CODE;
    case ATM:
      return ATM_CODE;
    default:
      return THIRD_PARTY_CODE;
    }
  }

}
