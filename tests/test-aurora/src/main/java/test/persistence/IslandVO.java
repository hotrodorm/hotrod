package test.persistence;

import test.persistence.primitives.Island;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class IslandVO extends Island {

  private static final long serialVersionUID = 1L;

  // Add custom code below.

}
