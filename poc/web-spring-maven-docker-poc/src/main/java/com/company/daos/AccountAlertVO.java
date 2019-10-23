package com.company.daos;

import com.company.daos.primitives.DomainAccountAlertPrototype;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AccountAlertVO extends DomainAccountAlertPrototype {

  private static final long serialVersionUID = 1L;

  // Add custom code below.

}
