package com.myapp1.app;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.myapp1.controller.Controller1;
import com.myapp1.controller.Controller2.CuentaInactivaException;
import com.myapp1.controller.Controller2.CuentaNoExisteException;
import com.myapp1.controller.Controller2.SaldoInsuficienteException;

@WebServlet("/transferirservlet")
public class TransferirServlet extends HttpServlet {

  private static final long serialVersionUID = 1L;

  protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
      throws ServletException, IOException {

    HttpSession session = (HttpSession) request.getSession();
    Cuentas cuentas = (Cuentas) session.getAttribute("cuentas");
    if (cuentas == null) {
      cuentas = new Cuentas();
      session.setAttribute("cuentas", cuentas);
    }
    session.removeAttribute("mensaje");

    boolean comandoTransferir = request.getParameter("transferir") != null;
    if (comandoTransferir) { // transferir
      comandoTransferir(request);
    }

    cuentas.refrescar();

    response.sendRedirect("transferir.jsp");
  }

  protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
      throws ServletException, IOException {
    doGet(request, response);
  }

  // Comandos

  private void comandoTransferir(final HttpServletRequest request) {
    HttpSession session = (HttpSession) request.getSession();
    Integer desdeId = new Integer(request.getParameter("desde"));
    Integer haciaId = new Integer(request.getParameter("hacia"));
    Integer monto = new Integer(request.getParameter("monto"));
    try {

      Controller1 c1 = SpringBeanRetriever.getBean("controller1");
      c1.transferir(desdeId, haciaId, monto);

      session.setAttribute("mensaje", "Transferencia exitosa.");
      session.setAttribute("tipo-mensaje", "info");

    } catch (CuentaNoExisteException e) {
      session.setAttribute("mensaje", "La cuenta no existe.");
      session.setAttribute("tipo-mensaje", "error");

    } catch (CuentaInactivaException e) {
      session.setAttribute("mensaje", "La cuenta está inactiva.");
      session.setAttribute("tipo-mensaje", "error");

    } catch (SaldoInsuficienteException e) {
      session.setAttribute("mensaje", "Saldo insuficiente.");
      session.setAttribute("tipo-mensaje", "error");

    } catch (RuntimeException e) {
      e.printStackTrace();
      session.setAttribute("mensaje", "Transferencia fallida.");
      session.setAttribute("tipo-mensaje", "error");
    }

  }

}
