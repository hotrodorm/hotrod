package simpletests;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import primitives.AbcDAOPrimitives.AbcDAOOrderBy;
import simpletests.dao.AbcDbStructurePOC;
import dao.AbcDAO;

public class Test001 {

  public static void main(final String[] args) throws IOException, SQLException {

    // 1. Get the session factory

    String resource = "testdata/mybatis-config.xml";
    InputStream is = Resources.getResourceAsStream(resource);
    // File f = new File(resource);
    // InputStream is = new FileInputStream(f);
    SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder()
        .build(is);

    // selectOne(sqlSessionFactory);

    selectOrdered();

    // multipleInsert(sqlSessionFactory);

  }

  private static void multipleInsert(final SqlSessionFactory sqlSessionFactory) {
    AbcDbStructurePOC a1 = assembleAbc(101);
    AbcDbStructurePOC a2 = assembleAbc(102);

    SqlSession session1 = sqlSessionFactory.openSession();
    SqlSession session2 = sqlSessionFactory.openSession();

    try {
      session1.insert("simpletests.dao.insertAbc", a1);
      session2.insert("simpletests.dao.insertAbc", a2);
      session2.commit();

      // session1.rollback();

    } finally {
      session1.close();
      session2.close();
    }

    System.out.println(a1 + " " + a2);
  }

  private static void selectOrdered() throws SQLException {
    AbcDAO filter = new AbcDAO();
    filter.setDescription("lorem");
    for (AbcDAO abc : filter.select(AbcDAOOrderBy.CHAPTER$DESC)) {
      System.out.println("abc=" + abc);
    }
    // for (AbcDAO abc : filter.select()) {
    // System.out.println("abc=" + abc);
    // }
  }

  private static AbcDbStructurePOC assembleAbc(final int id) {
    AbcDbStructurePOC abc = new AbcDbStructurePOC();
    abc.setId(id);
    abc.setIdn(id);
    abc.setVolume("" + id);
    abc.setChapter(id);
    abc.setSection(id);
    abc.setPage(id);
    abc.setDescription("" + id);
    return abc;
  }

  private static void selectOne(final SqlSessionFactory sqlSessionFactory) {
    SqlSession session = sqlSessionFactory.openSession();
    try {
      AbcDbStructurePOC abc = session.selectOne("simpletests.dao.selectAbc", 2);
      System.out.println("abc=" + abc);
    } finally {
      session.close();
    }
  }

}
