// Autogenerated by HotRod -- Do not edit.

package hr3.springboot.poc.hotrod.primitives;

import java.io.Serializable;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.hotrod.runtime.cursors.Cursor;
import org.hotrod.runtime.livesql.queries.select.MyBatisCursor;

import org.hotrod.runtime.interfaces.DaoWithOrder;
import org.hotrod.runtime.interfaces.UpdateByExampleDao;
import org.hotrod.runtime.interfaces.OrderBy;

import hr3.springboot.poc.hotrod.RequestItemImpl;
import hr3.springboot.poc.hotrod.ArticuloImpl;
import hr3.springboot.poc.hotrod.primitives.ArticuloDAO;
import hr3.springboot.poc.hotrod.PedidoImpl;
import hr3.springboot.poc.hotrod.primitives.PedidoDAO;

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

@Component
public class RequestItemDAO implements Serializable, ApplicationContextAware {

  private static final long serialVersionUID = 1L;

  @Autowired
  private SqlSession sqlSession;

  @Autowired
  private PedidoDAO pedidoDAO;

  @Autowired
  private ArticuloDAO articuloDAO;

  @Autowired
  private SQLDialect sqlDialect;

  private ApplicationContext applicationContext;

  @Override
  public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }

  // select by primary key

  public hr3.springboot.poc.hotrod.RequestItemImpl selectByPK(final java.lang.Long idPedido, final java.lang.Long idArticulo) {
    if (idPedido == null)
      return null;
    if (idArticulo == null)
      return null;
    hr3.springboot.poc.hotrod.RequestItemImpl vo = new hr3.springboot.poc.hotrod.RequestItemImpl();
    vo.setIdPedido(idPedido);
    vo.setIdArticulo(idArticulo);
    return this.sqlSession.selectOne("hr3.springboot.poc.hotrod.primitives.requestItem.selectByPK", vo);
  }

  // select by unique indexes: no unique indexes found (besides the PK) -- skipped

  // select by example

  public List<hr3.springboot.poc.hotrod.RequestItemImpl> selectByExample(final hr3.springboot.poc.hotrod.RequestItemImpl example, final RequestItemOrderBy... orderBies)
      {
    DaoWithOrder<hr3.springboot.poc.hotrod.RequestItemImpl, RequestItemOrderBy> dwo = //
        new DaoWithOrder<hr3.springboot.poc.hotrod.RequestItemImpl, RequestItemOrderBy>(example, orderBies);
    return this.sqlSession.selectList("hr3.springboot.poc.hotrod.primitives.requestItem.selectByExample", dwo);
  }

  public Cursor<hr3.springboot.poc.hotrod.RequestItemImpl> selectByExampleCursor(final hr3.springboot.poc.hotrod.RequestItemImpl example, final RequestItemOrderBy... orderBies)
      {
    DaoWithOrder<hr3.springboot.poc.hotrod.RequestItemImpl, RequestItemOrderBy> dwo = //
        new DaoWithOrder<hr3.springboot.poc.hotrod.RequestItemImpl, RequestItemOrderBy>(example, orderBies);
    return new MyBatisCursor<hr3.springboot.poc.hotrod.RequestItemImpl>(this.sqlSession.selectCursor("hr3.springboot.poc.hotrod.primitives.requestItem.selectByExample", dwo));
  }

  // select by criteria

  public CriteriaWherePhase<hr3.springboot.poc.hotrod.RequestItemImpl> selectByCriteria(final RequestItemDAO.RequestItemTable from,
      final Predicate predicate) {
    return new CriteriaWherePhase<hr3.springboot.poc.hotrod.RequestItemImpl>(from, this.sqlDialect, this.sqlSession,
        predicate, "hr3.springboot.poc.hotrod.primitives.requestItem.selectByCriteria");
  }

  // select parent(s) by FKs

  public SelectParentArticuloPhase selectParentArticuloOf(final RequestItemImpl vo) {
    return new SelectParentArticuloPhase(vo);
  }

  public class SelectParentArticuloPhase {

    private RequestItemImpl vo;

    SelectParentArticuloPhase(final RequestItemImpl vo) {
      this.vo = vo;
    }

    public SelectParentArticuloFromIdArticuloPhase fromIdArticulo() {
      return new SelectParentArticuloFromIdArticuloPhase(this.vo);
    }

  }

  public class SelectParentArticuloFromIdArticuloPhase {

    private RequestItemImpl vo;

    SelectParentArticuloFromIdArticuloPhase(final RequestItemImpl vo) {
      this.vo = vo;
    }

    public ArticuloImpl toArtId() {
      return articuloDAO.selectByPK(this.vo.idArticulo);
    }

  }

  public SelectParentPedidoPhase selectParentPedidoOf(final RequestItemImpl vo) {
    return new SelectParentPedidoPhase(vo);
  }

  public class SelectParentPedidoPhase {

    private RequestItemImpl vo;

    SelectParentPedidoPhase(final RequestItemImpl vo) {
      this.vo = vo;
    }

    public SelectParentPedidoFromIdPedidoPhase fromIdPedido() {
      return new SelectParentPedidoFromIdPedidoPhase(this.vo);
    }

  }

  public class SelectParentPedidoFromIdPedidoPhase {

    private RequestItemImpl vo;

    SelectParentPedidoFromIdPedidoPhase(final RequestItemImpl vo) {
      this.vo = vo;
    }

    public PedidoImpl toId() {
      return pedidoDAO.selectByPK(this.vo.idPedido);
    }

  }

  // select children by FKs: no exported FKs found -- skipped

  // insert

  public int insert(final hr3.springboot.poc.hotrod.RequestItemImpl vo) {
    String id = "hr3.springboot.poc.hotrod.primitives.requestItem.insert";
    return this.sqlSession.insert(id, vo);
  }

  // update by PK

  public int update(final hr3.springboot.poc.hotrod.RequestItemImpl vo) {
    if (vo.idPedido == null) return 0;
    if (vo.idArticulo == null) return 0;
    return this.sqlSession.update("hr3.springboot.poc.hotrod.primitives.requestItem.updateByPK", vo);
  }

  // delete by PK

  public int delete(final hr3.springboot.poc.hotrod.RequestItemImpl vo) {
    if (vo.idPedido == null) return 0;
    if (vo.idArticulo == null) return 0;
    return this.sqlSession.delete("hr3.springboot.poc.hotrod.primitives.requestItem.deleteByPK", vo);
  }

  // update by example

  public int updateByExample(final hr3.springboot.poc.hotrod.RequestItemImpl example, final hr3.springboot.poc.hotrod.RequestItemImpl updateValues) {
    UpdateByExampleDao<hr3.springboot.poc.hotrod.RequestItemImpl> fvd = //
      new UpdateByExampleDao<hr3.springboot.poc.hotrod.RequestItemImpl>(example, updateValues);
    return this.sqlSession.update("hr3.springboot.poc.hotrod.primitives.requestItem.updateByExample", fvd);
  }

  // delete by example

  public int deleteByExample(final hr3.springboot.poc.hotrod.RequestItemImpl example) {
    return this.sqlSession.delete("hr3.springboot.poc.hotrod.primitives.requestItem.deleteByExample", example);
  }

  // DAO ordering

  public enum RequestItemOrderBy implements OrderBy {

    ID_PEDIDO("trx_request_item", "req_id", true), //
    ID_PEDIDO$DESC("trx_request_item", "req_id", false), //
    ID_ARTICULO("trx_request_item", "art_id", true), //
    ID_ARTICULO$DESC("trx_request_item", "art_id", false), //
    CANTIDAD("trx_request_item", "item_qty", true), //
    CANTIDAD$DESC("trx_request_item", "item_qty", false);

    private RequestItemOrderBy(final String tableName, final String columnName,
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

  public static RequestItemTable newTable() {
    return new RequestItemTable();
  }

  public static RequestItemTable newTable(final String alias) {
    return new RequestItemTable(alias);
  }

  public static class RequestItemTable extends Table {

    // Properties

    public NumberColumn idPedido;
    public NumberColumn idArticulo;
    public NumberColumn cantidad;

    // Constructors

    RequestItemTable() {
      super(null, null, "TRX_REQUEST_ITEM", "Table", null);
      initialize();
    }

    RequestItemTable(final String alias) {
      super(null, null, "TRX_REQUEST_ITEM", "Table", alias);
      initialize();
    }

    // Initialization

    private void initialize() {
      this.idPedido = new NumberColumn(this, "REQ_ID", "idPedido");
      this.idArticulo = new NumberColumn(this, "ART_ID", "idArticulo");
      this.cantidad = new NumberColumn(this, "ITEM_QTY", "cantidad");
    }

  }

}
