package app.daos;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import app.daos.primitives.BigAccountPrototype;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class BigAccount extends BigAccountPrototype {

  private static final long serialVersionUID = 1L;

  // Add custom code below.

}
