package com.myapp1.controller;

import org.apache.ibatis.session.SqlSession;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.myapp1.generado.daos.CuentaDAO;
import com.myapp1.generado.vos.CuentaVO;

public class Controller2 {

  // Beans

  private SqlSession sqlSession;
  private Controller2 controller;
  private CuentaDAO cuentaDAO;

  // Bean setter

  public void setSqlSession(final SqlSession sqlSession) {
    this.sqlSession = sqlSession;
  }

  public void setController(final Controller2 controller) {
    this.controller = controller;
  }

  public void setCuentaDAO(CuentaDAO cuentaDAO) {
    this.cuentaDAO = cuentaDAO;
  }

  // Behavior

  public static class AbonarParameters {
    public AbonarParameters(Integer id, Integer monto) {
      this.id = id;
      this.monto = monto;
    }

    public Integer id;
    public Integer monto;
  }

  @Transactional(propagation = Propagation.REQUIRED)
  public int abonar(final Integer id, final Integer monto) {
    CuentaVO c = this.cuentaDAO.selectByPK(id);
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

  // Exceptions

  public static class CuentaNoExisteException extends RuntimeException {
    private static final long serialVersionUID = 1L;
  }

  public static class CuentaInactivaException extends RuntimeException {
    private static final long serialVersionUID = 1L;
  }

  public static class SaldoInsuficienteException extends RuntimeException {
    private static final long serialVersionUID = 1L;
  }

}
