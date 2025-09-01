package com.company.daos;

import com.company.daos.primitives.DomainCar_part_pricePrototype;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Car_part_priceVO extends DomainCar_part_pricePrototype {

  private static final long serialVersionUID = 1L;

  // Add custom code below.

}
