package app.daos;

import app.daos.primitives.AbstractVehicleVO;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import app.daos.primitives.VehicleDAO;
import org.springframework.beans.factory.annotation.Autowired;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class VehicleVO extends AbstractVehicleVO {

  private static final long serialVersionUID = 1L;

  @Autowired
  private VehicleDAO vehicleDAO;

  // Add custom code below.

}
