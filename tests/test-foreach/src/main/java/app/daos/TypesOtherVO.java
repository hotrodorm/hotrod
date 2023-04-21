package app.daos;

import app.daos.primitives.AbstractTypesOtherVO;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import app.daos.primitives.TypesOtherDAO;
import org.springframework.beans.factory.annotation.Autowired;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TypesOtherVO extends AbstractTypesOtherVO {

  private static final long serialVersionUID = 1L;

  @Autowired
  private TypesOtherDAO typesOtherDAO;

  // Add custom code below.

}
