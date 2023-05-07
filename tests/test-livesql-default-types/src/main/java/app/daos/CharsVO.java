package app.daos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import app.daos.primitives.AbstractCharsVO;
import app.daos.primitives.CharsDAO;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CharsVO extends AbstractCharsVO {

  private static final long serialVersionUID = 1L;

  @Autowired
  private CharsDAO charsDAO;

  // Add custom code below.

}
