package app.daos;

import app.daos.primitives.AbstractTestIdentity1VO;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import app.daos.primitives.TestIdentity1DAO;
import org.springframework.beans.factory.annotation.Autowired;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TestIdentity1VO extends AbstractTestIdentity1VO {

  private static final long serialVersionUID = 1L;

  @Autowired
  private TestIdentity1DAO testIdentity1DAO;

  // Add custom code below.

}
