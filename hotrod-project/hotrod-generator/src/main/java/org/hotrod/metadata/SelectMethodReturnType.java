package org.hotrod.metadata;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.hotrod.config.AbstractConfigurationTag;
import org.hotrod.config.JDBCTag;
import org.hotrod.config.SelectMethodTag.ResultSetMode;
import org.hotrod.exceptions.ErrorMessageException;
import org.hotrod.exceptions.InvalidIdentifierException;
import org.hotrod.metadata.VOMetadata.DuplicatePropertyNameException;
import org.hotrod.metadata.VOMetadata.VOMember;
import org.hotrod.metadata.VORegistry.SelectVOClass;
import org.hotrod.metadata.VORegistry.VOProperty;
import org.hotrod.metadata.VORegistry.VOProperty.EnclosingTagType;
import org.hotrod.utils.ClassPackage;

public class SelectMethodReturnType implements Serializable {

  private static final long serialVersionUID = 1L;

  private static final Logger log = Logger.getLogger(SelectMethodReturnType.class.getName());

  private SelectMethodMetadata sm;
  private ClassPackage fragmentPackage;
  private JDBCTag jdbcTag;

  private ClassPackage layoutPackage;
  private ClassPackage modelPackage;

  private SelectVOClass soloVO;
  private SelectVOClass abstractSoloVO;
  private transient VOMetadata connectedVO;

  private ResultSetMode mode;

  public SelectMethodReturnType(final SelectMethodMetadata sm, final ClassPackage fragmentPackage,
      final AbstractConfigurationTag tag, final JDBCTag jdbcTag) throws ErrorMessageException {

    this.sm = sm;
    this.fragmentPackage = fragmentPackage;
    this.jdbcTag = jdbcTag;

    this.layoutPackage = this.jdbcTag.getLayoutPackage(this.fragmentPackage);
    this.modelPackage = this.jdbcTag.getModelPackage(this.fragmentPackage);

    if (sm.isStructured()) { // graph columns

      StructuredColumnsMetadata structCols = sm.getStructuredColumns();
      this.mode = sm.getResultSetMode();
      if (structCols.getSoloVOClass() == null) { // it's a connected VO
        log.finer(">>> it's a connected VO (1)");
        this.soloVO = null;
        this.abstractSoloVO = null;

        this.connectedVO = structCols.getVOs().get(0);

      } else { // solo VO from a <columns> tag
        log.info(">>> solo VO from a <columns> tag (2)");
        List<VOMember> associations = new ArrayList<VOMember>();
        for (VOMetadata vo : sm.getStructuredColumns().getVOs()) {
          VOMember m;
          try {
            m = new VOMember(vo.getProperty(), vo.getClassPackage(), vo.getName(), vo.getTag());
          } catch (InvalidIdentifierException e) {
            String msg = "Invalid property '" + vo.getProperty() + "':" + e.getMessage();
            throw new ErrorMessageException(tag, msg);
          }
          associations.add(m);
        }
        this.soloVO = structCols.getSoloVOClass();
        this.connectedVO = null;
      }

    } else { // solo VO from non-graph columns
      log.finer(">>> solo VO (3)");

      List<VOProperty> properties = new ArrayList<VOProperty>();
      for (ColumnMetadata cm : sm.getNonStructuredColumns()) {
        StructuredColumnMetadata m = new StructuredColumnMetadata(cm, "entityPrefix2", "columnAlias", false, null);
        VOProperty p = new VOProperty(cm.getId().getJavaMemberName(), m, EnclosingTagType.NON_STRUCTURED_SELECT,
            sm.getTag());
        properties.add(p);
      }

      this.mode = sm.getResultSetMode();

      if (sm.getTag().belongsToEntity()) {

        this.abstractSoloVO = null;
        this.soloVO = null;

      } else {

        List<VOMember> associations = new ArrayList<VOMember>();
        List<VOMember> collections = new ArrayList<VOMember>();
        try {

          this.abstractSoloVO = new SelectVOClass(this.fragmentPackage, this.layoutPackage, sm.getAbstractVOClassName(),
              null, null, properties, associations, collections, tag);
          this.soloVO = new SelectVOClass(this.fragmentPackage, this.modelPackage, sm.getVOClassName(), null,
              sm.getTag().getImplementsClasses(), properties, associations, collections, tag);

        } catch (DuplicatePropertyNameException e) {
          // swallow this exception
        }
        this.connectedVO = null;

        log.finer(">>> sm.getVOClassName()=" + sm.getVOClassName() + " sm.getAbstractVOClassName()="
            + sm.getAbstractVOClassName());
//        log.finer("this.soloVO.getName()=" + (this.soloVO == null ? "null" : this.soloVO.getName())
//            + " this.connectedVO.getName()=" + (this.connectedVO == null ? "null" : this.connectedVO.getName()));
        log.finer("this.soloVO.getName()=" + (this.soloVO == null ? "null" : this.soloVO.getName())
            + " this.connectedVO.getName()=" + ("null"));

      }

    }

  }

  public SelectVOClass getSoloVO() {
    return soloVO;
  }

  public SelectVOClass getAbstractSoloVO() {
    return this.abstractSoloVO;
  }

  public VOMetadata getConnectedVO() {
    return connectedVO;
  }

  public ResultSetMode getMode() {
    return this.mode;
  }

  // Simpler methods

  private ClassPackage getReturnVOPackage() { // primitives.accounting
//    log.info("this.sm.entityVOs=" + this.sm.entityVOs);
    if (this.sm.getEntityVOs() != null) {
      return this.sm.getEntityVOs().getVo().getClassPackage();
    }
    return this.soloVO != null ? this.soloVO.getClassPackage() : this.connectedVO.getClassPackage();
  }

  public String getBaseReturnVOClass() { // AccountPersonVO
    if (this.sm.getEntityVOs() != null) {
      return this.sm.getEntityVOs().getVo().getClassName();
    }
    return this.soloVO != null ? this.soloVO.getName() : this.connectedVO.getName();
  }

  public String getReturnType() { // AccountPersonVO, List<AccountPersonVO>, Cursor<AccountPersonVO>
    switch (this.mode) {
    case LIST:
      return "List<" + getBaseReturnVOClass() + ">";
    case CURSOR:
      return "Cursor<" + getBaseReturnVOClass() + ">";
    default:
      return getBaseReturnVOClass(); // single-row
    }
  }

  public String getBaseReturnVOFullClassName() { // primitives.accounting.AccountPersonVO
    if (this.sm.getEntityVOs() != null) {
      return this.sm.getEntityVOs().getVo().getFullClassName();
    } else {
      return this.getReturnVOPackage().getFullClassName(getBaseReturnVOClass());
    }
  }

}