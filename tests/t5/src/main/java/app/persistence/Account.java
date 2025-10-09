package app.persistence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import app.persistence.dao.AccountDAO;
import app.persistence.layout.AccountLayout;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Account extends AccountLayout {

  private static final long serialVersionUID = 1L;

  @SuppressWarnings("unused")
  @Autowired
  private AccountDAO accountDAO;

  // Add custom code below.

}
