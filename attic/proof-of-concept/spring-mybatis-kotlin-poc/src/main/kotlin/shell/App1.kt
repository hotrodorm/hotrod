package shell

import model.ManagerCuentas;

fun main(args: Array<String>) {
  println("[Starting]")
  transferir()
  println("[Finished]")
}

fun transferir() {

  val managerCuentas: ManagerCuentas = SpringBeanRetriever.getBean("managerCuentas") as ManagerCuentas

  try {

    val desdeCta: String = "C02"
    val haciaCta: String = "A08"
    val monto: Int = 5000

    println(" ")
    managerCuentas.listarCuentas()

    println(" ")
    println("Transfiriendo $" + monto + " - desde " + desdeCta + " - hacia " + haciaCta + " ...")

    managerCuentas.transferir(desdeCta, haciaCta, monto)

    println(" ")
    println("Transferencia exitosa.")

  } catch (e: RuntimeException) {
    println(" ")
    try {
      println("Transferencia fallida")
      println(" - motivo: " + e::class.simpleName)
    } catch (e2: Exception) {
      println("Exception")
      println("Exception: " + e::class.simpleName)
    }
  }

  println(" ")
  managerCuentas.listarCuentasConNombre()

}