package app.daos;

import app.daos.primitives.AbstractTestDefault1VO;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import app.daos.primitives.TestDefault1DAO;
import org.springframework.beans.factory.annotation.Autowired;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TestDefault1VO extends AbstractTestDefault1VO {

  private static final long serialVersionUID = 1L;

  @Autowired
  private TestDefault1DAO testDefault1DAO;

  // Add custom code below.

}
