package hr3.poc.dao.action;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import hr3.poc.dao.action.primitives.ActionVO;
import hr3.poc.model.Action;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ActionImpl extends ActionVO implements Action {

  private static final long serialVersionUID = 1L;

  // Add custom code below.

}
