package app.daos;

import org.springframework.stereotype.Component;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import app.daos.primitives.AbstractCharsVO;
import org.springframework.beans.factory.annotation.Autowired;
import app.daos.primitives.CharsDAO;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CharsVO extends AbstractCharsVO {

  private static final long serialVersionUID = 1L;

  @SuppressWarnings("unused")
  @Autowired
  private CharsDAO charsDAO;

  // Add custom code below.

}
