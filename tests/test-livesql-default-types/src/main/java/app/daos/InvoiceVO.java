package app.daos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import app.daos.primitives.AbstractInvoiceVO;
import app.daos.primitives.InvoiceDAO;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class InvoiceVO extends AbstractInvoiceVO {

  private static final long serialVersionUID = 1L;

  @Autowired
  private InvoiceDAO invoiceDAO;

  // Add custom code below.

  public InvoiceDAO getInvoiceDAO() {
    return invoiceDAO;
  }

}
