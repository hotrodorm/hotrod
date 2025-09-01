package hr3.springboot.poc.model;

import java.util.List;

public interface Pedido extends PedidoBean {
	List<RequestItem> obtainRequestItemList();

	Iterable<RequestItem> obtainRequestItemCursor();

}