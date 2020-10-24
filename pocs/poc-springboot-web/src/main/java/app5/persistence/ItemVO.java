package app5.persistence;

import app5.persistence.primitives.AbstractItemVO;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ItemVO extends AbstractItemVO {

  private static final long serialVersionUID = 1L;

  // Add custom code below.

}
