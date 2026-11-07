package org.hotrod.livesql.functions;

public class LiveSQLFunctionTemplateBuilder {

  private LiveSQLFunctionTemplate template;

  public static LiveSQLFunctionTemplateBuilder begin(String templateName) {
    LiveSQLFunctionTemplateBuilder b = new LiveSQLFunctionTemplateBuilder();
    b.template = new LiveSQLFunctionTemplate(templateName);
    return b;
  }

  public LiveSQLFunctionTemplateBuilder literal(String literal) {
    this.template.add(TemplateLiteral.of(literal));
    return this;
  }

  public LiveSQLFunctionTemplateBuilder arg() {
    this.template.add(TemplateArg.of(this.template));
    return this;
  }

  public EndPhaseOfTemplateBuilder vararg(String firstPrefix, String prefix, String suffix) {
    this.template.add(TemplateVarArg.of(this.template, firstPrefix, prefix, suffix));
    return new EndPhaseOfTemplateBuilder(this.template);
  }

  public LiveSQLFunctionTemplate end() {
    this.template.seal();
    return this.template;
  }

  public static class EndPhaseOfTemplateBuilder {

    private LiveSQLFunctionTemplate template;

    EndPhaseOfTemplateBuilder(LiveSQLFunctionTemplate template) {
      this.template = template;
    }

    public EndPhaseOfTemplateBuilder literal(String literal) {
      this.template.add(TemplateLiteral.of(literal));
      return this;
    }

    public LiveSQLFunctionTemplate end() {
      this.template.seal();
      return this.template;
    }

  }

}
