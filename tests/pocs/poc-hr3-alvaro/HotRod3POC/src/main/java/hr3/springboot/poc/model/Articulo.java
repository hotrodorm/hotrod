package hr3.springboot.poc.model;

public interface Articulo {
	java.lang.Long getArtId();

	void setArtId(final java.lang.Long artId);

	java.lang.String getArtDesc();

	void setArtDesc(final java.lang.String artDesc);

	hr3.springboot.poc.model.EnumArticuloType getTipo();

	void setTipo(final hr3.springboot.poc.model.EnumArticuloType tipo);

}