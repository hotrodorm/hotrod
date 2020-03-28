package app.converters;

import org.hotrod.runtime.converter.TypeConverter;

import app.enums.PersonType;

public class PersonTypeConverter implements TypeConverter<Short, PersonType> {

  private static final short INDIVIDUAL_CODE = 1;
  private static final short COMMERCIAL_CODE = 2;
  private static final short GOVERNMENT_CODE = 3;

  @Override
  public PersonType decode(final Short value) {
    if (value == null) {
      return null;
    }
    switch (value.shortValue()) {
    case INDIVIDUAL_CODE:
      return PersonType.INDIVIDUAL;
    case COMMERCIAL_CODE:
      return PersonType.COMMERCIAL;
    case GOVERNMENT_CODE:
      return PersonType.GOVERNMENT;
    default:
      throw new IllegalArgumentException("Invalid code " + value + " for Person Type.");
    }
  }

  @Override
  public Short encode(final PersonType value) {
    if (value == null) {
      return null;
    }
    switch (value) {
    case INDIVIDUAL:
      return INDIVIDUAL_CODE;
    case COMMERCIAL:
      return COMMERCIAL_CODE;
    default:
      return GOVERNMENT_CODE;
    }
  }

}
