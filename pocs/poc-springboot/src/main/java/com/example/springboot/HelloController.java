package com.example.springboot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.springboot.daos.CuentaDAO;

@Configuration
@ComponentScan

@RestController
public class HelloController {

  @Autowired
  private CuentaDAO cuenta;

  @RequestMapping("/")
  public String index() {
    return "Greetings from Spring Boot!";
  }

  @RequestMapping("/saldo")
  public String saldo() {
    int saldo = this.cuenta.obtenerSaldo(1001);
    return "Saldo=" + saldo;
  }

}
