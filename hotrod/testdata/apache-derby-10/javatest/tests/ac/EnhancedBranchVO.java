package tests.ac;

import java.util.List;

public class EnhancedBranchVO extends BranchVO {

  private TreeVO tree;
  private List<FlowerVO> flowers;

  public TreeVO getTree() {
    return tree;
  }

  public void setTree(TreeVO tree) {
    this.tree = tree;
  }

  public List<FlowerVO> getFlowers() {
    return flowers;
  }

  public void setFlowers(List<FlowerVO> flowers) {
    this.flowers = flowers;
  }

}
