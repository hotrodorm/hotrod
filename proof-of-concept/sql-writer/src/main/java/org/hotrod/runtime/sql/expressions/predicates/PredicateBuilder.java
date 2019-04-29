package org.hotrod.runtime.sql.expressions.predicates;

import java.util.ArrayList;
import java.util.List;

public class PredicateBuilder {

  private List<List<Predicate>> sections;
  private List<Predicate> currentSection;

  public PredicateBuilder(final Predicate predicate) {
    this.sections = new ArrayList<List<Predicate>>();
    or(predicate);
  }

  public void and(final Predicate predicate) {
    this.currentSection.add(predicate);
  }

  public void or(final Predicate predicate) {
    this.currentSection = new ArrayList<Predicate>();
    this.currentSection.add(predicate);
    this.sections.add(this.currentSection);
  }

  public Predicate getAssembled() {
//    System.out.println("sections=" + sections.size());
    if (this.sections.size() == 1) {
      return this.assembleSection(this.sections.get(0));
    }
    Predicate assembled = null;
    for (List<Predicate> current : this.sections) {
      Predicate c = this.assembleSection(current);
      if (assembled == null) {
        assembled = c;
      } else {
        assembled = new Or(assembled, c);
      }
    }
    return assembled;
  }

  private Predicate assembleSection(final List<Predicate> s) {
    if (s == null || s.isEmpty()) {
      return null;
    }
//    System.out.println(" + section size=" + s.size());
    Predicate assembled = null;
    for (Predicate current : s) {
      if (assembled == null) {
        assembled = current;
      } else {
        assembled = new And(assembled, current);
      }
    }
    return assembled;
  }

}
