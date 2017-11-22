package automatedtests.identifiers;

import org.hotrod.utils.identifiers.DataSetIdentifier;
import org.hotrod.utils.identifiers.DbIdentifier;
import org.hotrod.utils.identifiers.Identifier;
import org.hotrod.utils.identifiers.JavaIdentifier;

import junit.framework.TestCase;

public class IdentifierTests extends TestCase {

  public IdentifierTests(final String txt) {
    super(txt);
  }

  public void testDBIdentifier() {

    // Without Java name

    {
      Identifier id = new DbIdentifier("");
      // display(id);
      assertEquals("_", id.getJavaClassIdentifier()); // Wrong: throw exception
      assertEquals("_", id.getJavaMemberIdentifier()); // Wrong: throw exception
      assertEquals("_", id.getJavaConstantIdentifier()); // Wrong: throw
                                                         // exception
    }

    {
      Identifier id = new DbIdentifier(" ");
      // display(id);
      assertEquals("$0020", id.getJavaClassIdentifier());
      assertEquals("$0020", id.getJavaMemberIdentifier());
      assertEquals("$0020", id.getJavaConstantIdentifier());
    }

    {
      Identifier id = new DbIdentifier("_");
      // display(id);
      assertEquals("__", id.getJavaClassIdentifier()); // Wrong: _
      assertEquals("__", id.getJavaMemberIdentifier()); // Wrong: _
      assertEquals("___", id.getJavaConstantIdentifier()); // Wrong: _
    }

    {
      Identifier id = new DbIdentifier("__");
      // display(id);
      assertEquals("___", id.getJavaClassIdentifier()); // Wrong: __
      assertEquals("___", id.getJavaMemberIdentifier()); // Wrong: __
      assertEquals("_____", id.getJavaConstantIdentifier()); // Wrong: __
    }

    {
      Identifier id = new DbIdentifier("a!");
      // display(id);
      assertEquals("A$0021", id.getJavaClassIdentifier());
      assertEquals("a$0021", id.getJavaMemberIdentifier());
      assertEquals("A$0021", id.getJavaConstantIdentifier());
    }

    {
      Identifier id = new DbIdentifier("abc");
      // display(id);
      assertEquals("Abc", id.getJavaClassIdentifier());
      assertEquals("abc", id.getJavaMemberIdentifier());
      assertEquals("ABC", id.getJavaConstantIdentifier());
    }

    {
      Identifier id = new DbIdentifier("abc_5");
      // display(id);
      assertEquals("Abc5", id.getJavaClassIdentifier());
      assertEquals("abc5", id.getJavaMemberIdentifier());
      assertEquals("ABC_5", id.getJavaConstantIdentifier());
    }

    {
      Identifier id = new DbIdentifier("ab$c_5");
      // display(id);
      assertEquals("Ab$0024c5", id.getJavaClassIdentifier()); // Wrong: Ab$c_5
      assertEquals("ab$0024c5", id.getJavaMemberIdentifier()); // Wrong: ab$c_5
      assertEquals("AB$0024C_5", id.getJavaConstantIdentifier()); // Wrong:
                                                                  // AB$C_5
    }

    {
      Identifier id = new DbIdentifier("abc_dEf5");
      // display(id);
      assertEquals("AbcDef5", id.getJavaClassIdentifier());
      assertEquals("abcDef5", id.getJavaMemberIdentifier());
      assertEquals("ABC_DEF5", id.getJavaConstantIdentifier());
    }

    // With Java name

    {
      Identifier id = new DbIdentifier("abc", "Vehicle4");
      // display(id);
      assertEquals("Vehicle4", id.getJavaClassIdentifier());
      assertEquals("vehicle4", id.getJavaMemberIdentifier());
      assertEquals("VEHICLE4", id.getJavaConstantIdentifier());
    }

    {
      Identifier id = new DbIdentifier("abc", "VeHicle_5");
      // display(id);
      assertEquals("VeHicle_5", id.getJavaClassIdentifier());
      assertEquals("veHicle_5", id.getJavaMemberIdentifier());
      assertEquals("VE_HICLE_5", id.getJavaConstantIdentifier());
    }

    {
      Identifier id = new DbIdentifier("abc", "veHicle_5");
      // display(id);
      assertEquals("VeHicle_5", id.getJavaClassIdentifier());
      assertEquals("veHicle_5", id.getJavaMemberIdentifier());
      assertEquals("VE_HICLE_5", id.getJavaConstantIdentifier());
    }

    {
      Identifier id = new DbIdentifier("abc", "VEHicle_5");
      // display(id);
      assertEquals("VEHicle_5", id.getJavaClassIdentifier());
      assertEquals("vEHicle_5", id.getJavaMemberIdentifier());
      assertEquals("VE_HICLE_5", id.getJavaConstantIdentifier());
    }

    {
      Identifier id = new DbIdentifier("abc", "Vehicle$7");
      // display(id);
      assertEquals("Vehicle$7", id.getJavaClassIdentifier());
      assertEquals("vehicle$7", id.getJavaMemberIdentifier());
      assertEquals("VEHICLE$7", id.getJavaConstantIdentifier());
    }

    {
      Identifier id = new DbIdentifier("abc", "Vehicle_Tall7");
      // display(id);
      assertEquals("Vehicle_Tall7", id.getJavaClassIdentifier());
      assertEquals("vehicle_Tall7", id.getJavaMemberIdentifier());
      assertEquals("VEHICLE__TALL7", id.getJavaConstantIdentifier()); // Wrong:
                                                                      // VEHICLE_TALL7
    }

  }

