package hr3.springboot.poc.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import hr3.springboot.poc.hotrod.PedidoImpl;
import hr3.springboot.poc.hotrod.PersonaImpl;
import hr3.springboot.poc.hotrod.RequestItemImpl;
import hr3.springboot.poc.hotrod.primitives.PedidoDAO;
import hr3.springboot.poc.hotrod.primitives.PersonaDAO;
import hr3.springboot.poc.hotrod.primitives.RequestItemDAO;
import hr3.springboot.poc.model.Pedido;
import hr3.springboot.poc.model.Persona;

@Transactional
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

	@Autowired
	private ApplicationContext ctx;

	@Autowired
	private PedidoDAO rdao;
	@Autowired
	private RequestItemDAO ridao;

	public Pedido makeNewRequest(Persona p, long[][] is) {
		PedidoImpl rvo = ctx.getBean(PedidoImpl.class);
		
		rvo.setIdPersona(p.getId());
		rvo.setDescripcion("sin descripción");
		rvo.setTimestamp(new Timestamp(System.currentTimeMillis()));
		rdao.insert(rvo);

		RequestItemImpl ri = new RequestItemImpl();
		for (int i = 0; i < 4; i++) {
			ri = new RequestItemImpl();
			System.out.println(is[i][0]);
			System.out.println(is[i][1]);
			ri.setIdArticulo(is[i][0]);
			ri.setCantidad(new BigDecimal(is[i][1]));
			ri.setIdPedido(rvo.getId());

			ridao.insert(ri);
		}

		return rvo;
	}

}
