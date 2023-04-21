package app.daos;

import app.daos.primitives.AbstractApplicationConfigVO;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import app.daos.primitives.ApplicationConfigDAO;
import org.springframework.beans.factory.annotation.Autowired;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ApplicationConfigVO extends AbstractApplicationConfigVO {

  private static final long serialVersionUID = 1L;

  @Autowired
  private ApplicationConfigDAO applicationConfigDAO;

  // Add custom code below.

}
