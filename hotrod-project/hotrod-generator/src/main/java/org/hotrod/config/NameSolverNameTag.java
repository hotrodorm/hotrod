package org.hotrod.config;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hotrod.exceptions.CouldNotResolveNameException;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.utils.Compare;
import org.hotrodorm.hotrod.utils.SUtil;

@XmlRootElement(name = "name")
public class NameSolverNameTag extends AbstractConfigurationTag {

  private static final long serialVersionUID = 1L;

  // Constants

  private static final Logger log = LogManager.getLogger(NameSolverNameTag.class);

  public enum Scope {
    TABLE, VIEW, COLUMN
  };

  // Properties

  private String value = null;
  private String replace = null;
  private String scope = null;

  private Pattern valuePattern;
  private Set<Integer> captures;
  private boolean matchesColumns;
  private boolean matchesTables;
  private boolean matchesViews;

  // Constructor

  public NameSolverNameTag() {
    super("name");
    log.debug("init");
  }

  // JAXB Setters

  @XmlAttribute(name = "value")
  public void setJavaType(final String value) {
    this.value = value;
  }

  @XmlAttribute(name = "replace")
  public void setReplace(final String replace) {
    this.replace = replace;
  }

  @XmlAttribute(name = "scope")
  public void setScope(final String scope) {
    this.scope = scope;
  }

  // Behavior

  public void validate(final HotRodConfigTag config) throws InvalidConfigurationFileException {

    // value

    if (SUtil.isEmpty(this.value)) {
      throw new InvalidConfigurationFileException(this, "Attribute 'value' cannot be empty: must be a regex pattern");
    }
    try {
      this.valuePattern = Pattern.compile(this.value);
    } catch (PatternSyntaxException e) {
      throw new InvalidConfigurationFileException(this,
          "Invalid pattern '" + this.value + "' in attribute 'value': " + e.getMessage());
    }

    // replace

    log.debug("this.replace=" + this.replace);
    if (SUtil.isEmpty(this.replace)) {
      throw new InvalidConfigurationFileException(this,
          "Attribute 'replace' cannot be empty: must specify a redering pattern for the name, using $1, $2, etc. for each capture");
    }

    this.captures = new HashSet<Integer>();
    for (int i = 1; i < 10; i++) {
      if (this.replace.indexOf("$" + i) != -1) {
        this.captures.add(i);
        log.debug(" * capture " + i);
      }
    }
    log.debug(" * total captures=" + this.captures.size());

    // scope

    if (this.scope == null) {
      this.matchesColumns = true;
      this.matchesTables = true;
      this.matchesViews = true;
    } else if (SUtil.isEmpty(this.scope)) {
      throw new InvalidConfigurationFileException(this, "When specified, the 'scope' attribute cannot be empty");
    } else {
      this.matchesColumns = false;
      this.matchesTables = false;
      this.matchesViews = false;
      List<String> parts = Stream.of(this.scope.split(",")).map(s -> s.trim()).collect(Collectors.toList());
      Set<String> s = new HashSet<String>();
      for (String p : parts) {
        if ("table".equals(p)) {
          this.matchesTables = true;
        } else if ("view".equals(p)) {
          this.matchesViews = true;
        } else if ("column".equals(p)) {
          this.matchesColumns = true;
        } else {
          throw new InvalidConfigurationFileException(this, "Invalid value '" + this.scope
              + "' in the 'scope' attribute; it can include a comma-separated list including: table, view, or column");
        }
        if (s.contains(p)) {
          throw new InvalidConfigurationFileException(this, "Invalid value '" + this.scope
              + "' in the 'scope' attribute; the value '" + p + "' can only be included once");
        }
        s.add(p);
      }
    }
    log.debug("this.matchesColumns=" + this.matchesColumns + " this.matchesTables=" + this.matchesTables
        + " this.matchesViews=" + this.matchesViews);

  }

  public String tryToReplace(final String name, final Scope scope) throws CouldNotResolveNameException {
    if (scope == Scope.TABLE && this.matchesTables || scope == Scope.VIEW && this.matchesViews
        || scope == Scope.COLUMN && this.matchesColumns) {
      Matcher m = this.valuePattern.matcher(name);
      boolean matches = m.matches();
      log.debug("%%% " + name + ".matches(" + this.value + ")=" + matches);
      if (matches) {
        String newName = this.replace;
        for (Integer c : this.captures) {
          String capture;
          try {
            capture = m.group(c);
            log.debug("capture=" + capture);
            newName = newName.replaceAll("\\$" + c, capture);
          } catch (IllegalStateException e) {
            throw new CouldNotResolveNameException(
                "Could not resolve name '" + name + "' in scope " + scope + " using pattern value '" + this.value
                    + "': could not find capture $" + c + " for replace pattern '" + this.replace + "'");
          }
        }
        return newName;
      }
    }
    return null;
  }

  // Getters

  // Merging logic

  @Override
  public boolean sameKey(final AbstractConfigurationTag fresh) {
    NameSolverNameTag f = (NameSolverNameTag) fresh;
    return Compare.same(this.value, f.value);
  }

  @Override
  public boolean copyNonKeyProperties(final AbstractConfigurationTag fresh) {
    try {
      NameSolverNameTag f = (NameSolverNameTag) fresh;
      boolean different = !same(fresh);
      this.replace = f.replace;
      this.scope = f.scope;
      this.matchesTables = f.matchesTables;
      this.matchesViews = f.matchesViews;
      this.matchesColumns = f.matchesColumns;
      return different;
    } catch (ClassCastException e) {
      return false;
    }
  }

  @Override
  public boolean same(final AbstractConfigurationTag fresh) {
    // TODO: needs to be properly implemented
    try {
      NameSolverNameTag f = (NameSolverNameTag) fresh;
      return //
      Compare.same(this.value, f.value) && //
          Compare.same(this.replace, f.replace) && //
          Compare.same(this.scope, f.scope);
    } catch (ClassCastException e) {
      return false;
    }
  }

  // Simple Caption

  @Override
  public String getInternalCaption() {
    return this.getTagName() + ":" + this.value;
  }

}
