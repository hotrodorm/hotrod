package automated;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.hotrod.eclipseplugin.utils.Correlator;
import org.hotrod.eclipseplugin.utils.Correlator.CorrelatedEntry;

import junit.framework.TestCase;

public class CorrelatorTests extends TestCase {

  public CorrelatorTests(final String txt) {
    super(txt);
  }

  public void testSortedComparable() {

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

  public void testSortedComparator() {

    Comparator<Row> comp = Row.getComparator();

    {
      List<Row> left = null;
      List<Row> right = null;
      List<CorrelatedEntry<Row>> ce = Correlator.correlateSorted(left, right, comp);
      assertEquals(0, ce.size());
    }

    {
      List<Row> left = Arrays.asList(new Row[] {});
      List<Row> right = null;
      List<CorrelatedEntry<Row>> ce = Correlator.correlateSorted(left, right, comp);
      assertEquals(0, ce.size());
    }

    {
      List<Row> left = null;
      List<Row> right = Arrays.asList(new Row[] {});
      List<CorrelatedEntry<Row>> ce = Correlator.correlateSorted(left, right, comp);
      assertEquals(0, ce.size());
    }

    {
      List<Row> left = Arrays.asList(new Row[] {});
      List<Row> right = Arrays.asList(new Row[] {});
      List<CorrelatedEntry<Row>> ce = Correlator.correlateSorted(left, right, comp);
      assertEquals(0, ce.size());
    }

    {
      List<Row> left = Arrays.asList(new Row[] { null }); // TODO: fix
      List<Row> right = Arrays.asList(new Row[] {});
      List<CorrelatedEntry<Row>> ce = Correlator.correlateSorted(left, right, comp);
      assertEquals(1, ce.size());
      assertEquals(null, ce.get(0).getLeft());
      assertEquals(null, ce.get(0).getRight());
    }

    {
      List<Row> left = Arrays.asList(new Row[] { new Row(1, "abc") });
      List<Row> right = Arrays.asList(new Row[] {});
      List<CorrelatedEntry<Row>> ce = Correlator.correlateSorted(left, right, comp);
      assertEquals(1, ce.size());
      assertTrue(ce.get(0).getLeft().is(1, "abc"));
      assertEquals(null, ce.get(0).getRight());
    }

    {
      List<Row> left = Arrays.asList(new Row[] {});
      List<Row> right = Arrays.asList(new Row[] { new Row(2, "def") });
      List<CorrelatedEntry<Row>> ce = Correlator.correlateSorted(left, right, comp);
      assertEquals(1, ce.size());
      assertEquals(null, ce.get(0).getLeft());
      assertTrue(ce.get(0).getRight().is(2, "def"));
    }

    {
      List<Row> left = Arrays.asList(new Row[] { new Row(1, "abcL") });
      List<Row> right = Arrays.asList(new Row[] { new Row(1, "abcD") });
      List<CorrelatedEntry<Row>> ce = Correlator.correlateSorted(left, right, comp);
      assertEquals(1, ce.size());
      assertTrue(ce.get(0).getLeft().is(1, "abcL"));
      assertTrue(ce.get(0).getRight().is(1, "abcD"));
    }

    {
      List<Row> left = Arrays.asList(new Row[] { new Row(1, "abc") });
      List<Row> right = Arrays.asList(new Row[] { new Row(2, "def") });
      List<CorrelatedEntry<Row>> ce = Correlator.correlateSorted(left, right, comp);
      assertEquals(2, ce.size());

      assertTrue(ce.get(0).getLeft().is(1, "abc"));
      assertEquals(null, ce.get(0).getRight());

      assertEquals(null, ce.get(1).getLeft());
      assertTrue(ce.get(1).getRight().is(2, "def"));
    }

    {
      List<Row> left = Arrays.asList(new Row[] { new Row(2, "def") });
      List<Row> right = Arrays.asList(new Row[] { new Row(1, "abc") });
      List<CorrelatedEntry<Row>> ce = Correlator.correlateSorted(left, right, comp);
      assertEquals(2, ce.size());

      assertEquals(null, ce.get(0).getLeft());
      assertTrue(ce.get(0).getRight().is(1, "abc"));

      assertTrue(ce.get(1).getLeft().is(2, "def"));
      assertEquals(null, ce.get(1).getRight());
    }

    {
      List<Row> left = Arrays.asList(new Row[] { new Row(2, "def") });
      List<Row> right = Arrays.asList(new Row[] { new Row(1, "abc"), new Row(3, "ghi") });
      List<CorrelatedEntry<Row>> ce = Correlator.correlateSorted(left, right, comp);
      assertEquals(3, ce.size());

      assertEquals(null, ce.get(0).getLeft());
      assertTrue(ce.get(0).getRight().is(1, "abc"));

      assertTrue(ce.get(1).getLeft().is(2, "def"));
      assertEquals(null, ce.get(1).getRight());

      assertEquals(null, ce.get(2).getLeft());
      assertTrue(ce.get(2).getRight().is(3, "ghi"));
    }

  }

  // Classes

  public static class Row {

    private int key;
    private String property1;

    public Row(int key, String property1) {
      this.key = key;
      this.property1 = property1;
    }

    public int getKey() {
      return key;
    }

    public String getProperty1() {
      return property1;
    }

    public String toString() {
      return "[key:" + this.key + ", property1:" + this.property1 + "]";
    }

    public boolean is(final int key, final String property1) {
      if (this.key != key) {
        return false;
      }
      return this.property1 == null ? property1 == null : this.property1.equals(property1);
    }

    public static Comparator<Row> getComparator() {
      return new Comparator<Row>() {
        @Override
        public int compare(final Row o1, final Row o2) {
          return o1.key - o2.key;
        }
      };
    }

  }

  // Utilities

  public void display(final String txt) {
    System.out.println("[Correlator Tests] " + txt);
  }

}
