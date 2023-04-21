package app.daos;

import app.daos.primitives.AbstractTestDefault2VO;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import app.daos.primitives.TestDefault2DAO;
import org.springframework.beans.factory.annotation.Autowired;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TestDefault2VO extends AbstractTestDefault2VO {

  private static final long serialVersionUID = 1L;

  @Autowired
  private TestDefault2DAO testDefault2DAO;

  // Add custom code below.

}
