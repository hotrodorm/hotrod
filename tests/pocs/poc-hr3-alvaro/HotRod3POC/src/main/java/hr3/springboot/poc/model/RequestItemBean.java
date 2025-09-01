package hr3.springboot.poc.model;

public interface RequestItemBean {
	java.lang.Long getIdPedido();

	void setIdPedido(final java.lang.Long idPedido);

	java.lang.Long getIdArticulo();

	void setIdArticulo(final java.lang.Long idArticulo);

	java.math.BigDecimal getCantidad();

	void setCantidad(final java.math.BigDecimal cantidad);

}