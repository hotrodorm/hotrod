package app.daos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import app.daos.primitives.AbstractNumbersVO;
import app.daos.primitives.NumbersDAO;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class NumbersVO extends AbstractNumbersVO {

  private static final long serialVersionUID = 1L;

  @Autowired
  private NumbersDAO numbersDAO;

  // Add custom code below.

  public NumbersDAO getNumbersDAO() {
    return numbersDAO;
  }

}
