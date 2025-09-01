package model

// Must be runtime exceptions for Kotlin.
// Otherwise, when kotlin receives a checked exception it doesn't know what
// to do with it, since Kotlin does NOT have checked exceptions.

class CuentaInactivaException() : RuntimeException()

class CuentaNoExisteException() : RuntimeException()

class SaldoInsuficienteException() : RuntimeException()

 

