package app5.persistence;

import app5.persistence.primitives.AbstractProductVO;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ProductVO extends AbstractProductVO {

  private static final long serialVersionUID = 1L;

  // Add custom code below.

}
