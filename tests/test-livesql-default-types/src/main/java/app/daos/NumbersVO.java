package app.daos;

import org.springframework.stereotype.Component;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import app.daos.primitives.AbstractNumbersVO;
import org.springframework.beans.factory.annotation.Autowired;
import app.daos.primitives.NumbersDAO;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class NumbersVO extends AbstractNumbersVO {

  private static final long serialVersionUID = 1L;

  @SuppressWarnings("unused")
  @Autowired
  private NumbersDAO numbersDAO;

  // Add custom code below.

}
