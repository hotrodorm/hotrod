package org.hotrod.generator.jdbc;

public class EntityDTOs {

  private Entity abstractVO;
  private Model vo;

  public EntityDTOs(Entity abstractVO, Model vo) {
    this.abstractVO = abstractVO;
    this.vo = vo;
  }

  public Entity getAbstractVO() {
    return abstractVO;
  }

  public Model getVo() {
    return vo;
  }
}
