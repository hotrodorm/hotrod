package app.daos;

import app.daos.primitives.AbstractCharsVO;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import app.daos.primitives.CharsDAO;
import org.springframework.beans.factory.annotation.Autowired;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CharsVO extends AbstractCharsVO {

  private static final long serialVersionUID = 1L;

  @Autowired
  private CharsDAO charsDAO;

  // Add custom code below.

}
