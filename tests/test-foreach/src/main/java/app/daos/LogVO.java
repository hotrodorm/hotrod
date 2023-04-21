package app.daos;

import app.daos.primitives.AbstractLogVO;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import app.daos.primitives.LogDAO;
import org.springframework.beans.factory.annotation.Autowired;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class LogVO extends AbstractLogVO {

  private static final long serialVersionUID = 1L;

  @Autowired
  private LogDAO logDAO;

  // Add custom code below.

}
