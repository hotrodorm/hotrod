package org.hotrod.dynamicsql.assembler;

import java.util.ArrayList;
import java.util.List;

import org.hotrod.dynamicsql.segments.ChooseSegment;
import org.hotrod.dynamicsql.segments.OtherwiseSegment;
import org.hotrod.dynamicsql.segments.WhenSegment;

public class ChooseAssembler {

  private Sentence parent;
  private List<WhenSegment> whens = new ArrayList<>();
  private OtherwiseSegment otherwise = null;

  public ChooseAssembler(Sentence parent) {
    this.parent = parent;
  }

  // When Segments

  public ChooseAssembler when(String test, Sentence sentence) {
    this.whens.add(new WhenSegment(test, sentence.segments, this.parent.factory));
    return this;
  }

  public Sentence otherwise(Sentence sentence) {
    this.parent.segments
        .add(new ChooseSegment(this.whens, new OtherwiseSegment(sentence.segments, this.parent.factory)));
    return this.parent;
  }

  // end

  public Sentence end() {
    this.parent.segments.add(new ChooseSegment(this.whens, this.otherwise));
    return this.parent;
  }

}
