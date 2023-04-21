package app.daos;

import app.daos.primitives.AbstractTransactionVO;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import app.daos.primitives.TransactionDAO;
import org.springframework.beans.factory.annotation.Autowired;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TransactionVO extends AbstractTransactionVO {

  private static final long serialVersionUID = 1L;

  @Autowired
  private TransactionDAO transactionDAO;

  // Add custom code below.

}
