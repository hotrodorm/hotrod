// Autogenerated by HotRod -- Do not edit.

package test.persistence.primitives;

import java.io.Serializable;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.hotrod.runtime.cursors.Cursor;
import org.hotrod.runtime.livesql.queries.select.MyBatisCursor;

import org.hotrod.runtime.interfaces.DaoWithOrder;
import org.hotrod.runtime.interfaces.UpdateByExampleDao;
import org.hotrod.runtime.interfaces.OrderBy;

import test.persistence.primitives.Agent;
import test.persistence.AgentVO;
import test.persistence.ClientVO;
import test.persistence.primitives.ClientDAO;

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
public class AgentDAO implements Serializable, ApplicationContextAware {

  private static final long serialVersionUID = 1L;

  @Autowired
  private SqlSession sqlSession;

  @Lazy
  @Autowired
  private ClientDAO clientDAO;

  @Autowired
  private SQLDialect sqlDialect;

  private ApplicationContext applicationContext;

  @Override
  public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }

  // select by primary key

  public test.persistence.AgentVO selectByPK(final java.lang.Integer id) {
    if (id == null)
      return null;
    test.persistence.AgentVO vo = new test.persistence.AgentVO();
    vo.setId(id);
    return this.sqlSession.selectOne("test.persistence.primitives.agent.selectByPK", vo);
  }

  // select by unique indexes: no unique indexes found (besides the PK) -- skipped

  // select by example

  public List<test.persistence.AgentVO> selectByExample(final test.persistence.primitives.Agent example, final AgentOrderBy... orderBies)
      {
    DaoWithOrder<test.persistence.primitives.Agent, AgentOrderBy> dwo = //
        new DaoWithOrder<>(example, orderBies);
    return this.sqlSession.selectList("test.persistence.primitives.agent.selectByExample", dwo);
  }

  public Cursor<test.persistence.AgentVO> selectByExampleCursor(final test.persistence.primitives.Agent example, final AgentOrderBy... orderBies)
      {
    DaoWithOrder<test.persistence.primitives.Agent, AgentOrderBy> dwo = //
        new DaoWithOrder<>(example, orderBies);
    return new MyBatisCursor<test.persistence.AgentVO>(this.sqlSession.selectCursor("test.persistence.primitives.agent.selectByExample", dwo));
  }

  // select by criteria

  public CriteriaWherePhase<test.persistence.AgentVO> selectByCriteria(final AgentDAO.AgentTable from,
      final Predicate predicate) {
    return new CriteriaWherePhase<test.persistence.AgentVO>(from, this.sqlDialect, this.sqlSession,
        predicate, "test.persistence.primitives.agent.selectByCriteria");
  }

  // select parent(s) by FKs

  public SelectParentClientPhase selectParentClientOf(final AgentVO vo) {
    return new SelectParentClientPhase(vo);
  }

  public class SelectParentClientPhase {

    private AgentVO vo;

    SelectParentClientPhase(final AgentVO vo) {
      this.vo = vo;
    }

    public SelectParentClientFromClientIdPhase fromClientId() {
      return new SelectParentClientFromClientIdPhase(this.vo);
    }

  }

  public class SelectParentClientFromClientIdPhase {

    private AgentVO vo;

    SelectParentClientFromClientIdPhase(final AgentVO vo) {
      this.vo = vo;
    }

    public ClientVO toId() {
      return clientDAO.selectByPK((this.vo.clientId == null) ? null : Integer.valueOf(this.vo.clientId.intValue()));
    }

  }

  // select children by FKs: no exported FKs found -- skipped

  // insert

  public test.persistence.AgentVO insert(final test.persistence.primitives.Agent vo) {
    String id = "test.persistence.primitives.agent.insert";
    int rows = this.sqlSession.insert(id, vo);
    test.persistence.AgentVO mo = new test.persistence.AgentVO();
    mo.setId(vo.getId());
    mo.setName(vo.getName());
    mo.setClientId(vo.getClientId());
    return mo;
  }

  // update by PK

  public int update(final test.persistence.AgentVO vo) {
    if (vo.id == null) return 0;
    return this.sqlSession.update("test.persistence.primitives.agent.updateByPK", vo);
  }

  // delete by PK

  public int delete(final test.persistence.AgentVO vo) {
    if (vo.id == null) return 0;
    return this.sqlSession.delete("test.persistence.primitives.agent.deleteByPK", vo);
  }

  // update by example

  public int updateByExample(final test.persistence.primitives.Agent example, final test.persistence.primitives.Agent updateValues) {
    UpdateByExampleDao<test.persistence.primitives.Agent> fvd = //
      new UpdateByExampleDao<test.persistence.primitives.Agent>(example, updateValues);
    return this.sqlSession.update("test.persistence.primitives.agent.updateByExample", fvd);
  }

  // delete by example

  public int deleteByExample(final test.persistence.primitives.Agent example) {
    return this.sqlSession.delete("test.persistence.primitives.agent.deleteByExample", example);
  }

  // DAO ordering

  public enum AgentOrderBy implements OrderBy {

    ID("agent", "id", true), //
    ID$DESC("agent", "id", false), //
    NAME("agent", "name", true), //
    NAME$DESC("agent", "name", false), //
    NAME$CASEINSENSITIVE("agent", "lower(name)", true), //
    NAME$CASEINSENSITIVE_STABLE_FORWARD("agent", "lower(name), name", true), //
    NAME$CASEINSENSITIVE_STABLE_REVERSE("agent", "lower(name), name", false), //
    NAME$DESC_CASEINSENSITIVE("agent", "lower(name)", false), //
    NAME$DESC_CASEINSENSITIVE_STABLE_FORWARD("agent", "lower(name), name", false), //
    NAME$DESC_CASEINSENSITIVE_STABLE_REVERSE("agent", "lower(name), name", true), //
    CLIENT_ID("agent", "client_id", true), //
    CLIENT_ID$DESC("agent", "client_id", false);

    private AgentOrderBy(final String tableName, final String columnName,
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

  // Database Table metadata

  public static AgentTable newTable() {
    return new AgentTable();
  }

  public static AgentTable newTable(final String alias) {
    return new AgentTable(alias);
  }

  public static class AgentTable extends Table {

    // Properties

    public NumberColumn id;
    public StringColumn name;
    public NumberColumn clientId;

    // Constructors

    AgentTable() {
      super(null, null, "agent", "Table", null);
      initialize();
    }

    AgentTable(final String alias) {
      super(null, null, "agent", "Table", alias);
      initialize();
    }

    // Initialization

    private void initialize() {
      this.id = new NumberColumn(this, "id", "id");
      this.name = new StringColumn(this, "name", "name");
      this.clientId = new NumberColumn(this, "client_id", "clientId");
    }

  }

}
