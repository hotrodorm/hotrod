package app.daos;

import app.daos.primitives.AbstractInvoiceVO;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import app.daos.primitives.InvoiceDAO;
import org.springframework.beans.factory.annotation.Autowired;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class InvoiceVO extends AbstractInvoiceVO {

  private static final long serialVersionUID = 1L;

  @Autowired
  private InvoiceDAO invoiceDAO;

  // Add custom code below.

}
