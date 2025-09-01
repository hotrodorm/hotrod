package hr3.springboot.poc.model;

public interface PersonaBean {

	java.lang.Long getId();

	void setId(java.lang.Long id);

	java.lang.String getNombre();

	void setNombre(java.lang.String nombre);

	java.lang.String getApellido();

	void setApellido(java.lang.String apellido);

	java.sql.Date getFechaNacimiento();

	void setFechaNacimiento(java.sql.Date fechaNacimiento);

	String toJSON();

}