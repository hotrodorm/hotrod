package org.hotrod.runtime.livesql.queries.select.entity;

import java.util.List;

import org.hotrod.runtime.livesql.queries.LiveSQLContext;
import org.hotrod.runtime.livesql.queries.select.AbstractSelectObject;
import org.hotrod.runtime.livesql.queries.select.sets.CombinedSelectObject;

public class LockableSelectEntityPhase<R> {

  private LiveSQLContext context;
  private CombinedSelectObject<R> cs;

  public LockableSelectEntityPhase(final LiveSQLContext context, final CombinedSelectObject<R> cs) {
    this.context = context;
    System.out.println("cs="+cs);
    this.cs = cs;
  }

//  // Next Phases
//
//  public SelectForUpdatePhase<R> forUpdate() {
//    return new SelectForUpdatePhase<R>(this.context, this.combined);
//  }

  // Execute

  public final List<R> execute() {
    AbstractSelectObject<R> so = this.cs.getFirstSelect();
    return this.cs.execute(this.context);
  }

//  List<Row> rows = this.sql
//      .select(
//        a.star().as(c -> "a:" + c.getProperty()),
//        b.star().as(c -> "b:" + c.getProperty())
//      )
//      .from(b)
//      .join(a, a.branchId.eq(b.id))
//      .where(b.region.like("N%"))
//      .orderBy(b.id.desc())
//      .execute();
//  for (Row r : rows) {
//    AccountVO account = this.accountDAO.parseRow(r, "a:");
//    BranchVO branch = this.branchDAO.parseRow(r, "b:");
//  }

}
