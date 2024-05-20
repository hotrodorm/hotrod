package app.daos;

import app.daos.primitives.AbstractAccountVO;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import app.daos.primitives.AccountDAO;
import org.springframework.beans.factory.annotation.Autowired;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AccountVO extends AbstractAccountVO {

  private static final long serialVersionUID = 1L;

  @Autowired
  private AccountDAO accountDAO;

  // Add custom code below.

}
