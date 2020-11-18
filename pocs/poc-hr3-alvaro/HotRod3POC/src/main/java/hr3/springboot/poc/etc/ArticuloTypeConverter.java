package hr3.springboot.poc.etc;

import org.hotrod.runtime.converter.TypeConverter;

import hr3.springboot.poc.model.EnumArticuloType;

public class ArticuloTypeConverter implements TypeConverter<Integer, EnumArticuloType> {

	@Override
	public EnumArticuloType decode(Integer dbValue) {
		return (dbValue != null) ? EnumArticuloType.getEnum(dbValue) : null;
	}

	@Override
	public Integer encode(EnumArticuloType arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
