package org.hotrod.dynamicsql.segments;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class ParameterSegment extends StaticSegment {

  public abstract String getName();

  public abstract Object getValue();

  public abstract void applyTo(PreparedStatement ps, int ordinal) throws SQLException;

}
