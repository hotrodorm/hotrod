package examples;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.hotrod.runtime.tx.TxManager;

import daos.ClientVO;
import daos.primitives.ClientDAO;

/**
 * Example 22 - Including Existing MyBatis Mappers
 * 
 * @author Vladimir Alarcon
 * 
 */
public class Example22 {

  public static void main(String[] args) throws SQLException {

    System.out.println("=== Running Example 22 - Including Existing MyBatis Mappers ===");

    // 1. Use a standard MyBatis mapper query
    // Example: Search clients from state VA

    TxManager txm = ClientDAO.getTxManager();
    SqlSession sqlSession = txm.getSqlSession();
    List<ClientVO> clients = sqlSession.selectList("daos.custom.search.searchClient", "CA");
    System.out.println("1. Use a standard MyBatis mapper query. Clients found=" + clients.size());
    for (ClientVO c : clients) {
      System.out.println(" ID=" + c.getId() + " - Name: " + c.getName());
    }

    System.out.println(" ");
    System.out.println("=== Example 22 Complete ===");

  }

}
