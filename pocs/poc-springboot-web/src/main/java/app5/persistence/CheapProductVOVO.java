package app5.persistence;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import app5.persistence.primitives.AbstractCheapProductVO;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CheapProductVOVO extends AbstractCheapProductVO {

  private static final long serialVersionUID = 1L;

  // Add custom code below.

}
