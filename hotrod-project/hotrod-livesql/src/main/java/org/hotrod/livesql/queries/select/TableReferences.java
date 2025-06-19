package org.hotrod.livesql.queries.select;

import java.util.HashSet;
import java.util.Set;

import org.hotrod.livesql.exceptions.InvalidLiveSQLStatementException;
import org.hotrod.livesql.metadata.MDShield;
import org.hotrod.livesql.metadata.TableOrView;
import org.hotrod.livesql.queries.ctes.RecursiveCTE;
import org.hotrod.utils.SUtil;

public class TableReferences {

  private Set<TableOrView> tableReferences = new HashSet<>();

  private Set<RecursiveCTE> visitedRecursiveCTEs = new HashSet<>();
  private Set<RecursiveCTE> visitedRecursiveCTEs2 = new HashSet<>();

  private Set<String> aliases = new HashSet<String>();

  public void register(final String alias, final TableOrView tableOrView) {
    if (this.tableReferences.contains(tableOrView)) {
      throw new InvalidLiveSQLStatementException(
          "An instance of the " + tableOrView.getType() + " " + MDShield.renderUnescapedName(tableOrView)
              + (alias == null ? " (with no alias)" : " (with alias '" + alias + "')")
              + " is used multiple times in the Live SQL statement (in the FROM clause, JOIN clause, or a subquery). "
              + "If you need to include the same " + tableOrView.getType()
              + " multiple times in the query you can get more instances of it using the DAO method new"
              + SUtil.upperFirst(tableOrView.getType()) + "(\"alias\").");
    }
    this.tableReferences.add(tableOrView);
  }

  public Set<TableOrView> getTableReferences() {
    return tableReferences;
  }

  public Set<String> getAliases() {
    return aliases;
  }

  public int size() {
    return this.tableReferences.size();
  }

  public boolean visited(final RecursiveCTE cte) {
    if (this.visitedRecursiveCTEs.contains(cte)) {
      return true;
    }
    this.visitedRecursiveCTEs.add(cte);
    return false;
  }

  public boolean visited2(final RecursiveCTE cte) {
    if (this.visitedRecursiveCTEs2.contains(cte)) {
      return true;
    }
    this.visitedRecursiveCTEs2.add(cte);
    return false;
  }

}
