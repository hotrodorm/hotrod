package examples;

import java.sql.Date;
import java.sql.SQLException;

import daos.ClientDAO;

/**
 * Example 10 - Regular SQL Queries
 * 
 * @author Vladimir Alarcon
 * 
 */
public class Example10 {

  public static void main(String[] args) throws SQLException {

    System.out.println("=== Running Example 10 - Regular SQL Queries ===");
    System.out.println(" ");

    int rows;

    // 1. Update total_purchased for all clients by adding their purchases

    rows = ClientDAO.computeTotalPurchased();
    System.out.println("1. Update total_purchased. Affected rows=" + rows);

    // 2. Upgrade to VIP all clients who have bought at least N cars

    rows = ClientDAO.upgradeToVIP(2);
    System.out.println("2. Upgrade clients to VIP. Affected rows=" + rows);

    // 3. Delete clients who haven't bought anything, created long time ago, and
    // haven't referred anyone.

    rows = ClientDAO.deleteInactiveClients(Date.valueOf("2017-03-01"));
    System.out.println("3. Delete Inactive Clients. Affected rows=" + rows);

    System.out.println(" ");
    System.out.println("=== Example 10 Complete ===");

  }

}
