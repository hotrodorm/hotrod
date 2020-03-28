package empusambcore.persistence;

import org.hotrod.runtime.util.ListWriter;

public class DaoWithOrder<P, O extends OrderBy> {

  private P p;
  private O[] o;
  private String renderedOrderby;

  public DaoWithOrder(final P p, final O... o) {
    this.p = p;
    this.o = o;
    this.renderedOrderby = render();
  }

  public P getP() {
    return p;
  }

  public String getO() {
    return this.renderedOrderby;
  }

  // Helpers

  public String render() {
    if (o == null || o.length == 0) {
      return null;
    }
    ListWriter lw = new ListWriter(", ");
    for (O ob : this.o) {
      lw.add(ob.getColumnName() + (ob.isAscending() ? "" : " desc"));
    }
    return lw.toString();
  }

}
