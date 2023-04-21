package app.daos;

import app.daos.primitives.AbstractTestMixed1VO;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import app.daos.primitives.TestMixed1DAO;
import org.springframework.beans.factory.annotation.Autowired;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TestMixed1VO extends AbstractTestMixed1VO {

  private static final long serialVersionUID = 1L;

  @Autowired
  private TestMixed1DAO testMixed1DAO;

  // Add custom code below.

}
