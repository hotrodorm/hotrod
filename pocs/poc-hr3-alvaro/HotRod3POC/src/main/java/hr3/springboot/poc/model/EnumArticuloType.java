package hr3.springboot.poc.model;

public enum EnumArticuloType {
	ROPA('r'), ELECTRODOMESTICO('e'), ALIMENTO('a'), OTRO('x');

	private char i;

	EnumArticuloType(char i) {
		this.i = i;
	}

	public char getValue() {
		return i;
	}

	public static EnumArticuloType getEnum(Character dbValue) {
		switch (dbValue) {
		case 'e':
			return ELECTRODOMESTICO;
		case 'a':
			return ALIMENTO;
		case 'r':
			return ROPA;
		default:
			return OTRO;
		}
	}
}
