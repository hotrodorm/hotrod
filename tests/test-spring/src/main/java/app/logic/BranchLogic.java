package app.logic;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import app.Beans;
import app.daos.primitives.BranchDAO;
import app.daos.primitives.BranchDAO.BranchTable;

@Component
public class BranchLogic {

  @Transactional
  public void updateBranches() {

    BranchTable b = BranchDAO.newTable("b");

    int cnt = Beans.liveSQL().update(b) //
        .set(b.type, 11) //
        .where(b.id.eq(1)) //
        .execute();

    if (cnt > 0) {
      System.out.println("rolling back...");
      throw new RuntimeException("Failed to update");
    }

    int cnt2 = Beans.liveSQL().update(b) //
        .set(b.type, 12) //
        .where(b.id.eq(2)) //
        .execute();

    System.out.println("Both updates done: cnt1=" + cnt + " cnt2=" + cnt2);

  }

}
