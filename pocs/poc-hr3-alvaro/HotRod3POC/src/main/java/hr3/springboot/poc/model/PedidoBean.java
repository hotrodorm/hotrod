package hr3.springboot.poc.model;

public interface PedidoBean {

	java.lang.Long getId();

	void setId(java.lang.Long id);

	java.sql.Timestamp getTimestamp();

	void setTimestamp(java.sql.Timestamp timestamp);

	java.lang.String getDescripcion();

	void setDescripcion(java.lang.String descripcion);

	java.lang.Long getIdPersona();

	void setIdPersona(java.lang.Long idPersona);

	String toJSON();

}