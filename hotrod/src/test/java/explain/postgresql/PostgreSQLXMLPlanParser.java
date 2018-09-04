package explain.postgresql;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import explain.InvalidPlanException;

public class PostgreSQLXMLPlanParser {

  private static final String CREATE_PREFIX = "CREATE ";

  public static PostgreSQLOperator parse(final Connection conn, final String doc)
      throws InvalidPlanException, SQLException {
    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    Document d;
    try {
      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
      d = dBuilder.parse(new ByteArrayInputStream(doc.getBytes("UTF-8")));
    } catch (UnsupportedEncodingException e1) {
      throw new InvalidPlanException(e1.getMessage());
    } catch (ParserConfigurationException e1) {
      throw new InvalidPlanException(e1.getMessage());
    } catch (SAXException e1) {
      throw new InvalidPlanException(e1.getMessage());
    } catch (IOException e1) {
      throw new InvalidPlanException(e1.getMessage());
    }

    Element explainTag = d.getDocumentElement();
    // System.out.println("explain=" + explainTag.getTagName());

    NodeList l1 = explainTag.getChildNodes();
    Node queryTag = l1.item(1);
    // System.out.println("query=" + queryTag.getNodeName());

    NodeList l2 = queryTag.getChildNodes();
    Node opTag = l2.item(1);
    // System.out.println("opTag=" + opTag.getNodeName());

    PostgreSQLOperator op = parseOperator(conn, opTag);

    return op;
  }

  private static PostgreSQLOperator parseOperator(final Connection conn, final Node operatorTag)
      throws InvalidPlanException, SQLException {

    int id = PostgreSQLOperator.produceUniqueId();

    String nodeType = null;
    String joinType = null;
    String parentRelationship = null;
    String alias = null;
    String indexName = null;
    String indexDescription = null;
    String indexCond = null;
    String recheckCond = null;
    Double startupCost = null;
    Double totalCost = null;
    Double planRows = null;
    Double planWidth = null;
    String relationName = null;
    String filter = null;
    List<PostgreSQLOperator> sources = new ArrayList<PostgreSQLOperator>();

    NodeList children = operatorTag.getChildNodes();
    for (int i = 0; i < children.getLength(); i++) {
      Node n = children.item(i);
      // System.out.println("n.getNodeName()=" + n.getNodeName());
      if ("Node-Type".equals(n.getNodeName())) {
        nodeType = getString(n);
      } else if ("Join-Type".equals(n.getNodeName())) {
        joinType = getString(n);
      } else if ("Parent-Relationship".equals(n.getNodeName())) {
        parentRelationship = getString(n);
      } else if ("Alias".equals(n.getNodeName())) {
        alias = getString(n);
      } else if ("Index-Name".equals(n.getNodeName())) {
        indexName = getString(n);
      } else if ("Index-Cond".equals(n.getNodeName())) {
        indexCond = getString(n);
      } else if ("Recheck-Cond".equals(n.getNodeName())) {
        recheckCond = getString(n);
      } else if ("Startup-Cost".equals(n.getNodeName())) {
        startupCost = getDouble(n);
      } else if ("Total-Cost".equals(n.getNodeName())) {
        totalCost = getDouble(n);
      } else if ("Plan-Rows".equals(n.getNodeName())) {
        planRows = getDouble(n);
      } else if ("Plan-Width".equals(n.getNodeName())) {
        planWidth = getDouble(n);
      } else if ("Relation-Name".equals(n.getNodeName())) {
        relationName = getString(n);
      } else if ("Filter".equals(n.getNodeName())) {
        filter = getString(n);
      } else if ("Plans".equals(n.getNodeName())) {
        NodeList plans = n.getChildNodes();
        for (int j = 0; j < plans.getLength(); j++) {
          Node item = plans.item(j);
          if ("Plan".equals(item.getNodeName())) {
            PostgreSQLOperator so = parseOperator(conn, item);
            sources.add(so);
          }
        }
      }
    }
    boolean includesHeapFetch = "Bitmap Heap Scan".equals(nodeType) //
        || "Index Scan".equals(nodeType) //
        || "Seq Scan".equals(nodeType) //
    ;
    if (indexName != null) {
      indexDescription = getIndexDefinition(conn, indexName);
    }

    return new PostgreSQLOperator(id, nodeType, joinType, parentRelationship, alias, indexName, indexDescription,
        indexCond, recheckCond, startupCost, totalCost, planRows, planWidth, relationName, filter, includesHeapFetch,
        sources);

  }

  private static String getIndexDefinition(final Connection conn, final String indexName) throws SQLException {
    PreparedStatement st = null;
    ResultSet rs = null;
    try {
      st = conn.prepareStatement("select indexdef from pg_indexes where indexname = ?");
      st.setString(1, indexName);
      rs = st.executeQuery();
      if (rs.next()) {
        String createIndex = rs.getString(1);
        if (rs.wasNull()) {
          return null;
        }
        if (createIndex.startsWith(CREATE_PREFIX)) {
          return createIndex.substring(CREATE_PREFIX.length());
        } else {
          return createIndex;
        }
      }
      return null;
    } finally {
      try {
        if (rs != null) {
          rs.close();
        }
      } finally {
        if (st != null) {
          st.close();
        }
      }
    }
  }

  // Helpers

  private static String getString(final Node node) {
    return node.getTextContent();
  }

  private static Double getDouble(final Node node) throws InvalidPlanException {
    String s = getString(node);
    if (s == null) {
      return null;
    } else {
      try {
        return Double.parseDouble(s);
      } catch (ClassCastException e) {
        throw new InvalidPlanException("Invalid floating point value '" + s + "'.");
      }
    }
  }

}
