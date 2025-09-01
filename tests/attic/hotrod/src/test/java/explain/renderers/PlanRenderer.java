package explain.renderers;

import java.sql.SQLException;

import explain.Operator;

public interface PlanRenderer {

  String render(Operator op) throws SQLException;

}
