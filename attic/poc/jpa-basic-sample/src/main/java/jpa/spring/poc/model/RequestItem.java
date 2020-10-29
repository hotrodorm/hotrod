package jpa.spring.poc.model;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "TRX_REQUEST_ITEM")
public class RequestItem {
	@EmbeddedId
	private RequestItemKey key;

	@Column(name = "ITEM_QTY", nullable = false)
	private int quantity;

	public RequestItemKey getKey() {
		return key;
	}

	public void setKey(RequestItemKey key) {
		this.key = key;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

}
