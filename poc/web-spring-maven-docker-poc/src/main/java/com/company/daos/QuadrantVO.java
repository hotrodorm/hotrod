package com.company.daos;

import com.company.daos.primitives.DomainQuadrantPrototype;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class QuadrantVO extends DomainQuadrantPrototype {

  private static final long serialVersionUID = 1L;

  // Add custom code below.

}