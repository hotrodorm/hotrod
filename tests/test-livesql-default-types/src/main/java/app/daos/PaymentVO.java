package app.daos;

import app.daos.primitives.AbstractPaymentVO;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import app.daos.primitives.PaymentDAO;
import org.springframework.beans.factory.annotation.Autowired;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PaymentVO extends AbstractPaymentVO {

  private static final long serialVersionUID = 1L;

  @Autowired
  private PaymentDAO paymentDAO;

  // Add custom code below.

}
