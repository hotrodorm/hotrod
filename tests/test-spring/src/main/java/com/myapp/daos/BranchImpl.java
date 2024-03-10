package com.myapp.daos;

import com.myapp.daos.primitives.BranchVO;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import com.myapp.daos.primitives.BranchDAO;
import org.springframework.beans.factory.annotation.Autowired;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class BranchImpl extends BranchVO {

  private static final long serialVersionUID = 1L;

  @Autowired
  private BranchDAO branchDAO;

  // Add custom code below.

}
