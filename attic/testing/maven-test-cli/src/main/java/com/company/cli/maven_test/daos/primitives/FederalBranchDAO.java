// Autogenerated by HotRod -- Do not edit.

package com.company.cli.maven_test.daos.primitives;

import java.io.Serializable;
import java.util.List;

import org.apache.ibatis.session.SqlSession;

import org.hotrod.runtime.interfaces.DaoWithOrder;
import org.hotrod.runtime.interfaces.UpdateByExampleDao;
import org.hotrod.runtime.interfaces.OrderBy;

import com.company.cli.maven_test.daos.FederalBranchVO;
import com.company.cli.maven_test.daos.ClientVO;
import com.company.cli.maven_test.daos.primitives.ClientDAO.ClientOrderBy;
import com.company.cli.maven_test.daos.primitives.ClientDAO;
import com.company.cli.maven_test.daos.TransactionVO;
import com.company.cli.maven_test.daos.primitives.TransactionDAO.TransactionOrderBy;
import com.company.cli.maven_test.daos.primitives.TransactionDAO;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

@Component("federalBranchDAO")
public class FederalBranchDAO implements Serializable, ApplicationContextAware {

  private static final long serialVersionUID = 1L;

  @Autowired
  private SqlSession sqlSession;

  @Value("#{sqlDialectFactory.sqlDialect}")
  private SQLDialect sqlDialect;

  private ApplicationContext applicationContext;

  @Override
  public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }

  // select by primary key

  public com.company.cli.maven_test.daos.FederalBranchVO selectByPK(final java.lang.Integer id) {
    if (id == null)
      return null;
    com.company.cli.maven_test.daos.FederalBranchVO vo = new com.company.cli.maven_test.daos.FederalBranchVO();
    vo.setId(id);
    return this.sqlSession.selectOne("com.company.cli.maven_test.daos.primitives.federalBranch.selectByPK", vo);
  }

  // select by unique indexes: no unique indexes found (besides the PK) -- skipped

  // select by example

  public List<com.company.cli.maven_test.daos.FederalBranchVO> selectByExample(final com.company.cli.maven_test.daos.FederalBranchVO example, final FederalBranchOrderBy... orderBies)
      {
    DaoWithOrder<com.company.cli.maven_test.daos.FederalBranchVO, FederalBranchOrderBy> dwo = //
        new DaoWithOrder<com.company.cli.maven_test.daos.FederalBranchVO, FederalBranchOrderBy>(example, orderBies);
    return this.sqlSession.selectList("com.company.cli.maven_test.daos.primitives.federalBranch.selectByExample", dwo);
  }

  // select by criteria

  public CriteriaWherePhase<com.company.cli.maven_test.daos.FederalBranchVO> selectByCriteria(final FederalBranchDAO.FederalBranchTable from,
      final Predicate predicate) {
    return new CriteriaWherePhase<com.company.cli.maven_test.daos.FederalBranchVO>(from, this.sqlDialect, this.sqlSession,
        predicate, "com.company.cli.maven_test.daos.primitives.federalBranch.selectByCriteria");
  }

  // insert

  public int insert(final com.company.cli.maven_test.daos.FederalBranchVO vo) {
    String id = "com.company.cli.maven_test.daos.primitives.federalBranch.insert";
    return this.sqlSession.insert(id, vo);
  }

  // update by PK

  public int update(final com.company.cli.maven_test.daos.FederalBranchVO vo) {
    if (vo.id == null) return 0;
    return this.sqlSession.update("com.company.cli.maven_test.daos.primitives.federalBranch.updateByPK", vo);
  }

  // delete by PK

  public int delete(final com.company.cli.maven_test.daos.FederalBranchVO vo) {
    if (vo.id == null) return 0;
    return this.sqlSession.delete("com.company.cli.maven_test.daos.primitives.federalBranch.deleteByPK", vo);
  }

  // update by example

  public int updateByExample(final com.company.cli.maven_test.daos.FederalBranchVO example, final com.company.cli.maven_test.daos.FederalBranchVO updateValues) {
    UpdateByExampleDao<com.company.cli.maven_test.daos.FederalBranchVO> fvd = //
      new UpdateByExampleDao<com.company.cli.maven_test.daos.FederalBranchVO>(example, updateValues);
    return this.sqlSession.update("com.company.cli.maven_test.daos.primitives.federalBranch.updateByExample", fvd);
  }

  // delete by example

  public int deleteByExample(final com.company.cli.maven_test.daos.FederalBranchVO example) {
    return this.sqlSession.delete("com.company.cli.maven_test.daos.primitives.federalBranch.deleteByExample", example);
  }

  // DAO ordering

  public enum FederalBranchOrderBy implements OrderBy {

    ID("federal_branch", "id", true), //
    ID$DESC("federal_branch", "id", false), //
    NAME("federal_branch", "name", true), //
    NAME$DESC("federal_branch", "name", false), //
    NAME$CASEINSENSITIVE("federal_branch", "lower(name)", true), //
    NAME$CASEINSENSITIVE_STABLE_FORWARD("federal_branch", "lower(name), name", true), //
    NAME$CASEINSENSITIVE_STABLE_REVERSE("federal_branch", "lower(name), name", false), //
    NAME$DESC_CASEINSENSITIVE("federal_branch", "lower(name)", false), //
    NAME$DESC_CASEINSENSITIVE_STABLE_FORWARD("federal_branch", "lower(name), name", false), //
    NAME$DESC_CASEINSENSITIVE_STABLE_REVERSE("federal_branch", "lower(name), name", true);

    private FederalBranchOrderBy(final String tableName, final String columnName,
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

  public static FederalBranchTable newTable() {
    return new FederalBranchTable();
  }

  public static FederalBranchTable newTable(final String alias) {
    return new FederalBranchTable(alias);
  }

  public static class FederalBranchTable extends Table {

    // Properties

    public NumberColumn id;
    public StringColumn name;

    // Constructors

    FederalBranchTable() {
      super(null, null, "federal_branch", "Table", null);
      initialize();
    }

    FederalBranchTable(final String alias) {
      super(null, null, "federal_branch", "Table", alias);
      initialize();
    }

    // Initialization

    private void initialize() {
      this.id = new NumberColumn(this, "id", "id");
      this.name = new StringColumn(this, "name", "name");
    }

  }

}