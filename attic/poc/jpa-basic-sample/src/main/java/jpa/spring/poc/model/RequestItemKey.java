package jpa.spring.poc.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class RequestItemKey implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "REQ_ID", nullable = false)
	private long idRequest;

	@Column(name = "ART_ID", nullable = false)
	private long idArticulo;

	public RequestItemKey(long idRequest, long idArticulo) {
		super();
		this.idRequest = idRequest;
		this.idArticulo = idArticulo;
	}

	// empty constructor MUST exist
	public RequestItemKey() {
		super();
	}

	public long getIdRequest() {
		return idRequest;
	}

	public void setIdRequest(long idRequest) {
		this.idRequest = idRequest;
	}

	public long getIdArticulo() {
		return idArticulo;
	}

	public void setIdArticulo(long idArticulo) {
		this.idArticulo = idArticulo;
	}

	@Override
	public boolean equals(Object obj) {
		// self check
		if (this == obj)
			return true;
		// null check
		if (obj == null)
			return false;
		// type check and cast
		if (getClass() != obj.getClass())
			return false;
		RequestItemKey rik = (RequestItemKey) obj;
		// field comparison
		return Objects.equals(this.idArticulo, rik.idArticulo) && Objects.equals(this.idRequest, rik.idRequest);
	}

}
