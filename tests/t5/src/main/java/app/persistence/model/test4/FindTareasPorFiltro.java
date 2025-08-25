package app.persistence.model.test4;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import app.persistence.layout.test4.FindTareasPorFiltroLayout;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class FindTareasPorFiltro extends FindTareasPorFiltroLayout {

  private static final long serialVersionUID = 1L;

  // Add custom code below.

}
