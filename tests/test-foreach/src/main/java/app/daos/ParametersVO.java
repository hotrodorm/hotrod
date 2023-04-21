package app.daos;

import app.daos.primitives.AbstractParametersVO;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import app.daos.primitives.ParametersDAO;
import org.springframework.beans.factory.annotation.Autowired;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ParametersVO extends AbstractParametersVO {

  private static final long serialVersionUID = 1L;

  @Autowired
  private ParametersDAO parametersDAO;

  // Add custom code below.

}
