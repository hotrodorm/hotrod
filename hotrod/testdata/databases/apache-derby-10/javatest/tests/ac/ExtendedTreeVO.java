package tests.ac;

import java.util.List;

public class ExtendedTreeVO extends TreeVO {

  private List<ExtendedBranchVO> branches;

  public List<ExtendedBranchVO> getBranches() {
    return branches;
  }

  public void setBranches(List<ExtendedBranchVO> branches) {
    this.branches = branches;
  }

}
