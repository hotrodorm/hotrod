package test.persistence;

import test.persistence.primitives.Transaction;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TransactionVO extends Transaction {

  private static final long serialVersionUID = 1L;

  // Add custom code below.

}
