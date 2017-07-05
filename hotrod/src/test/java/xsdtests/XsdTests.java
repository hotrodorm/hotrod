package xsdtests;

import java.io.IOException;
import java.net.MalformedURLException;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

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

    // test1();
    // test2();
    // test3();
    // test4();
    // test5();
    // test6();
    // test7();
    // test8();
    // test9();
    test10();

    System.out.println("=== XsdTests Complete ===");

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
