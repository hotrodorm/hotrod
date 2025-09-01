package app5.persistence.accounts;

import app5.persistence.accounts.primitives.AbstractAccountPersonFlatVO;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AccountPersonFlatVO extends AbstractAccountPersonFlatVO {

  private static final long serialVersionUID = 1L;

  // Add custom code below.

}
