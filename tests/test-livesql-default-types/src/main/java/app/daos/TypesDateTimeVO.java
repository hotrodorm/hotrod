package app.daos;

import app.daos.primitives.AbstractTypesDateTimeVO;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import app.daos.primitives.TypesDateTimeDAO;
import org.springframework.beans.factory.annotation.Autowired;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TypesDateTimeVO extends AbstractTypesDateTimeVO {

  private static final long serialVersionUID = 1L;

  @Autowired
  private TypesDateTimeDAO typesDateTimeDAO;

  // Add custom code below.

}
