package app.daos;

import app.daos.primitives.AbstractAgentVO;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import app.daos.primitives.AgentDAO;
import org.springframework.beans.factory.annotation.Autowired;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AgentVO extends AbstractAgentVO {

  private static final long serialVersionUID = 1L;

  @Autowired
  private AgentDAO agentDAO;

  // Add custom code below.

}
