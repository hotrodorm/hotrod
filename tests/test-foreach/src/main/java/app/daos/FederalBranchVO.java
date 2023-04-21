package app.daos;

import app.daos.primitives.AbstractFederalBranchVO;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import app.daos.primitives.FederalBranchDAO;
import org.springframework.beans.factory.annotation.Autowired;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class FederalBranchVO extends AbstractFederalBranchVO {

  private static final long serialVersionUID = 1L;

  @Autowired
  private FederalBranchDAO federalBranchDAO;

  // Add custom code below.

}
