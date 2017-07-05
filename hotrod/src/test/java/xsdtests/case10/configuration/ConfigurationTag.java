package xsdtests.case10.configuration;

public abstract class ConfigurationTag {

  // Properties

  protected String tagName;

  // Constructor

  protected ConfigurationTag(final String tagName) {
    this.tagName = tagName;
  }

  // Constructor just for JAXB's sake - never used
  @SuppressWarnings("unused")
  private ConfigurationTag() {
    this.tagName = "<configuration-tag>";
  }

  // Rendering

  public abstract String render();

  protected String renderTagHeader(final TagAttribute... attributes) {
    return renderHeader(false, attributes);
  }

  protected String renderEmptyTag(final TagAttribute... attributes) {
    return renderHeader(true, attributes);
  }

  private String renderHeader(final boolean emptyTag, final TagAttribute... attributes) {
    StringBuilder sb = new StringBuilder();
    sb.append("<" + this.tagName);
    for (TagAttribute a : attributes) {
      String value = a.getValue();
      if (value != null) {
        value = value.replace("&", "&amp;").replace("<", "&lt;").replace("\"", "&quot;");
        sb.append(" " + a.getName() + "=\"" + value + "\"");
      }
    }
    sb.append(emptyTag ? " />" : ">");
    return sb.toString();
  }

  protected String renderTagFooter() {
    StringBuilder sb = new StringBuilder();
    sb.append("</" + this.tagName + ">");
    return sb.toString();
  }

}
