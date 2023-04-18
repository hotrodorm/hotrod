package app.converters;

import org.hotrod.runtime.converter.TypeConverter;

public class DoubleConverter implements TypeConverter<Double, Double> {

  @Override
  public Double decode(Double raw) {
    System.out.println("### decoding: " + raw);
    return raw;
  }

  @Override
  public Double encode(Double appValue) {
    System.out.println("### encoding: " + appValue);
    return appValue;
  }

}
