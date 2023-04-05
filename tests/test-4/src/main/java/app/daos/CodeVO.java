package app.daos;

import app.daos.primitives.AbstractCodeVO;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import app.daos.primitives.CodeDAO;
import org.springframework.beans.factory.annotation.Autowired;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CodeVO extends AbstractCodeVO {

  private static final long serialVersionUID = 1L;

  @Autowired
  private CodeDAO codeDAO;

  // Add custom code below.

}
