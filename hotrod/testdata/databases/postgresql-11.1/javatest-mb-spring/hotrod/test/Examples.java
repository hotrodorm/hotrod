package hotrod.test;

import java.io.IOException;
import java.sql.SQLException;

public class Examples {

  public static void main(final String[] args) throws IOException, SQLException {

    UIServices ui = SpringBeanRetriever.getBean("uiServices");
    // ui.runExamples();
    // ui.runLiveSQL1();
    ui.runLiveSQL2();
    // ui.runSelectbyCriteria();
    // ui.runSelectbyCriteriaBinary();
    // ui.runSelectbyCriteriaUUID();
    // ui.runSelectbyCriteriaIn();

  }

}
