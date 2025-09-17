package app.persistence.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import app.persistence.dao.CoinDAO;
import app.persistence.layout.CoinLayout;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Coin extends CoinLayout {

  private static final long serialVersionUID = 1L;

  @SuppressWarnings("unused")
  @Autowired
  private CoinDAO coinDAO;

  // Add custom code below.

}
