package app5.persistence;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import app5.cursors.ProductInterface;
import app5.persistence.primitives.AbstractProductVO;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ProductVO extends AbstractProductVO implements ProductInterface {

  private static final long serialVersionUID = 1L;

  // Add custom code below.

}
