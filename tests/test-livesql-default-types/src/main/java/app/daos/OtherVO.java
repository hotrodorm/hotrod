package app.daos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import app.daos.primitives.AbstractOtherVO;
import app.daos.primitives.OtherDAO;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class OtherVO extends AbstractOtherVO {

  private static final long serialVersionUID = 1L;

  @Autowired
  private OtherDAO otherDAO;

  // Add custom code below.

}
