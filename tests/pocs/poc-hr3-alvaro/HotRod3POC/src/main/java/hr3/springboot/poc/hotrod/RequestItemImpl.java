package hr3.springboot.poc.hotrod;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import hr3.springboot.poc.hotrod.primitives.RequestItemDAO;
import hr3.springboot.poc.hotrod.primitives.RequestItemVO;
import hr3.springboot.poc.model.Articulo;
import hr3.springboot.poc.model.RequestItem;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class RequestItemImpl extends RequestItemVO implements RequestItem {
	@Autowired
	RequestItemDAO requestItemDAO;

	private static final long serialVersionUID = 1L;

	// Add custom code below.

	@Override
	public Articulo getArticulo() {
		// TODO Auto-generated method stub
		return requestItemDAO.selectParentArticuloOf(this).fromIdArticulo().toArtId();
	}

}