  public void testJavaIdentifier() {

    {
      Identifier id = new JavaIdentifier("Vehicle4", "java.lang.String");
      // display(id);
      assertEquals("Vehicle4", id.getJavaClassIdentifier());
      assertEquals("vehicle4", id.getJavaMemberIdentifier());
      assertEquals("VEHICLE4", id.getJavaConstantIdentifier());
    }

    {
      Identifier id = new JavaIdentifier("VeHicle_5", "java.lang.String");
      // display(id);
      assertEquals("VeHicle_5", id.getJavaClassIdentifier());
      assertEquals("veHicle_5", id.getJavaMemberIdentifier());
      assertEquals("VE_HICLE_5", id.getJavaConstantIdentifier());
    }

    {
      Identifier id = new JavaIdentifier("veHicle_5", "java.lang.String");
      // display(id);
      assertEquals("VeHicle_5", id.getJavaClassIdentifier());
      assertEquals("veHicle_5", id.getJavaMemberIdentifier());
      assertEquals("VE_HICLE_5", id.getJavaConstantIdentifier());
    }

    {
      Identifier id = new JavaIdentifier("VEHicle_5", "java.lang.String");
      // display(id);
      assertEquals("VEHicle_5", id.getJavaClassIdentifier());
      assertEquals("vEHicle_5", id.getJavaMemberIdentifier()); // Wrong:
                                                               // veHicle_5
      assertEquals("V_E_HICLE_5", id.getJavaConstantIdentifier()); // Wrong:
                                                                   // VE_HICLE_5
    }

    {
      Identifier id = new JavaIdentifier("Vehicle$7", "java.lang.String");
      // display(id);
      assertEquals("Vehicle$7", id.getJavaClassIdentifier());
      assertEquals("vehicle$7", id.getJavaMemberIdentifier());
      assertEquals("VEHICLE$7", id.getJavaConstantIdentifier());
    }

    {
      Identifier id = new JavaIdentifier("Vehicle_Tall7", "java.lang.String");
      display(id);
      assertEquals("Vehicle_Tall7", id.getJavaClassIdentifier());
      assertEquals("vehicle_Tall7", id.getJavaMemberIdentifier());
      assertEquals("VEHICLE__TALL7", id.getJavaConstantIdentifier()); // Wrong:
                                                                      // VEHICLE_TALL7
    }

  }

