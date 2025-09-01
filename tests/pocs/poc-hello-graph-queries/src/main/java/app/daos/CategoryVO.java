package app.daos;

import app.daos.primitives.Category;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import app.daos.primitives.CategoryDAO;
import org.springframework.beans.factory.annotation.Autowired;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CategoryVO extends Category {

  private static final long serialVersionUID = 1L;

  @Autowired
  private CategoryDAO categoryDAO;

  // Add custom code below.

}
