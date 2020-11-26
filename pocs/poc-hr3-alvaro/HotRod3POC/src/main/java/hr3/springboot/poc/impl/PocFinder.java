package hr3.springboot.poc.impl;

import org.hotrod.runtime.cursors.Cursor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import hr3.springboot.poc.etc.CursorGenericAdapter;
import hr3.springboot.poc.hotrod.ExtendedPedidoImpl;
import hr3.springboot.poc.hotrod.PersonaImpl;
import hr3.springboot.poc.hotrod.primitives.PedidoCustomDAO;
import hr3.springboot.poc.hotrod.primitives.PersonaDAO;
import hr3.springboot.poc.model.Persona;

@Transactional
@Component
public class PocFinder {
	@Autowired
	private PersonaDAO pdao;

	public Cursor<Persona> findAllPersona() {
//		PersonaTable ptable = PersonaDAO.newTable();
//		Cursor<PersonaImpl> c = pdao.selectByCriteria(ptable, ptable.id.eq(100)).executeCursor();

		Cursor<PersonaImpl> c = pdao.selectByExampleCursor(new PersonaImpl());

		Cursor<Persona> cc = new CursorGenericAdapter<Persona, PersonaImpl>(c);

		return cc;
	}

	@Autowired
	private PedidoCustomDAO pedidoCustomDAO;

	public Cursor<ExtendedPedidoImpl> findExtendedPedido(long pedidoId) {
		return pedidoCustomDAO.selectExtendedRequest(pedidoId);
	}
}
