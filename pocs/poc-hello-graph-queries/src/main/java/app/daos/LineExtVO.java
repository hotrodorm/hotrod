package app.daos;

import app.daos.primitives.LineExt;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class LineExtVO extends LineExt {

  private static final long serialVersionUID = 1L;

  // Add custom code below.

}
