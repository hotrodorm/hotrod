package model;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import exceptions.CuentaInactivaException;
import exceptions.CuentaNoExisteException;
import exceptions.SaldoInsuficienteException;
import persistence.CuentaConNombre;
import persistence.Cuenta;
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
    Cuenta c = this.cuentaDAO.selectByPK(numCta);
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
    List<Cuenta> todas = this.cuentaDAO.selectByExample(new Cuenta(), CuentaOrderBy.ID_CLIENTE,
        CuentaOrderBy.CREADA_EN);
    System.out.println("--- Listado de Cuentas (" + todas.size() + " cuentas) ---");
    for (Cuenta c : todas) {
      System.out.println("    * Cuenta " + c.getNumCta() + " - Saldo: $" + c.getSaldo());
    }
    System.out.println("--- Fin de Listado ---");
  }

  public void listarCuentasConNombre() {
    List<CuentaConNombre> todas = this.reportesDAO.listarCuentasConNombre();
    System.out.println("--- Listado de Cuentas con Nombre (" + todas.size() + " cuentas) ---");
    for (CuentaConNombre c : todas) {
      System.out.println(
          "    * Cuenta " + c.getNumCta() + " (perteneciente a " + c.getNombre() + ") - Saldo: $" + c.getSaldo());
    }
    System.out.println("--- Fin de Listado ---");
  }

}
