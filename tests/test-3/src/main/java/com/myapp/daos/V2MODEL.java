package com.myapp.daos;

import com.myapp.daos.primitives.V2VO;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import com.myapp.daos.primitives.V2DAO;
import org.springframework.beans.factory.annotation.Autowired;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class V2MODEL extends V2VO implements java.io.Serializable {

  private static final long serialVersionUID = 1L;

  @Autowired
  private V2DAO v2DAO;

  // Add custom code below.

}
