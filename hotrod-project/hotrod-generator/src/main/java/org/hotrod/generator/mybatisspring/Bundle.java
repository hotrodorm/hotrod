package org.hotrod.generator.mybatisspring;

public class Bundle {

  private ObjectAbstractVO abstractVO;
  private ObjectVO vo;
  private ObjectDAO dao;
  private Mapper mapper;
  private Bundle parent;

  public Bundle(final ObjectAbstractVO abstractVO, final ObjectVO vo, final ObjectDAO dao, final Mapper mapper) {
    this.abstractVO = abstractVO;
    this.vo = vo;
    this.dao = dao;
    this.mapper = mapper;
    this.parent = null;
  }

  public ObjectAbstractVO getAbstractVO() {
    return abstractVO;
  }

  public ObjectVO getVO() {
    return vo;
  }

  public ObjectDAO getDAO() {
    return dao;
  }

  public Mapper getMapper() {
    return mapper;
  }

  public Bundle getParent() {
    return parent;
  }

  public void setParent(final Bundle parent) {
    this.parent = parent;
  }

}
