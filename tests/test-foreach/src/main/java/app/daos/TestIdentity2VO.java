package app.daos;

import app.daos.primitives.AbstractTestIdentity2VO;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import app.daos.primitives.TestIdentity2DAO;
import org.springframework.beans.factory.annotation.Autowired;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TestIdentity2VO extends AbstractTestIdentity2VO {

  private static final long serialVersionUID = 1L;

  @Autowired
  private TestIdentity2DAO testIdentity2DAO;

  // Add custom code below.

}
