package app.daos;

import app.daos.primitives.AbstractCar_part_priceVO;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import app.daos.primitives.Car_part_priceDAO;
import org.springframework.beans.factory.annotation.Autowired;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Car_part_priceVO extends AbstractCar_part_priceVO {

  private static final long serialVersionUID = 1L;

  @Autowired
  private Car_part_priceDAO car_part_priceDAO;

  // Add custom code below.

}
