package app.daos;

import app.daos.primitives.AbstractInvoiceLineVO;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import app.daos.primitives.InvoiceLineDAO;
import org.springframework.beans.factory.annotation.Autowired;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class InvoiceLineVO extends AbstractInvoiceLineVO {

  private static final long serialVersionUID = 1L;

  @Autowired
  private InvoiceLineDAO invoiceLineDAO;

  // Add custom code below.

}
