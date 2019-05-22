package examples.livesql;

import java.sql.SQLException;

import hotrod.test.SpringBeanRetriever;

public class AllExamples {

  public static void main(final String[] args) throws SQLException {

    GeneralSQL generalSQL = SpringBeanRetriever.getBean("generalSQLExamples");
    generalSQL.limit();

    Joins joins = SpringBeanRetriever.getBean("joinsExamples");
    joins.innerJoin();

    DataTypes datatypes = SpringBeanRetriever.getBean("dataTypesExamples");
    datatypes.filter();

    OperatorsAndFunctions oaf = SpringBeanRetriever.getBean("operatorsAndFunctionsExamples");
    oaf.arithmetic();

  }

}
