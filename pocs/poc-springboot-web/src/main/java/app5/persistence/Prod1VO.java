package app5.persistence;

import app5.persistence.primitives.AbstractProd1VO;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Prod1VO extends AbstractProd1VO {

  private static final long serialVersionUID = 1L;

  // Add custom code below.

}
