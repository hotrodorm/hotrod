package test.persistence;

import test.persistence.primitives.Car_part_price;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Car_part_priceVO extends Car_part_price {

  private static final long serialVersionUID = 1L;

  // Add custom code below.

}
