package model

import org.apache.ibatis.session.SqlSession
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import persistence.Cuenta
import persistence.primitives.CuentaDAO
import persistence.primitives.CuentaDAO.CuentaOrderBy
import persistence.primitives.ReportesDAO

open class ManagerCuentas {

  // Beans y Setters

  private var sqlSession: SqlSession? = null
  private var cuentaDAO: CuentaDAO? = null
  private var reportesDAO: ReportesDAO? = null

  // Setters (setters are separate to allow different visibility)

  open fun setSqlSession(sqlSession: SqlSession) {
    this.sqlSession = sqlSession
  }

  open fun setCuentaDAO(cuentaDAO: CuentaDAO) {
    this.cuentaDAO = cuentaDAO
  }

  open fun setReportesDAO(reportesDAO: ReportesDAO) {
    this.reportesDAO = reportesDAO
  }

  // Comportamiento

  @Transactional(propagation = Propagation.REQUIRED)
  open fun transferir(desdeCta: String, haciaCta: String, monto: Int) {
    abonar(haciaCta, monto)
    abonar(desdeCta, -monto)
  }

  private fun abonar(cuenta: String, monto: Int): Int {
    val c = cuentaDAO!!.selectByPK(cuenta)
    if (c == null) {
      throw CuentaNoExisteException()
    }

    if (c.getSaldo() == null) {
      throw CuentaInactivaException()
    }
    if (monto < 0 && c.getSaldo() < -monto) {
      throw SaldoInsuficienteException()
    }
    c.setSaldo(c.getSaldo() + monto);
    val rows = cuentaDAO!!.update(c)
    println("   ... abono exitoso")
    return rows;
  }

  open fun listarCuentas() {
    val todas = cuentaDAO!!.selectByExample(Cuenta(), CuentaOrderBy.ID_CLIENTE, CuentaOrderBy.CREADA_EN)
    println("--- Listado de Cuentas (" + todas.size + " cuentas) ---");
    todas.forEach {
      println("    * Cuenta " + it.getNumCta() + " - Saldo: $" + it.getSaldo())
    }
    println("--- Fin de Listado ---");
  }

  open fun listarCuentasConNombre() {
    val todas = reportesDAO!!.listarCuentasConNombre();
    println("--- Listado de Cuentas con Nombre (" + todas.size + " cuentas) ---");
    todas.forEach {
      println(
        "    * Cuenta " + it.getNumCta() + " (perteneciente a " + it.getNombre() + ") - Saldo: $" + it.getSaldo()
      );
    }
    println("--- Fin de Listado ---");
  }

}

