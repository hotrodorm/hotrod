<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"%>
<%@ page import="com.myapp1.app.Cuentas"%>
<%@ page import="com.myapp1.generado.vos.CuentaVO"%>
<%
  Cuentas cuentas = (Cuentas) request.getSession().getAttribute("cuentas");
  // System.out.println("cuentas=" + cuentas);
  if (cuentas == null) {
    response.sendRedirect("transferirservlet");
    return;
  }
  String mensaje = (String) request.getSession().getAttribute("mensaje");
%>

<html>
<head>
<title>Banco de Pelotillehue</title>
</head>
<body>

  <h2>Banco de Pelotillehue</h2>

  <%
    if (mensaje != null) {
      String tipoMensaje = (String) request.getSession().getAttribute("tipo-mensaje");
      String bg = "info".equals(tipoMensaje) ? "#4bba31" : "#ff0000";
  %>
  <p
    style="color: white;
  border: 3px solid darkgray;
  background-color: <%=bg%>;
  margin: 0 0 1em 0;
  padding: 0.5em;
  font-weight: bold;"><%=mensaje%></p>
  <%
    }
  %>

  <fieldset>
    <legend>Transferencia</legend>

    <form action="transferirservlet" method="post">

      <p>
        Desde: <select name="desde">
          <%
            for (CuentaVO c : cuentas.getCuentas()) {
          %>
          <option value="<%=c.getId()%>"><%=c.getTitulo()%></option>
          <%
            }
          %>

        </select> - Hacia: <select name="hacia">
          <%
            for (CuentaVO c : cuentas.getCuentas()) {
          %>
          <option value="<%=c.getId()%>"><%=c.getTitulo()%></option>
          <%
            }
          %>
        </select> - Monto: <input name="monto" /> <input name="transferir"
          type="Submit" value="Transferir" />
      </p>


    </form>
  </fieldset>

  <h3>Cuentas</h3>

  <table border="1">
    <tr>
      <td>ID</td>
      <td>Nombre</td>
      <td>Tipo</td>
      <td>Saldo</td>
    </tr>
    <%
      for (CuentaVO c : cuentas.getCuentas()) {
    %>
    <tr>
      <td><%=c.getId()%></td>
      <td><%=c.getNumero()%></td>
      <td><%=c.getTipo()%></td>
      <td><%=c.getSaldo()%></td>
    </tr>
    <%
      }
    %>
  </table>

</body>
</html>