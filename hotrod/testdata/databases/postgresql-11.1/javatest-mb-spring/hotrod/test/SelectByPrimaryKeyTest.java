package hotrod.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import hotrod.test.generation.EventVO;
import hotrod.test.generation.primitives.EventDAO;

@Component("selectsByPrimaryKeyTest")
public class SelectByPrimaryKeyTest {

  @Autowired
  private EventDAO dao;

  @Transactional
  public void select1() {

    System.out.println("--- Retrieving accounts...");

    EventVO e1 = dao.selectByPK(1L);
    System.out.println("e1=" + e1);

  }

}
