package app5.livesql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LiveSQLExamples {

  @Autowired
  private GeneralSQL generalSQL;

  @Autowired
  private Joins joins;

  @Autowired
  private DataTypes datatypes;

  @Autowired
  private OperatorsAndFunctions oaf;

  @Autowired
  private SelectByCriteria sbc;

  @Autowired
  private WindowFunctions wf;

  public void runExamples() {

    generalSQL.limit();
//    generalSQL.showSQLStatement();
//    generalSQL.caseExpressions();
//
//    joins.innerJoin();
//    joins.innerJoinUsing();
//    joins.crossJoin();
//    joins.naturalFullOuterJoin();
//    joins.unionJoin();
//
//    datatypes.filter();
//
//    oaf.arithmetic();
//
//    sbc.runSelectbyCriteria();
//
//    wf.aggregate();
//    wf.windowFrames();

  }

}
