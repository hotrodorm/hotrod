package tests.ac;

import java.util.List;

public class ExtendedBranchVO extends BranchVO {

  private List<LeafVO> leaves;

  public List<LeafVO> getLeaves() {
    return leaves;
  }

  public void setLeaves(List<LeafVO> leaves) {
    this.leaves = leaves;
  }

}
