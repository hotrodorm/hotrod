package com.myapp.daos;

import com.myapp.daos.primitives.EmployeeVO;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import com.myapp.daos.primitives.EmployeeDAO;
import org.springframework.beans.factory.annotation.Autowired;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class EmployeeMODEL extends EmployeeVO implements java.io.Serializable {

  private static final long serialVersionUID = 1L;

  @Autowired
  private EmployeeDAO employeeDAO;

  // Add custom code below.

}
