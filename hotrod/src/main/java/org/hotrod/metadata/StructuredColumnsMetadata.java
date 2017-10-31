package org.hotrod.metadata;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hotrod.config.structuredcolumns.ColumnsTag;
import org.hotrod.metadata.VOMetadata.DuplicatePropertyNameException;
import org.hotrod.metadata.VORegistry.StructuredVOAlreadyExistsException;
import org.hotrod.metadata.VORegistry.StructuredVOClass;
import org.hotrod.metadata.VORegistry.VOAlreadyExistsException;
import org.hotrod.utils.ClassPackage;

public class StructuredColumnsMetadata {

  // Constants

  private static final Logger log = Logger.getLogger(StructuredColumnsMetadata.class);

  // Properties

  private ColumnsTag tag;
  private boolean innerVOResult;
  private String vo;
  private List<ExpressionsMetadata> expressions = new ArrayList<ExpressionsMetadata>();
  private List<VOMetadata> vos = new ArrayList<VOMetadata>();

  private StructuredVOClass voClass;

  // Constructor

  public StructuredColumnsMetadata(final ColumnsTag tag, final ClassPackage classPackage, final boolean innerVOResult,
      final String vo, final List<ExpressionsMetadata> expressions, final List<VOMetadata> vos) {
    this.tag = tag;
    this.innerVOResult = innerVOResult;
    this.vo = vo;
    this.expressions = expressions;
    this.vos = vos;
  }

  public void registerVOs(final ClassPackage classPackage, final VORegistry voRegistry)
      throws VOAlreadyExistsException, StructuredVOAlreadyExistsException, DuplicatePropertyNameException {
    // log.info(":: main <column> tag: this.innerVOResult=" +
    // this.innerVOResult);

    Set<String> members = new HashSet<String>();
    List<StructuredColumnMetadata> columns = compileColumns(members);

    if (this.innerVOResult) {
      this.voClass = null;
    } else {
      this.voClass = new StructuredVOClass(classPackage, this.vo, null, columns, this.tag.getSourceLocation());
      voRegistry.addVO(this.voClass);
    }

    for (VOMetadata vo : this.vos) {
      if (members.contains(vo.getProperty())) {
        throw new DuplicatePropertyNameException(vo.getProperty(), vo.getSourceLocation());
      }
      members.add(vo.getProperty());
      vo.registerVOs(classPackage, voRegistry);
    }

  }

  private List<StructuredColumnMetadata> compileColumns(final Set<String> members)
      throws DuplicatePropertyNameException {
    List<StructuredColumnMetadata> columns = new ArrayList<StructuredColumnMetadata>();
    for (ExpressionsMetadata em : this.getExpressions()) {
      for (StructuredColumnMetadata cm : em.getColumns()) {
        String memberName = cm.getIdentifier().getJavaMemberIdentifier();
        if (members.contains(memberName)) {
          throw new DuplicatePropertyNameException(memberName, em.getSourceLocation());
        }
        columns.add(cm);
      }
    }
    return columns;
  }

  // Getters

  public List<ExpressionsMetadata> getExpressions() {
    return expressions;
  }

  public List<VOMetadata> getVOs() {
    return vos;
  }

  public StructuredVOClass getVOClass() {
    return voClass;
  }

  public List<ColumnMetadata> getExpressionsColumns() {
    List<ColumnMetadata> columns = new ArrayList<ColumnMetadata>();
    for (ExpressionsMetadata exp : this.expressions) {
      columns.addAll(exp.getColumns());
    }
    return columns;
  }

  public List<StructuredColumnMetadata> getColumnsMetadata() {
    List<StructuredColumnMetadata> m = new ArrayList<StructuredColumnMetadata>();
    for (ExpressionsMetadata em : this.expressions) {
      m.addAll(em.getColumns());
    }
    return m;
  }

}
