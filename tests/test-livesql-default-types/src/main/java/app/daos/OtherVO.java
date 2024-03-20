package app.daos;

import app.daos.primitives.AbstractOtherVO;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import app.daos.primitives.OtherDAO;
import org.springframework.beans.factory.annotation.Autowired;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class OtherVO extends AbstractOtherVO {

  private static final long serialVersionUID = 1L;

  @Autowired
  private OtherDAO otherDAO;

  // Add custom code below.

}
