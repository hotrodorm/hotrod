package app.daos;

import app.daos.primitives.AbstractBinariesVO;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import app.daos.primitives.BinariesDAO;
import org.springframework.beans.factory.annotation.Autowired;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class BinariesVO extends AbstractBinariesVO {

  private static final long serialVersionUID = 1L;

  @Autowired
  private BinariesDAO binariesDAO;

  // Add custom code below.

}
