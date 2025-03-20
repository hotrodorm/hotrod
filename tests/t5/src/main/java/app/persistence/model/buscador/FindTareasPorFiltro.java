package app.persistence.model.buscador;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import app.persistence.layout.buscador.FindTareasPorFiltroLayout;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class FindTareasPorFiltro extends FindTareasPorFiltroLayout {

  private static final long serialVersionUID = 1L;

  // Add custom code below.

}
