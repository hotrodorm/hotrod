package empusambcore.persistence;

public interface OrderBy {

  String getTableName();

  String getColumnName();

  boolean isAscending();

}
