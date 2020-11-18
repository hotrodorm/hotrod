package hr3.springboot.poc.model;

import java.util.List;

public interface Pedido {
	List<RequestItem> obtainRequestItemList();

	Iterable<RequestItem> obtainRequestItemCursor();

}