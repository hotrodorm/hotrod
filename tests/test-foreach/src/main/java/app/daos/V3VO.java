package app.daos;

import app.daos.primitives.V3;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import app.daos.primitives.V3DAO;
import org.springframework.beans.factory.annotation.Autowired;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class V3VO extends V3 {

  private static final long serialVersionUID = 1L;

  @Autowired
  private V3DAO v3DAO;

  // Add custom code below.

}
