package org.hotrod.metadata;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hotrod.config.structuredcolumns.ColumnsTag;
import org.hotrod.config.structuredcolumns.Expressions;
import org.hotrod.metadata.VOMetadata.DuplicatePropertyNameException;
import org.hotrod.metadata.VOMetadata.VOMember;
import org.hotrod.metadata.VORegistry.SelectVOClass;
import org.hotrod.metadata.VORegistry.StructuredVOAlreadyExistsException;
import org.hotrod.metadata.VORegistry.VOAlreadyExistsException;
import org.hotrod.metadata.VORegistry.VOProperty;
import org.hotrod.metadata.VORegistry.VOProperty.EnclosingTagType;
import org.hotrod.utils.ClassPackage;

public class StructuredColumnsMetadata implements Serializable {

  private static final long serialVersionUID = 1L;

  // Constants

  private static final Logger log = Logger.getLogger(StructuredColumnsMetadata.class);

  // Properties

  private ColumnsTag tag;
  private boolean isSoloVO;
  private String vo;
  private Expressions expressions;
  private List<VOMetadata> vos = new ArrayList<VOMetadata>();

  private SelectVOClass soloVOClass;

  // Constructor

  public StructuredColumnsMetadata(final ColumnsTag tag, final ClassPackage classPackage, final boolean isSoloVO,
      final String soloVOClassName, final Expressions expressions, final List<VOMetadata> vos) {
    log.debug("init");
    this.tag = tag;
    this.isSoloVO = isSoloVO;
    this.vo = soloVOClassName;
    this.expressions = expressions;
    this.vos = vos;
  }

  public void registerVOs(final ClassPackage classPackage, final VORegistry voRegistry)
      throws VOAlreadyExistsException, StructuredVOAlreadyExistsException, DuplicatePropertyNameException {

    List<VOProperty> properties = new ArrayList<VOProperty>();
    log.debug("this.vos.size()=" + this.vos.size());

    // Expressions properties

    for (StructuredColumnMetadata cm : this.expressions.getMetadata()) {
      properties.add(new VOProperty(cm.getIdentifier().getJavaMemberIdentifier(), cm, EnclosingTagType.EXPRESSIONS,
          cm.getSourceLocation()));
    }

    if (this.isSoloVO) { // solo VO

      List<VOMember> associations = new ArrayList<VOMember>();
      for (VOMetadata vo : this.vos) {
        associations.add(new VOMember(vo.getProperty(), vo.getClassPackage(), vo.getName(), vo.getSourceLocation()));
      }

      List<VOMember> collections = new ArrayList<VOMember>();

      this.soloVOClass = new SelectVOClass(classPackage, this.vo, null, properties, associations, collections,
          this.tag.getSourceLocation());
      voRegistry.addVO(this.soloVOClass);

    } else { // connected VO
      this.soloVOClass = null;
    }

    // Register sub tree

    for (VOMetadata vo : this.vos) {
      vo.registerSubTreeVOs(classPackage, voRegistry);
    }

  }

  // Getters

  public Expressions getExpressions() {
    return expressions;
  }

  public List<VOMetadata> getVOs() {
    return vos;
  }

  public SelectVOClass getSoloVOClass() {
    return soloVOClass;
  }

  public List<StructuredColumnMetadata> getColumnsMetadata() {
    return this.expressions.getMetadata();
  }

}
