package org.hotrod.getters;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ResultSetGetter {

  Object get(ResultSet rs, int ordinal) throws SQLException;

}
