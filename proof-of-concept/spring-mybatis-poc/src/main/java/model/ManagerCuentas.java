package model;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import exceptions.CuentaInactivaException;
import exceptions.CuentaNoExisteException;
import exceptions.SaldoInsuficienteException;
import persistence.CuentaConNombreVO;
import persistence.CuentaVO;
import persistence.primitives.CuentaDAO;
import persistence.primitives.CuentaDAO.CuentaOrderBy;
import persistence.primitives.ReportesDAO;

public class ManagerCuentas {

  // Beans

  private SqlSession sqlSession;
  private CuentaDAO cuentaDAO;
  private ReportesDAO reportesDAO;

  // Bean setter

  public void setSqlSession(final SqlSession sqlSession) {
    this.sqlSession = sqlSession;
  }

  public void setCuentaDAO(final CuentaDAO cuentaDAO) {
    this.cuentaDAO = cuentaDAO;
  }

  public void setReportesDAO(final ReportesDAO reportesDAO) {
    this.reportesDAO = reportesDAO;
  }

  // Comportamiento

  @Transactional(propagation = Propagation.REQUIRED)
  public void transferir(final String desdeCta, final String haciaCta, final Integer monto) {
    abonar(haciaCta, monto);
    abonar(desdeCta, -monto);
  }

  public int abonar(final String numCta, final Integer monto) {
    CuentaVO c = this.cuentaDAO.selectByPK(numCta);
    if (c == null) {
      throw new CuentaNoExisteException();
    }
    if (c.getSaldo() == null) {
      throw new CuentaInactivaException();
    }
    if (monto < 0 && c.getSaldo() < -monto) {
      throw new SaldoInsuficienteException();
    }
    c.setSaldo(c.getSaldo() + monto);
    return this.cuentaDAO.update(c);
  }

  public void listarCuentas() {
    List<CuentaVO> todas = this.cuentaDAO.selectByExample(new CuentaVO(), CuentaOrderBy.ID_CLIENTE,
        CuentaOrderBy.CREADA_EN);
    System.out.println("--- Listado de Cuentas (" + todas.size() + " cuentas) ---");
    for (CuentaVO c : todas) {
      System.out.println("    * Cuenta " + c.getNumCta() + " - Saldo: $" + c.getSaldo());
    }
    System.out.println("--- Fin de Listado ---");
  }

  public void listarCuentasConNombre() {
    List<CuentaConNombreVO> todas = this.reportesDAO.listarCuentasConNombre();
    System.out.println("--- Listado de Cuentas con Nombre (" + todas.size() + " cuentas) ---");
    for (CuentaConNombreVO c : todas) {
      System.out.println(
          "    * Cuenta " + c.getNumCta() + " (perteneciente a " + c.getNombre() + ") - Saldo: $" + c.getSaldo());
    }
    System.out.println("--- Fin de Listado ---");
  }

}
