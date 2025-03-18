package org.hotrod.generator.jdbc;

public class EntityDTOs {

  private Layout abstractVO;
  private Model vo;

  public EntityDTOs(Layout abstractVO, Model vo) {
    this.abstractVO = abstractVO;
    this.vo = vo;
  }

  public Layout getAbstractVO() {
    return abstractVO;
  }

  public Model getVo() {
    return vo;
  }
}
