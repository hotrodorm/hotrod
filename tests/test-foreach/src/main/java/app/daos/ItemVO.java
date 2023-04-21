package app.daos;

import app.daos.primitives.AbstractItemVO;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import app.daos.primitives.ItemDAO;
import org.springframework.beans.factory.annotation.Autowired;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ItemVO extends AbstractItemVO {

  private static final long serialVersionUID = 1L;

  @Autowired
  private ItemDAO itemDAO;

  // Add custom code below.

}
