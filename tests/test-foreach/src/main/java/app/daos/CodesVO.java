package app.daos;

import app.daos.primitives.AbstractCodesVO;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import app.daos.primitives.CodesDAO;
import org.springframework.beans.factory.annotation.Autowired;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CodesVO extends AbstractCodesVO {

  private static final long serialVersionUID = 1L;

  @Autowired
  private CodesDAO codesDAO;

  // Add custom code below.

}
