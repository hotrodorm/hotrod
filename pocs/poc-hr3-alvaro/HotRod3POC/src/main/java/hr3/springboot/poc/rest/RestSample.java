package hr3.springboot.poc.rest;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import hr3.springboot.poc.impl.PocBuilder;
import hr3.springboot.poc.impl.PocFinder;
import hr3.springboot.poc.model.Persona;

// invoca los servicios en orden 1), 2) y 3)   ...as usual ;)
@RestController
@RequestMapping(value = "/hr3/poc")
public class RestSample {

	@RequestMapping(value = "nada", method = RequestMethod.GET)
	public String doNothing() {
		return "The emptiness";
	}

	@Autowired
	private PocBuilder b;
	@Autowired
	private PocFinder f;

	/*
	 * @Transactional annotation must be specified in order to avoid early close of
	 * internal cursor, returned by findAllPersona method. Cursor is automatically
	 * closed by Spring on transaction termination.
	 */
	@Transactional
	@RequestMapping(value = "algo", method = RequestMethod.GET)
	public String doSomething() throws ParseException {
		Persona maria = b.createPerson("María", "Undurraga", new SimpleDateFormat("dd-MM-yyyy").parse("21-10-2004"));
		Persona alejandro = b.createPerson("Alejandro", "Martínez",
				new SimpleDateFormat("dd-MM-yyyy").parse("18-06-1999"));

		for (Persona p : f.findAllPersona())
			System.out.println(p);

		return "The matter";
	}

}
