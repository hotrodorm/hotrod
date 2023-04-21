package app.daos;

import app.daos.primitives.AbstractHouseVO;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import app.daos.primitives.HouseDAO;
import org.springframework.beans.factory.annotation.Autowired;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class HouseVO extends AbstractHouseVO {

  private static final long serialVersionUID = 1L;

  @Autowired
  private HouseDAO houseDAO;

  // Add custom code below.

}
