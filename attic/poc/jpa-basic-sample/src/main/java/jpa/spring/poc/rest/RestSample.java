package jpa.spring.poc.rest;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import jpa.spring.poc.model.Articulo;
import jpa.spring.poc.model.ArticuloRepository;
import jpa.spring.poc.model.Persona;
import jpa.spring.poc.model.PersonaRepository;
import jpa.spring.poc.model.Request;
import jpa.spring.poc.model.RequestItem;
import jpa.spring.poc.model.RequestItemKey;
import jpa.spring.poc.model.RequestItemRepository;
import jpa.spring.poc.model.RequestRepository;

// invoca los servicios en orden 1), 2) y 3)   ...as usual ;)
@RestController
@RequestMapping(value = "/jpa")
public class RestSample {

	@RequestMapping(value = "nada", method = RequestMethod.GET)
	public String doNothing() {
		return "The emptiness";
	}

	@Autowired
	private PersonaRepository personaRepository;
	@Autowired
	private ArticuloRepository articuloRepository;
	@Autowired
	private RequestRepository requestRepository;
	@Autowired
	private RequestItemRepository requestItemRepository;

	// 1) crea un pedido (request)
	// Ej: http://localhost:8080/jpa/request/100
	@RequestMapping(value = "request/{personId}", method = RequestMethod.PUT)
	public long newRequest(@PathVariable Long personId) {
		// find persona y art�culo
		Persona p = personaRepository.findById(personId).get();

//		Crea pedido (request)
		Request r = new Request();
		r.setDescripcion("una compra");
		r.setFechaCreacion(new Timestamp(System.currentTimeMillis()));
		r.setIdPersona(p.getId());

		requestRepository.save(r);

		return r.getId();
	}

	// 2) agrega art�culo a pedido
	// Ej: http://localhost:8080/jpa/request/1/4/333
	@RequestMapping(value = "request/{requestId}/{articuloId}/{quantity}", method = RequestMethod.PUT)
	public int newRequestItem(@PathVariable Long requestId, @PathVariable Long articuloId, @PathVariable int quantity) {
		// find persona y art�culo
		Articulo a = articuloRepository.findById(articuloId).get();

		// Crea pedido (request)
		Request r = requestRepository.findById(requestId).get();

		RequestItemKey rik = new RequestItemKey(requestId, articuloId);
		Optional<RequestItem> ori = requestItemRepository.findById(rik);
		RequestItem item;
		if (ori.isPresent()) {
			// Actualiza cantidad
			item = ori.get();
			item.setQuantity(item.getQuantity() + quantity);

		} else {
			// Agrega art�culo a pedido
			item = new RequestItem();
			item.setKey(new RequestItemKey(r.getId(), a.getId()));
			item.setQuantity(quantity);
		}
		requestItemRepository.save(item);

		return item.getQuantity();
	}

	// ad-hoc VO for proper JSON return
	private static class RequestItemRetVO {
		public long id;
		public int quantity;

		public RequestItemRetVO(long id, int quantity) {
			super();
			this.id = id;
			this.quantity = quantity;
		}

	}

	// 3) retorna art�culos de un pedido
	// Ej: http://localhost:8080/jpa/request/1/items
	@RequestMapping(value = "request/{requestId}/items", method = RequestMethod.GET)
	public List<RequestItemRetVO> requestItemList(@PathVariable Long requestId) {
		List<RequestItemRetVO> ret = new ArrayList<>();
		Optional<Request> or = requestRepository.findById(requestId);
		if (or.isPresent()) {
			Request r = or.get();
			List<RequestItem> lri = requestRepository.findAllItems(r.getId());

			if (lri != null) {
				ret = lri.stream().map(ri -> new RequestItemRetVO(ri.getKey().getIdArticulo(), ri.getQuantity()))
						.collect(Collectors.toList());
			}
		}

		return ret;
	}

}
