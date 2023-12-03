package app.daos;

import app.daos.primitives.Branch;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import app.daos.primitives.BranchDAO;
import org.springframework.beans.factory.annotation.Autowired;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class BranchVO extends Branch {

  private static final long serialVersionUID = 1L;

  @Autowired
  private BranchDAO branchDAO;

  // Add custom code below.

}
