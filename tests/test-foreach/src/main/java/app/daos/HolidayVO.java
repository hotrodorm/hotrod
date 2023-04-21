package app.daos;

import app.daos.primitives.AbstractHolidayVO;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import app.daos.primitives.HolidayDAO;
import org.springframework.beans.factory.annotation.Autowired;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class HolidayVO extends AbstractHolidayVO {

  private static final long serialVersionUID = 1L;

  @Autowired
  private HolidayDAO holidayDAO;

  // Add custom code below.

}
