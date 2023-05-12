package app.daos;

import app.daos.primitives.AbstractDatesVO;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import app.daos.primitives.DatesDAO;
import org.springframework.beans.factory.annotation.Autowired;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DatesVO extends AbstractDatesVO {

  private static final long serialVersionUID = 1L;

  @Autowired
  private DatesDAO datesDAO;

  // Add custom code below.

}
