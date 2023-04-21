package app.daos;

import app.daos.primitives.AbstractStateBranchVO;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import app.daos.primitives.StateBranchDAO;
import org.springframework.beans.factory.annotation.Autowired;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class StateBranchVO extends AbstractStateBranchVO {

  private static final long serialVersionUID = 1L;

  @Autowired
  private StateBranchDAO stateBranchDAO;

  // Add custom code below.

}
