package app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import app.persistence.dao.TestIdentity1DAO;
import app.persistence.layout.TestIdentity1Layout;

@Component
public class BeanOps {

//  @Autowired
//  private DataSource dataSource;
//  private final JdbcTemplate jdbcTemplate;

//  public BeanOps(DataSource dataSource) {
//    this.jdbcTemplate = new JdbcTemplate(dataSource);
//  }

  @Autowired
  private TestIdentity1DAO testIdentity1DAO;

  @Transactional
  public void insertCategories(boolean abort) {
    TestIdentity1Layout c1 = new TestIdentity1Layout();
    c1.setName("Name 1");
    this.testIdentity1DAO.insertByExample(c1);

    if (abort) {
      throw new RuntimeException("--- aborting tx!");
    }

    TestIdentity1Layout c2 = new TestIdentity1Layout();
    c2.setName("Name 2");
    this.testIdentity1DAO.insertByExample(c2);

  }

//  @Transactional
//  public void insert(boolean abort) {
//    try (Connection conn = DataSourceUtils.getConnection(this.dataSource);) {
////    try (Connection conn = this.jdbcTemplate.getDataSource().getConnection()) {
//      try (PreparedStatement ps = conn.prepareStatement("insert into k2 (name) values('Line 1')")) {
//        int count = ps.executeUpdate();
//        System.out.println("count1=" + count);
//      }
//      if (abort) {
//        throw new RuntimeException("INSERT aborted.");
//      }
//      try (PreparedStatement ps = conn.prepareStatement("insert into k2 (name) values('Line 2')")) {
//        int count = ps.executeUpdate();
//        System.out.println("count2=" + count);
//      }
//    } catch (SQLException e) {
//      e.printStackTrace();
//    }
//  }
//
//  @Transactional
//  public void insert2(boolean abort) {
//    try (Connection conn = this.jdbcTemplate.getDataSource().getConnection()) {
//      int count = this.jdbcTemplate.update("insert into k2 (name) values('Line 1')");
//      System.out.println("count1=" + count);
//      if (abort) {
//        throw new RuntimeException("INSERT aborted.");
//      }
//      count = this.jdbcTemplate.update("insert into k2 (name) values('Line 2')");
//      System.out.println("count1=" + count);
//    } catch (SQLException e) {
//      e.printStackTrace();
//    }
//
//  }

}
