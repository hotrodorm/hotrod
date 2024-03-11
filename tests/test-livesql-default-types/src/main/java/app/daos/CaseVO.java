package app.daos;

import app.daos.primitives.AbstractCaseVO;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import app.daos.primitives.CaseDAO;
import org.springframework.beans.factory.annotation.Autowired;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CaseVO extends AbstractCaseVO {

  private static final long serialVersionUID = 1L;

  @Autowired
  private CaseDAO caseDAO;

  // Add custom code below.

}
