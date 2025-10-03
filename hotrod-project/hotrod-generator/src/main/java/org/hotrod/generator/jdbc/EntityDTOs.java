package org.hotrod.generator.jdbc;

public class EntityDTOs {

  private LayoutWriter abstractVO;
  private ModelWriter vo;

  public EntityDTOs(LayoutWriter abstractVO, ModelWriter vo) {
    this.abstractVO = abstractVO;
    this.vo = vo;
  }

  public LayoutWriter getAbstractVO() {
    return abstractVO;
  }

  public ModelWriter getVo() {
    return vo;
  }
}
