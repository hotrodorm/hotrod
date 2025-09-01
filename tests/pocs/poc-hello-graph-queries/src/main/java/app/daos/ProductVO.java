package app.daos;

import app.daos.primitives.Product;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import app.daos.primitives.ProductDAO;
import org.springframework.beans.factory.annotation.Autowired;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ProductVO extends Product {

  private static final long serialVersionUID = 1L;

  @Autowired
  private ProductDAO productDAO;

  // Add custom code below.

}
