package app.daos;

import app.daos.primitives.PlayerVO;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import app.daos.primitives.PlayerDAO;
import org.springframework.beans.factory.annotation.Autowired;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PlayerMODEL extends PlayerVO {

  private static final long serialVersionUID = 1L;

  @Autowired
  private PlayerDAO playerDAO;

  // Add custom code below.

}
