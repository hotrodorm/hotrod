package app.daos;

import org.springframework.stereotype.Component;
import org.springframework.context.annotation.Scope;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import app.daos.primitives.AbstractBinariesVO;
import org.springframework.beans.factory.annotation.Autowired;
import app.daos.primitives.BinariesDAO;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class BinariesVO extends AbstractBinariesVO {

  private static final long serialVersionUID = 1L;

  @SuppressWarnings("unused")
  @Autowired
  private BinariesDAO binariesDAO;

  // Add custom code below.

}
