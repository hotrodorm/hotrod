package hr3.springboot.poc.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import hr3.springboot.poc.hotrod.PersonaImpl;
import hr3.springboot.poc.hotrod.primitives.PersonaDAO;
import hr3.springboot.poc.model.Persona;

@Component
public class PocBuilder {
	@Autowired
	private PersonaDAO pdao;

	public Persona createPerson(String nombre, String apellido, Date birthday) {
		PersonaImpl vo = new PersonaImpl();
		vo.setApellido(apellido);
		vo.setNombre(nombre);
		vo.setFechaNacimiento(new java.sql.Date(birthday.getTime()));

		pdao.insert(vo);

		return vo;
	}

}
