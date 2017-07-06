package xsdtests.case00;

import java.util.List;

import xsdtests.case10.configuration.TagAttribute;

// This renderer is incomplete.
// * It does not escape attribute values.
// * It does not escape text content.

public class RendererHelper {

  public static void render(final String currentTag, final StringBuilder sb, final List<Object> content,
      final TagAttribute... attributes) {

    renderHeader(currentTag, sb, attributes);

    if (content != null) {
      for (Object obj : content) {
        if (obj == null) {
          sb.append(obj);
        } else {
          try {
            String s = (String) obj;
            sb.append(s);
          } catch (ClassCastException e) {
            try {
              RendereableTag t = (RendereableTag) obj;
              t.render(sb);
            } catch (ClassCastException e2) {
              sb.append("[Could not render object of class '" + obj.getClass().getName() + "' inside the tag <"
                  + currentTag + "> ]");
            }
          }
        }
      }
    }

    renderFooter(currentTag, sb);

  }

  public static void render(final String currentTag, final StringBuilder sb, final String content,
      final TagAttribute... attributes) {

    renderHeader(currentTag, sb, attributes);

    if (content != null) {
      sb.append(content);
    }

    renderFooter(currentTag, sb);

  }

  public static void render(final String currentTag, final StringBuilder sb, final TagAttribute... attributes) {

    renderHeader(currentTag, sb, attributes);

    renderFooter(currentTag, sb);

  }

  // Helpers

  public static void renderHeader(final String currentTag, final StringBuilder sb, final TagAttribute... attributes) {
    sb.append("<" + currentTag);
    for (TagAttribute a : attributes) {
      if (a.getValue() != null) {
        sb.append(" " + a.getName() + "=\"" + a.getValue() + "\"");
      }
    }
    sb.append(">");
  }

  public static void renderFooter(final String currentTag, final StringBuilder sb) {
    sb.append("</" + currentTag + ">");
  }

}
