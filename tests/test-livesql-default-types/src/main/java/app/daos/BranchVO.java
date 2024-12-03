package app.daos;

import org.springframework.stereotype.Component;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import app.daos.primitives.AbstractBranchVO;
import org.springframework.beans.factory.annotation.Autowired;
import app.daos.primitives.BranchDAO;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class BranchVO extends AbstractBranchVO {

  private static final long serialVersionUID = 1L;

  @SuppressWarnings("unused")
  @Autowired
  private BranchDAO branchDAO;

  // Add custom code below.

}
