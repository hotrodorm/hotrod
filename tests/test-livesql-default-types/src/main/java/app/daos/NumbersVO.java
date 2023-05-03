package app.daos;

import app.daos.primitives.AbstractNumbersVO;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import app.daos.primitives.NumbersDAO;
import org.springframework.beans.factory.annotation.Autowired;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class NumbersVO extends AbstractNumbersVO {

  private static final long serialVersionUID = 1L;

  @Autowired
  private NumbersDAO numbersDAO;

  // Add custom code below.

}
