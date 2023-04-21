package app.daos;

import app.daos.primitives.AbstractPrimeVO;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import app.daos.primitives.PrimeDAO;
import org.springframework.beans.factory.annotation.Autowired;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PrimeVO extends AbstractPrimeVO {

  private static final long serialVersionUID = 1L;

  @Autowired
  private PrimeDAO primeDAO;

  // Add custom code below.

}