  public void testDataSetdentifier() {

    // Without Java name

    {
      Identifier id = new DataSetIdentifier("abc");
      // display(id);
      assertEquals("Abc", id.getJavaClassIdentifier());
      assertEquals("abc", id.getJavaMemberIdentifier());
      assertEquals("ABC", id.getJavaConstantIdentifier());
    }

    {
      Identifier id = new DataSetIdentifier("abc_5");
      // display(id);
      assertEquals("Abc5", id.getJavaClassIdentifier());
      assertEquals("abc5", id.getJavaMemberIdentifier());
      assertEquals("ABC_5", id.getJavaConstantIdentifier());
    }

    {
      Identifier id = new DataSetIdentifier("ab$c_5");
      // display(id);
      assertEquals("Ab$0024c5", id.getJavaClassIdentifier()); // Wrong: Ab$c_5
      assertEquals("ab$0024c5", id.getJavaMemberIdentifier()); // Wrong: ab$c_5
      assertEquals("AB$0024C_5", id.getJavaConstantIdentifier()); // Wrong:
                                                                  // AB$C_5
    }

    {
      Identifier id = new DataSetIdentifier("abc_dEf5");
      // display(id);
      assertEquals("AbcDef5", id.getJavaClassIdentifier());
      assertEquals("abcDef5", id.getJavaMemberIdentifier());
      assertEquals("ABC_DEF5", id.getJavaConstantIdentifier());
    }

    // With Java name

    {
      Identifier id = new DataSetIdentifier("abc", "Vehicle4");
      // display(id);
      assertEquals("Vehicle4", id.getJavaClassIdentifier());
      assertEquals("vehicle4", id.getJavaMemberIdentifier());
      assertEquals("VEHICLE4", id.getJavaConstantIdentifier());
    }

    {
      Identifier id = new DataSetIdentifier("abc", "VeHicle_5");
      // display(id);
      assertEquals("VeHicle_5", id.getJavaClassIdentifier());
      assertEquals("veHicle_5", id.getJavaMemberIdentifier());
      assertEquals("VE_HICLE_5", id.getJavaConstantIdentifier());
    }

    {
      Identifier id = new DataSetIdentifier("abc", "veHicle_5");
      // display(id);
      assertEquals("VeHicle_5", id.getJavaClassIdentifier());
      assertEquals("veHicle_5", id.getJavaMemberIdentifier());
      assertEquals("VE_HICLE_5", id.getJavaConstantIdentifier());
    }

    {
      Identifier id = new DataSetIdentifier("abc", "VEHicle_5");
      // display(id);
      assertEquals("VEHicle_5", id.getJavaClassIdentifier());
      assertEquals("veHicle_5", id.getJavaMemberIdentifier());
      assertEquals("VE_HICLE_5", id.getJavaConstantIdentifier());
    }

    {
      Identifier id = new DataSetIdentifier("abc", "Vehicle$7");
      // display(id);
      assertEquals("Vehicle$7", id.getJavaClassIdentifier());
      assertEquals("vehicle$7", id.getJavaMemberIdentifier());
      assertEquals("VEHICLE$7", id.getJavaConstantIdentifier());
    }

    {
      Identifier id = new DataSetIdentifier("abc", "Vehicle_Tall7");
      // display(id);
      assertEquals("Vehicle_Tall7", id.getJavaClassIdentifier());
      assertEquals("vehicle_Tall7", id.getJavaMemberIdentifier());
      assertEquals("VEHICLE__TALL7", id.getJavaConstantIdentifier()); // Wrong:
                                                                      // VEHICLE_TALL7
    }

  }

  // Utilities

  public void display(final String txt) {
    System.out.println(txt);
  }

  public void display(final Identifier id) {
    System.out.println(
        id.getJavaClassIdentifier() + " / " + id.getJavaMemberIdentifier() + " / " + id.getJavaConstantIdentifier());
  }

}
