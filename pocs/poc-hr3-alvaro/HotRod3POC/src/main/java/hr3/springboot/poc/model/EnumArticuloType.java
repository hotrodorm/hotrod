package hr3.springboot.poc.model;

public enum EnumArticuloType {
	ROPA(1001), ELECTRODOMESTICO(1002), ALIMENTO(1003);

	EnumArticuloType(int i) {
	}

	public static EnumArticuloType getEnum(Integer dbValue) {
		switch (dbValue) {
		case 1001:
			return ROPA;
		case 1002:
			return ROPA;
		case 1003:
			return ROPA;
		default:
			return null;
		}
	}
}
