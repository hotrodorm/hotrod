package app.persistence.model;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import app.persistence.layout.FoundEmployeeLayout;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class FoundEmployee extends FoundEmployeeLayout {

  private static final long serialVersionUID = 1L;

  // Add custom code below.

}
