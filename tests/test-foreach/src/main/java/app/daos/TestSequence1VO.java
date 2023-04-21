package app.daos;

import app.daos.primitives.AbstractTestSequence1VO;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import app.daos.primitives.TestSequence1DAO;
import org.springframework.beans.factory.annotation.Autowired;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TestSequence1VO extends AbstractTestSequence1VO {

  private static final long serialVersionUID = 1L;

  @Autowired
  private TestSequence1DAO testSequence1DAO;

  // Add custom code below.

}
