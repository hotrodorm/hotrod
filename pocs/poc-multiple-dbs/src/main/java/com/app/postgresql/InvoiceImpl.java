package com.app.postgresql;

import com.app.postgresql.primitives.InvoiceVO;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class InvoiceImpl extends InvoiceVO {

  private static final long serialVersionUID = 1L;

  // Add custom code below.

}
