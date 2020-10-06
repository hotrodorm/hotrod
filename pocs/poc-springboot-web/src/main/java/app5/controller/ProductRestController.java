package app5.controller;

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

  @GetMapping("{id}")
  ProductVO getProduct(@PathVariable String id) {
    System.out.println("Using retrieving product " + id + "...");
    try {
      Integer iid = Integer.parseInt(id);
      return this.productDAO.selectByPK(iid);
    } catch (NumberFormatException e) {
      return null;
    }
  }

}
