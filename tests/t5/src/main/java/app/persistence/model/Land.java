package app.persistence.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import app.persistence.dao.LandDAO;
import app.persistence.layout.LandLayout;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Land extends LandLayout {

  private static final long serialVersionUID = 1L;

  @SuppressWarnings("unused")
  @Autowired
  private LandDAO landDAO;

  // Add custom code below.

}
