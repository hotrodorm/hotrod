package app.daos;

import app.daos.primitives.AbstractTypesCharVO;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import app.daos.primitives.TypesCharDAO;
import org.springframework.beans.factory.annotation.Autowired;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TypesCharVO extends AbstractTypesCharVO {

  private static final long serialVersionUID = 1L;

  @Autowired
  private TypesCharDAO typesCharDAO;

  // Add custom code below.

}
