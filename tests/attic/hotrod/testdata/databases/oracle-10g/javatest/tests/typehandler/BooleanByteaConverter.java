package tests.typehandler;

import org.hotrod.runtime.converter.TypeConverter;

public class BooleanByteaConverter implements TypeConverter<byte[], Boolean> {

  @Override
  public Boolean decode(final byte[] value) {
    if (value == null) {
      return null;
    }
    return value.length > 0 && value[0] != 0;
  }

  @Override
  public byte[] encode(final Boolean value) {
    if (value == null) {
      return null;
    }
    return value ? new byte[1] : new byte[0];
  }

}
