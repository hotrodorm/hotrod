package app.daos;

import app.daos.primitives.Employee;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import app.daos.primitives.EmployeeDAO;
import org.springframework.beans.factory.annotation.Autowired;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class EmployeeVO extends Employee {

  private static final long serialVersionUID = 1L;

  @Autowired
  private EmployeeDAO employeeDAO;

  // Add custom code below.

}
