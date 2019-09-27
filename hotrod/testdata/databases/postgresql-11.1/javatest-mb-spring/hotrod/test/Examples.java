package hotrod.test;

import java.io.IOException;
import java.sql.SQLException;

import examples.livesql.SelectByPK;
import examples.livesql.Transactions;

public class Examples {

  public static void main(final String[] args) throws IOException, SQLException {

    {
      UIServices ui = SpringBeanRetriever.getBean("uiServices");
      // ui.runExamples();
      // ui.runLiveSQL1();
      // ui.runLiveSQL2();
      // ui.runSelectbyCriteria();
      // ui.runSelectbyCriteriaBinary();
      // ui.runSelectbyCriteriaUUID();
      // ui.runSelectbyCriteriaIn();
      // ui.getNewAccountVolume();
      // ui.frames();
    }

    {
      UIServicesLiveSQL uil = SpringBeanRetriever.getBean("uiServices2");
      // uil.runLiveSQL2();
      // uil.runLiveSQL3Binary();
    }

    {
      Transactions txs = SpringBeanRetriever.getBean("transactions");
      // txs.transfer(1234001, 1234004, 200);
    }

    {
      SelectByPK sbpk = SpringBeanRetriever.getBean("selectsByPK");
//      sbpk.insertSelect();
    }

    {
      SelectByPrimaryKeyTest s = SpringBeanRetriever.getBean("selectsByPrimaryKeyTest");
      s.select1();
    }

  }

}
