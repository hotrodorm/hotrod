package com.myapp.daos;

import com.myapp.daos.primitives.ClientVO;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ClientImpl extends ClientVO {

  private static final long serialVersionUID = 1L;

  // Add custom code below.

}
