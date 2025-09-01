package com.app.mysql;

import com.app.mysql.primitives.IngredientVO;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class IngredientImpl extends IngredientVO {

  private static final long serialVersionUID = 1L;

  // Add custom code below.

}
