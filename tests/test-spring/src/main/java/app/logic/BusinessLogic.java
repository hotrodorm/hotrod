package app.logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BusinessLogic {

  @Autowired
  private BranchLogic branchLogic;

  public void updateBranches() {
    this.branchLogic.updateBranches();
  }

}
