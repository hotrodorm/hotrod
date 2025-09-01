package automated;

import java.util.Arrays;
import java.util.List;

import org.hotrod.eclipseplugin.utils.ClassPathEncoder;

import junit.framework.TestCase;

public class EncoderTests extends TestCase {

  public EncoderTests(final String txt) {
    super(txt);
  }

  public void testEncode() {

    {
      List<String> paths = Arrays.asList(new String[] { "abc", "def" });
      String enc = ClassPathEncoder.encode(paths);
      assertEquals("abc:def", enc);
    }

    {
      List<String> paths = Arrays.asList(new String[] { "", "" });
      String enc = ClassPathEncoder.encode(paths);
      assertEquals(":", enc);
    }

    {
      List<String> paths = Arrays.asList(new String[] { "abc", "" });
      String enc = ClassPathEncoder.encode(paths);
      assertEquals("abc:", enc);
    }

    {
      List<String> paths = Arrays.asList(new String[] { ":", ":" });
      String enc = ClassPathEncoder.encode(paths);
      // display("enc=" + enc);
      assertEquals("\\::\\:", enc);
    }

  }

  public void testDecode() {

    {
      List<String> paths = ClassPathEncoder.decode("abc:def");
      assertEquals(2, paths.size());
      assertEquals("abc", paths.get(0));
      assertEquals("def", paths.get(1));
    }

    {
      List<String> paths = ClassPathEncoder.decode(":def");
      assertEquals(2, paths.size());
      assertEquals("", paths.get(0));
      assertEquals("def", paths.get(1));
    }

    {
      List<String> paths = ClassPathEncoder.decode("\\::\\:");
      assertEquals(2, paths.size());
      assertEquals(":", paths.get(0));
      assertEquals(":", paths.get(1));
    }

  }

  // Utilities

  public void display(final String txt) {
    System.out.println(txt);
  }

}
