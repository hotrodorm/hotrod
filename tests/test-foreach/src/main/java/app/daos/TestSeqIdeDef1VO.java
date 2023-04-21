package app.daos;

import app.daos.primitives.AbstractTestSeqIdeDef1VO;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import app.daos.primitives.TestSeqIdeDef1DAO;
import org.springframework.beans.factory.annotation.Autowired;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TestSeqIdeDef1VO extends AbstractTestSeqIdeDef1VO {

  private static final long serialVersionUID = 1L;

  @Autowired
  private TestSeqIdeDef1DAO testSeqIdeDef1DAO;

  // Add custom code below.

}
