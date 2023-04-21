package app.daos;

import app.daos.primitives.AbstractVehicleTypeVO;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import app.daos.primitives.VehicleTypeDAO;
import org.springframework.beans.factory.annotation.Autowired;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class VehicleTypeVO extends AbstractVehicleTypeVO {

  private static final long serialVersionUID = 1L;

  @Autowired
  private VehicleTypeDAO vehicleTypeDAO;

  // Add custom code below.

}
