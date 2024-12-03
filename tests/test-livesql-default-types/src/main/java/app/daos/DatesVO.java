package app.daos;

import org.springframework.stereotype.Component;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import app.daos.primitives.AbstractDatesVO;
import org.springframework.beans.factory.annotation.Autowired;
import app.daos.primitives.DatesDAO;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DatesVO extends AbstractDatesVO {

  private static final long serialVersionUID = 1L;

  @SuppressWarnings("unused")
  @Autowired
  private DatesDAO datesDAO;

  // Add custom code below.

}
