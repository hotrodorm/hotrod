package tests;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.session.SqlSession;

import hotrod.test.generation.AccountVO;
import hotrod.test.generation.primitives.AccountDAO;
import hotrod.test.generation.primitives.TypesOtherDAO;
import tests.ac.EnhancedBranchVO;
import tests.ac.EnhancedLeafVO;
import tests.ac.ExtendedBranchVO;
import tests.ac.ExtendedTreeVO;
import tests.ac.FlowerVO;
import tests.ac.LeafVO;
import tests.ac.TreeVO;

public class SelectTests {

  public static void main(final String[] args) throws SQLException {
    // showAccounts();
    // promotion74();
    getSequence();

    // selectAssociation();
    // selectAssociationMixed();
    // selectCollection();
    // selectCollectionMixed();

    // StructuredTests st = new StructuredTests();
    // st.run();

  }

  private static void showAccounts() throws SQLException {
    System.out.println("Show all accounts:");
    System.out.println("==================");
    for (AccountVO a : AccountDAO.selectByExample(new AccountVO())) {
      System.out.println("a=" + a);
    }
  }

  private static void promotion74() throws SQLException {
    System.out.println("Apply Promotion 74:");
    System.out.println("===================");
    int rows = TypesOtherDAO.applyAccountPromotion74(4, null, null, 200, null, "CHK");
    System.out.println(rows + " row(s) updated.");
  }

  private static void getSequence() throws SQLException {
    System.out.println("Get Sequence:");
    System.out.println("=============");
    long seq = TypesOtherDAO.getCodeSequence();
    System.out.println("-> seq " + seq);
  }

  private static void selectAssociation() throws SQLException {
    System.out.println("Enhanced Leaves (associations):");
    System.out.println("===============================");
    SqlSession session = AccountDAO.getTxManager().getSqlSession();
    List<EnhancedLeafVO> leaves = session.selectList("hotrod.test.generation.primitives.account.selectEnhancedLeaf");

    for (EnhancedLeafVO leaf : leaves) {
      EnhancedBranchVO b = leaf.getBranch();
      if (b != null) {
        b.setName(b.getName() + " from leaf " + leaf.getId());
      }
    }

    for (EnhancedLeafVO leaf : leaves) {
      System.out.println("leaf=" + leaf);
      EnhancedBranchVO b = leaf.getBranch();
      if (b != null) {
        System.out.println("  -> branch=" + b);
        TreeVO t = b.getTree();
        if (t != null) {
          System.out.println("    -> tree=" + t);
        }
      }
    }

  }

  private static void selectAssociationMixed() throws SQLException {
    System.out.println("Enhanced Leaves (mixed associations):");
    System.out.println("=====================================");
    SqlSession session = AccountDAO.getTxManager().getSqlSession();
    List<EnhancedLeafVO> leaves = session
        .selectList("hotrod.test.generation.primitives.account.selectEnhancedLeafMixed");

    for (EnhancedLeafVO leaf : leaves) {
      System.out.println("leaf=" + leaf);
      EnhancedBranchVO b = leaf.getBranch();
      if (b != null) {
        System.out.println("  -> branch=" + b);
        TreeVO t = b.getTree();
        if (t != null) {
          System.out.println("    -> tree=" + t);
        }
        for (FlowerVO f : b.getFlowers()) {
          System.out.println("    -> flower=" + f);
        }
      }
    }

  }

  private static void selectCollection() throws SQLException {
    System.out.println("Trees (collections):");
    System.out.println("====================");
    SqlSession session = AccountDAO.getTxManager().getSqlSession();
    List<ExtendedTreeVO> trees = session.selectList("hotrod.test.generation.primitives.account.selectExtendedTree");

    for (ExtendedTreeVO tree : trees) {
      System.out.println("tree=" + tree);
      for (ExtendedBranchVO branch : tree.getBranches()) {
        System.out.println(" -> branch=" + branch);
        for (LeafVO leaf : branch.getLeaves()) {
          System.out.println("     -> leaf=" + leaf);
        }
      }
    }

  }

  private static void selectCollectionMixed() throws SQLException {
    System.out.println("Trees (mixed collections):");
    System.out.println("==========================");
    SqlSession session = AccountDAO.getTxManager().getSqlSession();
    List<ExtendedTreeVO> trees = session
        .selectList("hotrod.test.generation.primitives.account.selectExtendedTreeMixed");

    for (ExtendedTreeVO tree : trees) {
      System.out.println("tree=" + tree);
      for (ExtendedBranchVO branch : tree.getBranches()) {
        System.out.println(" -> branch=" + branch);
        if (branch.getBranchType() != null) {
          System.out.println("     -> type=" + branch.getBranchType());
        }
        for (LeafVO leaf : branch.getLeaves()) {
          System.out.println("     -> leaf=" + leaf);
        }
      }
    }

  }

}
