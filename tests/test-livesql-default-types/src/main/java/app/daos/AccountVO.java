package app.daos;

import org.springframework.stereotype.Component;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import app.daos.primitives.AbstractAccountVO;
import org.springframework.beans.factory.annotation.Autowired;
import app.daos.primitives.AccountDAO;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AccountVO extends AbstractAccountVO {

  private static final long serialVersionUID = 1L;

  @SuppressWarnings("unused")
  @Autowired
  private AccountDAO accountDAO;

  // Add custom code below.

}
