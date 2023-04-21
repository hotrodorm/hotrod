package app.daos;

import app.daos.primitives.AbstractTestSequence2VO;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import app.daos.primitives.TestSequence2DAO;
import org.springframework.beans.factory.annotation.Autowired;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TestSequence2VO extends AbstractTestSequence2VO {

  private static final long serialVersionUID = 1L;

  @Autowired
  private TestSequence2DAO testSequence2DAO;

  // Add custom code below.

}
