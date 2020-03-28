package com.myapp1.app;

import java.util.ArrayList;
import java.util.List;

import com.myapp1.controller.Controller1;
import com.myapp1.generado.daos.CuentaDAO;
import com.myapp1.generado.vos.CuentaVO;

public class Cuentas {

  private List<CuentaVO> cuentas = new ArrayList<CuentaVO>();

  public void refrescar() {
    CuentaDAO dao = SpringBeanRetriever.getBean("cuentaDAO");
    this.cuentas = dao.selectByExample(new CuentaVO());
  }

  public void transferirMonto(final Integer desdeId, final Integer haciaId, final Integer monto) {
    Controller1 c1 = SpringBeanRetriever.getBean("controller1");
    c1.transferir(desdeId, haciaId, monto);
  }

  public List<CuentaVO> getCuentas() {
    return cuentas;
  }

}
