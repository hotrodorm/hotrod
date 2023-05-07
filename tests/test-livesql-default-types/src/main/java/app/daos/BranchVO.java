package app.daos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import app.daos.primitives.AbstractBranchVO;
import app.daos.primitives.BranchDAO;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class BranchVO extends AbstractBranchVO {

  private static final long serialVersionUID = 1L;

  @Autowired
  private BranchDAO branchDAO;

  // Add custom code below.

}
