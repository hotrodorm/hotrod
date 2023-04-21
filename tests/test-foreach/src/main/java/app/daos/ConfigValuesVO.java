package app.daos;

import app.daos.primitives.AbstractConfigValuesVO;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import app.daos.primitives.ConfigValuesDAO;
import org.springframework.beans.factory.annotation.Autowired;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ConfigValuesVO extends AbstractConfigValuesVO {

  private static final long serialVersionUID = 1L;

  @Autowired
  private ConfigValuesDAO configValuesDAO;

  // Add custom code below.

}
