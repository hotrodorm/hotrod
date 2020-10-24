package examples.livesql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import app5.persistence.AccountVO;
import app5.persistence.primitives.AccountDAO;

@Component("selectsByPK")
public class SelectByPK {

  @Autowired
  private AccountDAO dao;

  @Transactional
  public void insertSelect() {

    System.out.println("--- Retrieving accounts...");

    AccountVO a1 = dao.selectByPK(1234004);
    System.out.println("a1=" + a1);

    AccountVO a2 = dao.selectByPK(1234005);
    System.out.println("a2=" + a2);

    AccountVO a3 = dao.selectByPK(1234001);
    System.out.println("a3=" + a3);

  }

}
