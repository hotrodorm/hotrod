package app.daos;

import app.daos.primitives.AbstractQuadrantVO;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import app.daos.primitives.QuadrantDAO;
import org.springframework.beans.factory.annotation.Autowired;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class QuadrantVO extends AbstractQuadrantVO {

  private static final long serialVersionUID = 1L;

  @Autowired
  private QuadrantDAO quadrantDAO;

  // Add custom code below.

}
