// Autogenerated by HotRod -- Do not edit.

package persistence.primitives;

import java.io.Serializable;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import org.hotrod.runtime.tx.TxManager;
import org.hotrod.runtime.interfaces.DaoWithOrder;
import org.hotrod.runtime.interfaces.UpdateByExampleDao;
import org.hotrod.runtime.interfaces.OrderBy;

import persistence.Cliente;
import persistence.Cuenta;
import persistence.primitives.CuentaDAO.CuentaOrderBy;
import persistence.primitives.CuentaDAO;


public class ClienteDAO implements Serializable {

  private static final long serialVersionUID = 1L;

  private SqlSession sqlSession;

  // Bean setter

  public void setSqlSession(final SqlSession sqlSession) {
    this.sqlSession = sqlSession;
  }


  // select by primary key

  public Cliente selectByPK(final java.lang.Integer id) {
    if (id == null)
      return null;
    Cliente vo = new Cliente();
    vo.setId(id);
    return this.sqlSession.selectOne("persistence.primitives.cliente.selectByPK", vo);
  }

  // select by unique indexes

  public Cliente  selectByUINombre(final java.lang.String nombre) {
    if (nombre == null)
      return null;
    Cliente vo = new Cliente();
    vo.setNombre(nombre);
    return this.sqlSession.selectOne("persistence.primitives.cliente.selectByUINombre", vo);
  }

  // select by example (with ordering)

  public List<Cliente> selectByExample(final Cliente example, final ClienteOrderBy... orderBies)
      {
    DaoWithOrder<Cliente, ClienteOrderBy> dwo = //
        new DaoWithOrder<Cliente, ClienteOrderBy>(example, orderBies);
    return this.sqlSession.selectList("persistence.primitives.cliente.selectByExample", dwo);
  }

  // insert

  public int insert(final Cliente vo) {
    String id = "persistence.primitives.cliente.insert";
    int rows = this.sqlSession.insert(id, vo);
    return rows;
  }

  // update by PK

  public int update(final Cliente vo) {
    if (vo.id == null) return 0;
    return this.sqlSession.update("persistence.primitives.cliente.updateByPK", vo);
  }

  // delete by PK

  public int delete(final Cliente vo) {
    if (vo.id == null) return 0;
    return this.sqlSession.delete("persistence.primitives.cliente.deleteByPK", vo);
  }

  // update by example

  public int updateByExample(final Cliente example, final Cliente updateValues) {
    UpdateByExampleDao<Cliente> fvd = //
      new UpdateByExampleDao<Cliente>(example, updateValues);
    return this.sqlSession.update("persistence.primitives.cliente.updateByExample", fvd);
  }

  // delete by example

  public int deleteByExample(final Cliente example) {
    return this.sqlSession.delete("persistence.primitives.cliente.deleteByExample", example);
  }

  // DAO ordering

  public enum ClienteOrderBy implements OrderBy {

    ID("cliente", "id", true), //
    ID$DESC("cliente", "id", false), //
    NOMBRE("cliente", "nombre", true), //
    NOMBRE$DESC("cliente", "nombre", false), //
    NOMBRE$CASEINSENSITIVE("cliente", "lower(nombre)", true), //
    NOMBRE$CASEINSENSITIVE_STABLE_FORWARD("cliente", "lower(nombre), nombre", true), //
    NOMBRE$CASEINSENSITIVE_STABLE_REVERSE("cliente", "lower(nombre), nombre", false), //
    NOMBRE$DESC_CASEINSENSITIVE("cliente", "lower(nombre)", false), //
    NOMBRE$DESC_CASEINSENSITIVE_STABLE_FORWARD("cliente", "lower(nombre), nombre", false), //
    NOMBRE$DESC_CASEINSENSITIVE_STABLE_REVERSE("cliente", "lower(nombre), nombre", true);

    private ClienteOrderBy(final String tableName, final String columnName,
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

}
