package app.persistence.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import app.persistence.dao.EmployeeBranchDAO;
import app.persistence.layout.EmployeeBranchLayout;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class EmployeeBranch extends EmployeeBranchLayout {

  private static final long serialVersionUID = 1L;

  @SuppressWarnings("unused")
  @Autowired
  private EmployeeBranchDAO employeeBranchDAO;

  // Add custom code below.

}
