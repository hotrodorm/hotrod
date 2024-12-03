package app.daos.test2;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import app.daos.test2.primitives.AbstractFindTareasPorFiltroVO;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class FindTareasPorFiltroVO extends AbstractFindTareasPorFiltroVO {

  private static final long serialVersionUID = 1L;

  // Add custom code below.

}
