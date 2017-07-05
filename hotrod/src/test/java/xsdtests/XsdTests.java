package xsdtests;

import java.io.IOException;
import java.net.MalformedURLException;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import xsdtests.case00.XMLLoader0;
import xsdtests.case01.XMLLoader1;
import xsdtests.case02.XMLLoader2;
import xsdtests.case03.XMLLoader3;
import xsdtests.case04.XMLLoader4;
import xsdtests.case05.XMLLoader5;
import xsdtests.case06.XMLLoader6;
import xsdtests.case07.XMLLoader7;
import xsdtests.case08.XMLLoader8;
import xsdtests.case09.XMLLoader9;
import xsdtests.case10.XMLLoader10;

public class XsdTests {

  public static void main(final String[] args)
      throws SAXException, IOException, ParserConfigurationException, JAXBException {

    System.out.println("=== XsdTests ===");

    // --- 0. All cases combined
    test0();

    // --- 1. No Attributes, No Elements, No Text
    // test1();

    // --- 2. No Attributes, No Elements, TEXT
    // test2();

    // --- 3. ATTRIBUTES, no Elements, no Text
    // test3();

    // --- 4. No Attributes, ELEMENTS, no Text
    // test4();

    // --- 5. Attributes, no Elements, Text
    // test5();

    // --- 6. Attributes, Elements, no Text
    // test6();

    // --- 7. No Attributes, Elements, Text
    // test7();

    // --- 8. Attributes, Elements, Text
    // test8();

    // --- 9. Attributes, Multi Elements, Text
    // test9();

    // --- 10. Recursive Tags
    // test10();

    System.out.println("=== XsdTests Complete ===");

  }

  private static void test0() throws JAXBException, MalformedURLException, SAXException {
    XMLLoader0 loader = new XMLLoader0();
    loader.parse();
  }

  private static void test1() throws JAXBException, MalformedURLException, SAXException {
    XMLLoader1 loader = new XMLLoader1();
    loader.parse();
  }

  private static void test2() throws JAXBException, MalformedURLException, SAXException {
    XMLLoader2 loader = new XMLLoader2();
    loader.parse();
  }

  private static void test3() throws JAXBException, MalformedURLException, SAXException {
    XMLLoader3 loader = new XMLLoader3();
    loader.parse();
  }

  private static void test4() throws JAXBException, MalformedURLException, SAXException {
    XMLLoader4 loader = new XMLLoader4();
    loader.parse();
  }

  private static void test5() throws JAXBException, MalformedURLException, SAXException {
    XMLLoader5 loader = new XMLLoader5();
    loader.parse();
  }

  private static void test6() throws JAXBException, MalformedURLException, SAXException {
    XMLLoader6 loader = new XMLLoader6();
    loader.parse();
  }

  private static void test7() throws JAXBException, MalformedURLException, SAXException {
    XMLLoader7 loader = new XMLLoader7();
    loader.parse();
  }

  private static void test8() throws JAXBException, MalformedURLException, SAXException {
    XMLLoader8 loader = new XMLLoader8();
    loader.parse();
  }

  private static void test9() throws JAXBException, MalformedURLException, SAXException {
    XMLLoader9 loader = new XMLLoader9();
    loader.parse();
  }

  private static void test10() throws JAXBException, MalformedURLException, SAXException {
    XMLLoader10 loader = new XMLLoader10();
    loader.parse();
  }

}
