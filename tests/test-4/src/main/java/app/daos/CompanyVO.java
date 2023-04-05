package app.daos;

import app.daos.primitives.AbstractCompanyVO;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import app.daos.primitives.CompanyDAO;
import org.springframework.beans.factory.annotation.Autowired;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CompanyVO extends AbstractCompanyVO {

  private static final long serialVersionUID = 1L;

  @Autowired
  private CompanyDAO companyDAO;

  // Add custom code below.

}
