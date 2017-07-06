package org.hotrod.config;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;

import org.apache.log4j.Logger;
import org.hotrod.config.tags.QueryTag;
import org.hotrod.config.tags.SequenceTag;
import org.hotrod.exceptions.InvalidConfigurationFileException;

/**
 * <pre>
 *
 *     AbstractDAOTag < ------------------------------+
 *      ^                                             |
 *      |                                             |
 *     AbstractCompositeDAOTag                    AbstractSQLDAOTag
 *      ^          ^        ^                         ^        ^
 *      |          |        |                         |        |
 * CustomDAOTag  TableTag  ViewTag                SelectTag  QueryTag
 * 
 * </pre>
 */
public abstract class AbstractCompositeDAOTag extends AbstractDAOTag {

  // Constants

  private static final Logger log = Logger.getLogger(AbstractCompositeDAOTag.class);

  // Properties

  private List<SequenceTag> sequences = new ArrayList<SequenceTag>();
  private List<QueryTag> queries = new ArrayList<QueryTag>();

  // Constructor

  protected AbstractCompositeDAOTag(final String tagName) {
    super(tagName);
  }

  // JAXB Setters

  @XmlElement
  public final void setSequence(final SequenceTag sequence) {
    this.sequences.add(sequence);
  }

  @XmlElement
  public final void setQuery(final QueryTag query) {
    this.queries.add(query);
  }

  // Behavior

  protected void validate(final String tagName, final String nameTitle, final String nameValue)
      throws InvalidConfigurationFileException {

    // sequences

    Set<String> seqNames = new HashSet<String>();

    String tagId = "tag <" + tagName + "> with " + nameTitle + " '" + nameValue + "'";

    for (SequenceTag s : sequences) {
      s.validate();
      if (seqNames.contains(s.getName())) {
        throw new InvalidConfigurationFileException("Duplicate sequence with name '" + s.getName() + "' on " + tagId
            + ". You cannot add the same sequence twice in the same <dao> tag.");
      }
      String method = s.getJavaMethodName();
      if (this.declaredMethodNames.contains(method)) {
        throw new InvalidConfigurationFileException("Duplicate sequence method-name '" + method + "' on " + tagId
            + ". Method names (either specified or implied) must be different inside a DAO. "
            + "Please change the method-name.");
      }
      seqNames.add(s.getName());
      this.declaredMethodNames.add(method);
      log.debug("* added '" + method + "'");
    }

    // updates

    for (QueryTag q : this.queries) {
      q.validate();
      if (this.declaredMethodNames.contains(q.getJavaMethodName())) {
        throw new InvalidConfigurationFileException("Duplicate java-method-name '" + q.getJavaMethodName() + "' on "
            + tagId + ". You cannot add multiple queries or sequences with identical java-method-name "
            + "(specified or implied) in the same <dao> tag. " + "For <query> tags they cannot have the same name, "
            + "even if they have different parameters (different signature).");
      }
      this.declaredMethodNames.add(q.getJavaMethodName());
      log.debug("* added '" + q.getJavaMethodName() + "'");
    }

  }

  // Getters

  public final List<SequenceTag> getSequences() {
    return sequences;
  }

  public final List<QueryTag> getQueries() {
    return queries;
  }

}
