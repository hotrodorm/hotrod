package app.daos;

import app.daos.primitives.AbstractTypesNumericVO;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import app.daos.primitives.TypesNumericDAO;
import org.springframework.beans.factory.annotation.Autowired;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TypesNumericVO extends AbstractTypesNumericVO {

  private static final long serialVersionUID = 1L;

  @Autowired
  private TypesNumericDAO typesNumericDAO;

  // Add custom code below.

}