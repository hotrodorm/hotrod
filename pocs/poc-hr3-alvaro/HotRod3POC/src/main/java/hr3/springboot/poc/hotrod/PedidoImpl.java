package hr3.springboot.poc.hotrod;

import java.util.List;

import org.hotrod.runtime.cursors.Cursor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import hr3.springboot.poc.etc.CursorGenericAdapter;
import hr3.springboot.poc.hotrod.primitives.PedidoDAO;
import hr3.springboot.poc.hotrod.primitives.PedidoDAO.SelectChildrenRequestItemFromIdPhase;
import hr3.springboot.poc.hotrod.primitives.PedidoDAO.SelectChildrenRequestItemPhase;
import hr3.springboot.poc.hotrod.primitives.PedidoVO;
import hr3.springboot.poc.model.Pedido;
import hr3.springboot.poc.model.RequestItem;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PedidoImpl extends PedidoVO implements Pedido {

	private static final long serialVersionUID = 1L;

	// Add custom code below.
	@Autowired
	private PedidoDAO pedidoDao;

	@Override
	public List<RequestItem> obtainRequestItemList() {
		return null;
	}

	@Override
	public Iterable<RequestItem> obtainRequestItemCursor() {
		SelectChildrenRequestItemPhase ph = pedidoDao.selectChildrenRequestItemOf(this);
		SelectChildrenRequestItemFromIdPhase idph = ph.fromId();
		Cursor<RequestItemImpl> c = pedidoDao.selectChildrenRequestItemOf(this).fromId().cursorToIdPedido();
		Cursor<RequestItem> cc = new CursorGenericAdapter<RequestItem, RequestItemImpl>(c);

		return cc;
	}

}
