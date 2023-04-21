package app.daos;

import app.daos.primitives.AbstractIslandVO;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import app.daos.primitives.IslandDAO;
import org.springframework.beans.factory.annotation.Autowired;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class IslandVO extends AbstractIslandVO {

  private static final long serialVersionUID = 1L;

  @Autowired
  private IslandDAO islandDAO;

  // Add custom code below.

}
