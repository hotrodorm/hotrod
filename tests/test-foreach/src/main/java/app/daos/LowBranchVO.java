package app.daos;

import app.daos.primitives.AbstractLowBranchVO;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class LowBranchVO extends AbstractLowBranchVO {

  private static final long serialVersionUID = 1L;

  // Add custom code below.

}