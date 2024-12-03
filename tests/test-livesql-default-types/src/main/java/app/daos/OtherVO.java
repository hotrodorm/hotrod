package app.daos;

import org.springframework.stereotype.Component;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import app.daos.primitives.AbstractOtherVO;
import org.springframework.beans.factory.annotation.Autowired;
import app.daos.primitives.OtherDAO;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class OtherVO extends AbstractOtherVO {

  private static final long serialVersionUID = 1L;

  @SuppressWarnings("unused")
  @Autowired
  private OtherDAO otherDAO;

  // Add custom code below.

}
