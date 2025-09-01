package hr3.springboot.poc.hotrod;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import hr3.springboot.poc.hotrod.primitives.ArticuloVO;
import hr3.springboot.poc.model.Articulo;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ArticuloImpl extends ArticuloVO implements Articulo {

	private static final long serialVersionUID = 1L;

	// Add custom code below.

}
