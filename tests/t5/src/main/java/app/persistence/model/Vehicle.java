package app.persistence.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import app.persistence.dao.VehicleDAO;
import app.persistence.layout.VehicleLayout;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Vehicle extends VehicleLayout {

  private static final long serialVersionUID = 1L;

  @SuppressWarnings("unused")
  @Autowired
  private VehicleDAO vehicleDAO;

  // Add custom code below.

}
