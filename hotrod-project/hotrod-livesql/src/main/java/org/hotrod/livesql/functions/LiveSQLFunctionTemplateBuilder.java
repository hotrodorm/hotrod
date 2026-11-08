package org.hotrod.livesql.functions;

import java.util.List;

import org.hotrod.functions.Section;

public class LiveSQLFunctionTemplateBuilder {

  public static LiveSQLFunctionTemplate build(String name, List<Section> sections) {
    TemplateSegment[] segments = sections.stream().map(s -> TemplateSegment.of(s)).toArray(TemplateSegment[]::new);
    LiveSQLFunctionTemplate t = new LiveSQLFunctionTemplate(name, segments);
    return t;
  }

}
