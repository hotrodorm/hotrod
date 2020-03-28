package com.company.daos;

import com.company.daos.primitives.DomainVehiclePrototype;
import com.company.daos.primitives.VehicleType;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class VehicleVO extends DomainVehiclePrototype {

  private static final long serialVersionUID = 1L;

  // Add custom code below.

}
