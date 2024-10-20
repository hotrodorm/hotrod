package app.daos;

import app.daos.primitives.Customer;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import app.daos.primitives.CustomerDAO;
import org.springframework.beans.factory.annotation.Autowired;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CustomerVO extends Customer {

  private static final long serialVersionUID = 1L;

  @Autowired
  private CustomerDAO customerDAO;

  // Add custom code below.

}
