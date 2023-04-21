package app.daos;

import app.daos.primitives.AbstractEventVO;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import app.daos.primitives.EventDAO;
import org.springframework.beans.factory.annotation.Autowired;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class EventVO extends AbstractEventVO {

  private static final long serialVersionUID = 1L;

  @Autowired
  private EventDAO eventDAO;

  // Add custom code below.

}
