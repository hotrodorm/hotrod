package com.myapp1.persistence;

import com.myapp1.persistence.primitives.AbstractRegionVO;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class RegionVO extends AbstractRegionVO {

  private static final long serialVersionUID = 1L;

  // Add custom code below.

}
