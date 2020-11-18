package hr3.springboot.poc.hotrod;

import hr3.springboot.poc.hotrod.primitives.PedidoVO;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PedidoImpl extends PedidoVO {

  private static final long serialVersionUID = 1L;

  // Add custom code below.

}
