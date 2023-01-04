package com.myapp.daos;

import com.myapp.daos.primitives.TaskVO;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TaskImpl extends TaskVO {

  private static final long serialVersionUID = 1L;

  // Add custom code below.

}
