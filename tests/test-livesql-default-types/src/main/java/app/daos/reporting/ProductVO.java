package app.daos.reporting;

import app.daos.reporting.primitives.AbstractProductVO;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import app.daos.reporting.primitives.ProductDAO;
import org.springframework.beans.factory.annotation.Autowired;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ProductVO extends AbstractProductVO {

  private static final long serialVersionUID = 1L;

  @Autowired
  private ProductDAO productDAO;

  // Add custom code below.

}
