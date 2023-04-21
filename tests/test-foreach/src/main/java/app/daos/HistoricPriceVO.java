package app.daos;

import app.daos.primitives.AbstractHistoricPriceVO;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import app.daos.primitives.HistoricPriceDAO;
import org.springframework.beans.factory.annotation.Autowired;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class HistoricPriceVO extends AbstractHistoricPriceVO {

  private static final long serialVersionUID = 1L;

  @Autowired
  private HistoricPriceDAO historicPriceDAO;

  // Add custom code below.

}
