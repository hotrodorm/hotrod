// Autogenerated by HotRod -- Do not edit.

package app5.persistence.primitives;

import java.io.Serializable;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.hotrod.runtime.cursors.Cursor;
import org.hotrod.runtime.livesql.queries.select.MyBatisCursor;

import org.hotrod.runtime.interfaces.DaoWithOrder;
import org.hotrod.runtime.interfaces.UpdateByExampleDao;
import org.hotrod.runtime.interfaces.OrderBy;
import org.hotrod.runtime.interfaces.Selectable;

import app5.persistence.ActiveClientVO;

import org.hotrod.runtime.livesql.expressions.ResultSetColumn;
import org.hotrod.runtime.livesql.dialects.SQLDialect;
import org.hotrod.runtime.livesql.metadata.NumberColumn;
import org.hotrod.runtime.livesql.metadata.StringColumn;
import org.hotrod.runtime.livesql.metadata.DateTimeColumn;
import org.hotrod.runtime.livesql.metadata.BooleanColumn;
import org.hotrod.runtime.livesql.metadata.ByteArrayColumn;
import org.hotrod.runtime.livesql.metadata.ObjectColumn;
import org.hotrod.runtime.livesql.metadata.Table;
import org.hotrod.runtime.livesql.expressions.predicates.Predicate;
import org.hotrod.runtime.livesql.queries.select.CriteriaWherePhase;
import org.hotrod.runtime.livesql.metadata.View;

import org.springframework.stereotype.Component;
import org.springframework.beans.BeansException;
import org.springframework.context.annotation.Lazy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

@Component
public class ActiveClientDAO implements Serializable, ApplicationContextAware {

  private static final long serialVersionUID = 1L;

  @Autowired
  private SqlSession sqlSession;

  @Autowired
  private SQLDialect sqlDialect;

  private ApplicationContext applicationContext;

  @Override
  public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }

  // select by example

  public List<app5.persistence.ActiveClientVO> selectByExample(final app5.persistence.ActiveClientVO example, final ActiveClientOrderBy... orderBies)
      {
    DaoWithOrder<app5.persistence.ActiveClientVO, ActiveClientOrderBy> dwo = //
        new DaoWithOrder<app5.persistence.ActiveClientVO, ActiveClientOrderBy>(example, orderBies);
    return this.sqlSession.selectList("app5.persistence.primitives.activeClient.selectByExample", dwo);
  }

  public Cursor<app5.persistence.ActiveClientVO> selectByExampleCursor(final app5.persistence.ActiveClientVO example, final ActiveClientOrderBy... orderBies)
      {
    DaoWithOrder<app5.persistence.ActiveClientVO, ActiveClientOrderBy> dwo = //
        new DaoWithOrder<app5.persistence.ActiveClientVO, ActiveClientOrderBy>(example, orderBies);
    return new MyBatisCursor<app5.persistence.ActiveClientVO>(this.sqlSession.selectCursor("app5.persistence.primitives.activeClient.selectByExample", dwo));
  }

  // select by criteria

  public CriteriaWherePhase<app5.persistence.ActiveClientVO> selectByCriteria(final ActiveClientDAO.ActiveClientView from,
      final Predicate predicate) {
    return new CriteriaWherePhase<app5.persistence.ActiveClientVO>(from, this.sqlDialect, this.sqlSession,
        predicate, "app5.persistence.primitives.activeClient.selectByCriteria");
  }

  // insert by example

  public int insertByExample(final app5.persistence.ActiveClientVO example) {
    return sqlSession.insert("app5.persistence.primitives.activeClient.insertByExample", example);
  }

  // update by example

  public int updateByExample(final app5.persistence.ActiveClientVO example, final app5.persistence.ActiveClientVO updateValues) {
    UpdateByExampleDao<app5.persistence.ActiveClientVO> fvd = //
      new UpdateByExampleDao<app5.persistence.ActiveClientVO>(example, updateValues);
    return this.sqlSession.update("app5.persistence.primitives.activeClient.updateByExample", fvd);
  }

  // delete by example

  public int deleteByExample(final app5.persistence.ActiveClientVO example) {
    return this.sqlSession.delete("app5.persistence.primitives.activeClient.deleteByExample", example);
  }

  // DAO ordering

  public enum ActiveClientOrderBy implements OrderBy {

    ID("active_client", "id", true), //
    ID$DESC("active_client", "id", false), //
    NAME("active_client", "name", true), //
    NAME$DESC("active_client", "name", false), //
    NAME$CASEINSENSITIVE("active_client", "lower(name)", true), //
    NAME$CASEINSENSITIVE_STABLE_FORWARD("active_client", "lower(name), name", true), //
    NAME$CASEINSENSITIVE_STABLE_REVERSE("active_client", "lower(name), name", false), //
    NAME$DESC_CASEINSENSITIVE("active_client", "lower(name)", false), //
    NAME$DESC_CASEINSENSITIVE_STABLE_FORWARD("active_client", "lower(name), name", false), //
    NAME$DESC_CASEINSENSITIVE_STABLE_REVERSE("active_client", "lower(name), name", true), //
    ACTIVE("active_client", "active", true), //
    ACTIVE$DESC("active_client", "active", false);

    private ActiveClientOrderBy(final String tableName, final String columnName,
        boolean ascending) {
      this.tableName = tableName;
      this.columnName = columnName;
      this.ascending = ascending;
    }

    private String tableName;
    private String columnName;
    private boolean ascending;

    public String getTableName() {
      return this.tableName;
    }

    public String getColumnName() {
      return this.columnName;
    }

    public boolean isAscending() {
      return this.ascending;
    }

  }

  // Database View metadata

  public static ActiveClientView newView() {
    return new ActiveClientView();
  }

  public static ActiveClientView newView(final String alias) {
    return new ActiveClientView(alias);
  }

  public static class ActiveClientView extends View {

    // Properties

    public NumberColumn id;
    public StringColumn name;
    public BooleanColumn active;

    // Constructors

    ActiveClientView() {
      super(null, null, "active_client", "View", null);
      initialize();
    }

    ActiveClientView(final String alias) {
      super(null, null, "active_client", "View", alias);
      initialize();
    }

    // Initialization

    private void initialize() {
      this.id = new NumberColumn(this, "id", "id");
      this.name = new StringColumn(this, "name", "name");
      this.active = new BooleanColumn(this, "active", "active");
    }

  }

}