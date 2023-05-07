package app.daos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import app.daos.primitives.AbstractDatesVO;
import app.daos.primitives.DatesDAO;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DatesVO extends AbstractDatesVO {

  private static final long serialVersionUID = 1L;

  @Autowired
  private DatesDAO datesDAO;

  // Add custom code below.

}
