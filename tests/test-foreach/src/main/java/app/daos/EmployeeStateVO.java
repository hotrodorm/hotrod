package app.daos;

import app.daos.primitives.AbstractEmployeeStateVO;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import app.daos.primitives.EmployeeStateDAO;
import org.springframework.beans.factory.annotation.Autowired;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class EmployeeStateVO extends AbstractEmployeeStateVO {

  private static final long serialVersionUID = 1L;

  @Autowired
  private EmployeeStateDAO employeeStateDAO;

  // Add custom code below.

}
