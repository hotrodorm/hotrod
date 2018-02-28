package tests.ac;

import java.util.List;

public class ExtendedBranchVO extends BranchVO {

  private List<LeafVO> leaves;
  private BranchTypeVO branchType;

  public List<LeafVO> getLeaves() {
    return leaves;
  }

  public void setLeaves(List<LeafVO> leaves) {
    this.leaves = leaves;
  }

  public BranchTypeVO getBranchType() {
    return branchType;
  }

  public void setBranchType(BranchTypeVO branchType) {
    this.branchType = branchType;
  }

}
