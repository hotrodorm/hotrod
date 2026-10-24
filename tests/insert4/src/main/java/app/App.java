package app;

import java.sql.Timestamp;

import org.hotrod.runtime.livesql.LiveSQL;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import app.daos.Product;
import app.daos.primitives.ProductDAO;

@Configuration
@SpringBootApplication
@ComponentScan
@ComponentScan(basePackageClasses = LiveSQL.class)
@MapperScan(basePackageClasses = LiveSQL.class)
public class App {

  @Autowired
  private ProductDAO productDAO;

  public static void main(String[] args) {
    SpringApplication.run(App.class, args);
  }

  @Bean
  public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
    return args -> {
      demoCRUD();
    };
  }

//insert into product (id) values (100);
//insert into product (id, created) values (200, '2024-01-02 12:34:56');
//insert into product (id, updated) values (300, '2024-05-23 12:34:56');
//insert into product (id, updated) values (400, null);
//insert into product (id, local_code) values (500, 486933);

  private void demoCRUD() {
    Product p = null;
    Product i = null;
    long count = 0;

    this.productDAO.delete(new Product());

    p = new Product();
    p.setId(100);
    i = this.productDAO.insert(p);
    count = count + (i != null ? 1 : 0);

    p = new Product();
    p.setId(200);
    p.setCreated(Timestamp.valueOf("2024-01-02 12:34:56"));
    i = this.productDAO.insert(p);
    count = count + (i != null ? 1 : 0);

    p = new Product();
    p.setId(300);
    p.setUpdated(Timestamp.valueOf("2024-05-23 12:34:56"));
    i = this.productDAO.insert(p);
    count = count + (i != null ? 1 : 0);

    p = new Product();
    p.setId(400);
    p.setUpdated(null);
    i = this.productDAO.insert(p);
    count = count + (i != null ? 1 : 0);

    p = new Product();
    p.setId(500);
    p.setLocalCode(486933);
    i = this.productDAO.insert(p);
    count = count + (i != null ? 1 : 0);

    System.out.println("Inserted " + count + " rows.");

  }

}
