package app.daos;

import app.daos.primitives.AbstractCursorExample1VO;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import app.daos.primitives.CursorExample1DAO;
import org.springframework.beans.factory.annotation.Autowired;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CursorExample1VO extends AbstractCursorExample1VO {

  private static final long serialVersionUID = 1L;

  @Autowired
  private CursorExample1DAO cursorExample1DAO;

  // Add custom code below.

}
