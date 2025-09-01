package hr3.springboot.poc.hotrod;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import hr3.springboot.poc.hotrod.primitives.PersonaVO;
import hr3.springboot.poc.model.Persona;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PersonaImpl extends PersonaVO implements Persona {

  private static final long serialVersionUID = 1L;

  // Add custom code below.

}
