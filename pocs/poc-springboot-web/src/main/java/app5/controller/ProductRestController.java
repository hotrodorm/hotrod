package app5.controller;

import org.hotrod.runtime.livesql.LiveSQL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app5.persistence.ProductVO;
import app5.persistence.primitives.ProductDAO;

@RequestMapping("/product")
@RestController
public class ProductRestController {

  @Autowired
  private ProductDAO productDAO;

  @Autowired
  private LiveSQL sql;

  @GetMapping("{id}")
  ProductVO getProduct(@PathVariable String id) {
    System.out.println("Retrieving product " + id + "...");
    try {
      Long iid = Long.parseLong(id);
      return this.productDAO.selectByPK(iid);
    } catch (NumberFormatException e) {
      return null;
    }
  }

  @GetMapping("produceerror")
  void getProductError() {
    sql.select(sql.currentDateTime().between(sql.currentTime(), sql.currentDateTime())).execute();
  }

}
