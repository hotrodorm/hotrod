package hr3.poc.dao.action;

import hr3.poc.dao.action.primitives.PollVO;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PollImpl extends PollVO {

  private static final long serialVersionUID = 1L;

  // Add custom code below.

}
