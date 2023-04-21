package app.daos;

import app.daos.primitives.AbstractTxBranchVO;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import app.daos.primitives.TxBranchDAO;
import org.springframework.beans.factory.annotation.Autowired;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TxBranchVO extends AbstractTxBranchVO {

  private static final long serialVersionUID = 1L;

  @Autowired
  private TxBranchDAO txBranchDAO;

  // Add custom code below.

}
