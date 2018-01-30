package automated;

import java.util.Arrays;
import java.util.List;

import org.hotrod.eclipseplugin.utils.Correlator;
import org.hotrod.eclipseplugin.utils.Correlator.CorrelatedEntry;

import junit.framework.TestCase;

public class CorrelatorTests extends TestCase {

  public CorrelatorTests(final String txt) {
    super(txt);
  }

  public void testSorted() {

    {
      List<String> left = null;
      List<String> right = null;
      List<CorrelatedEntry<String>> ce = Correlator.correlateSorted(left, right);
      assertEquals(0, ce.size());
    }

    {
      List<String> left = Arrays.asList(new String[] {});
      List<String> right = null;
      List<CorrelatedEntry<String>> ce = Correlator.correlateSorted(left, right);
      assertEquals(0, ce.size());
    }

    {
      List<String> left = null;
      List<String> right = Arrays.asList(new String[] {});
      List<CorrelatedEntry<String>> ce = Correlator.correlateSorted(left, right);
      assertEquals(0, ce.size());
    }

    {
      List<String> left = Arrays.asList(new String[] {});
      List<String> right = Arrays.asList(new String[] {});
      List<CorrelatedEntry<String>> ce = Correlator.correlateSorted(left, right);
      assertEquals(0, ce.size());
    }

    {
      List<String> left = Arrays.asList(new String[] { "abc" });
      List<String> right = Arrays.asList(new String[] {});
      List<CorrelatedEntry<String>> ce = Correlator.correlateSorted(left, right);
      assertEquals(1, ce.size());
      assertEquals("abc", ce.get(0).getLeft());
      // display("ce.get(0).getRight()=" + ce.get(0).getRight());
      assertEquals(null, ce.get(0).getRight());
    }

    {
      List<String> left = Arrays.asList(new String[] {});
      List<String> right = Arrays.asList(new String[] { "def" });
      List<CorrelatedEntry<String>> ce = Correlator.correlateSorted(left, right);
      assertEquals(1, ce.size());
      assertEquals(null, ce.get(0).getLeft());
      assertEquals("def", ce.get(0).getRight());
    }

    {
      List<String> left = Arrays.asList(new String[] { "abc" });
      List<String> right = Arrays.asList(new String[] { "abc" });
      List<CorrelatedEntry<String>> ce = Correlator.correlateSorted(left, right);
      assertEquals(1, ce.size());
      assertEquals("abc", ce.get(0).getLeft());
      assertEquals("abc", ce.get(0).getRight());
    }

    {
      List<String> left = Arrays.asList(new String[] { "abc" });
      List<String> right = Arrays.asList(new String[] { "def" });
      List<CorrelatedEntry<String>> ce = Correlator.correlateSorted(left, right);
      assertEquals(2, ce.size());
      assertEquals("abc", ce.get(0).getLeft());
      assertEquals(null, ce.get(0).getRight());
      assertEquals(null, ce.get(1).getLeft());
      assertEquals("def", ce.get(1).getRight());
    }

    {
      List<String> left = Arrays.asList(new String[] { "def" });
      List<String> right = Arrays.asList(new String[] { "abc" });
      List<CorrelatedEntry<String>> ce = Correlator.correlateSorted(left, right);
      assertEquals(2, ce.size());
      assertEquals(null, ce.get(0).getLeft());
      assertEquals("abc", ce.get(0).getRight());
      assertEquals("def", ce.get(1).getLeft());
      assertEquals(null, ce.get(1).getRight());
    }

    {
      List<String> left = Arrays.asList(new String[] { "def" });
      List<String> right = Arrays.asList(new String[] { "abc", "ghi" });
      List<CorrelatedEntry<String>> ce = Correlator.correlateSorted(left, right);
      assertEquals(3, ce.size());
      assertEquals(null, ce.get(0).getLeft());
      assertEquals("abc", ce.get(0).getRight());
      assertEquals("def", ce.get(1).getLeft());
      assertEquals(null, ce.get(1).getRight());
      assertEquals(null, ce.get(2).getLeft());
      assertEquals("ghi", ce.get(2).getRight());
    }

  }

  // Utilities

  public void display(final String txt) {
    System.out.println("[Correlator Tests] " + txt);
  }

}
