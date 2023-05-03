package app.daos;

import app.daos.primitives.AbstractOthersVO;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import app.daos.primitives.OthersDAO;
import org.springframework.beans.factory.annotation.Autowired;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class OthersVO extends AbstractOthersVO {

  private static final long serialVersionUID = 1L;

  @Autowired
  private OthersDAO othersDAO;

  // Add custom code below.

}
