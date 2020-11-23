package hr3.springboot.poc.etc;

import org.hotrod.runtime.converter.TypeConverter;

import hr3.springboot.poc.model.EnumArticuloType;

public class ArticuloTypeConverter implements TypeConverter<String, EnumArticuloType> {

	@Override
	public EnumArticuloType decode(String dbValue) {
		return (dbValue != null) ? EnumArticuloType.getEnum(dbValue.charAt(0)) : null;
	}

	@Override
	public String encode(EnumArticuloType arg0) {
		return String.valueOf(arg0.getValue());
	}

}
