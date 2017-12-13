package com.myapp1.controller;

import org.apache.ibatis.session.SqlSession;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public class Controller1 {

  // Beans

  @SuppressWarnings("unused")
  private SqlSession sqlSession;
  private Controller2 controller2;

  // Bean setter

  public void setSqlSession(final SqlSession sqlSession) {
    this.sqlSession = sqlSession;
  }

  public void setController2(final Controller2 controller2) {
    this.controller2 = controller2;
  }

  // Behavior

  @Transactional(propagation = Propagation.REQUIRED)
  public void transferir(final Integer desdeId, final Integer haciaId, final Integer monto) {
    this.controller2.abonar(haciaId, monto);
    this.controller2.abonar(desdeId, -monto);
  }

}
