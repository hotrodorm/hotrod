package org.hotrod.config;

import java.io.File;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.xml.bind.Unmarshaller.Listener;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.stream.Location;
import javax.xml.stream.XMLStreamReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hotrod.database.DatabaseAdapter;
import org.hotrod.exceptions.ControlledException;
import org.hotrod.exceptions.FacetNotFoundException;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.exceptions.UncontrolledException;
import org.hotrod.metadata.DataSetMetadata;
import org.hotrod.metadata.Metadata;
import org.hotrod.runtime.dynamicsql.SourceLocation;
import org.hotrod.utils.FileRegistry;
import org.nocrala.tools.database.tartarus.core.CatalogSchema;
import org.nocrala.tools.database.tartarus.core.JdbcTable;

public abstract class AbstractHotRodConfigTag extends AbstractConfigurationTag {

  private static final long serialVersionUID = 1L;

  // Constants

  private static final Logger log = LogManager.getLogger(AbstractHotRodConfigTag.class);

  // Properties

  private List<TableTag> tables = new ArrayList<TableTag>();
  private List<ViewTag> views = new ArrayList<ViewTag>();
  private List<EnumTag> enums = new ArrayList<EnumTag>();
  private List<ExecutorTag> executors = new ArrayList<ExecutorTag>();
  private List<FragmentTag> fragments = new ArrayList<FragmentTag>();
  private List<FacetTag> facets = new ArrayList<FacetTag>();
  private Map<String, FacetTag> assembledFacets = new HashMap<String, FacetTag>();

  private FacetTag allFacets = null;

  private Set<String> facetNames = new HashSet<String>();
  private Set<FacetTag> chosenFacets = new HashSet<FacetTag>();

  // Constructor

  protected AbstractHotRodConfigTag(final String tagName) {
    super(tagName);
  }

  // Setters (JAXB)

  @XmlElement
  public void setTable(final TableTag table) {
    this.tables.add(table);
  }

  @XmlElement
  public void setView(final ViewTag view) {
    this.views.add(view);
  }

  @XmlElement
  public void setEnum(final EnumTag e) {
    this.enums.add(e);
  }

  @XmlElement(name = "dao")
  public void setExecutor(final ExecutorTag executor) {
    this.executors.add(executor);
  }

  @XmlElement
  public void setFragment(final FragmentTag fragment) {
    this.fragments.add(fragment);
  }

  @XmlElement
  public void setFacet(final FacetTag facet) {
    this.facets.add(facet);
  }

  // Setters

  private void setChosenFacets(final Set<String> facetNames) throws FacetNotFoundException {
    this.facetNames = facetNames;
    this.chosenFacets = new HashSet<FacetTag>();
    for (String f : facetNames) {
      FacetTag facet = this.assembledFacets.get(f);
      if (facet == null) {
        throw new FacetNotFoundException(f);
      }
      this.chosenFacets.add(facet);
    }
  }

  // Behavior

  protected void validateCommon(final HotRodConfigTag config, final File file, final FileRegistry fileRegistry,
      final File parentFile, final DaosTag daosTag, final HotRodFragmentConfigTag fragmentConfig,
      final DatabaseAdapter adapter, final LinkedHashSet<String> facetNames, final CatalogSchema currentCS)
      throws InvalidConfigurationFileException, ControlledException, UncontrolledException, FacetNotFoundException {

    log.debug("validateCommon");

    File parentDir = file != null ? file.getParentFile() : null;

    // DAOs

    for (TableTag t : this.tables) {
      t.validate(daosTag, config, fragmentConfig, adapter, currentCS);
    }
    Collections.sort(this.tables, new Comparator<TableTag>() {
      @Override
      public int compare(final TableTag a, final TableTag b) {
        return a.getId().compareTo(b.getId());
      }
    });
    super.addChildren(this.tables);

    for (EnumTag e : this.enums) {
      e.validate(daosTag, config, fragmentConfig, adapter, currentCS);
    }
    Collections.sort(this.enums, new Comparator<EnumTag>() {
      @Override
      public int compare(final EnumTag a, final EnumTag b) {
        return a.getId().compareTo(b.getId());
      }
    });
    super.addChildren(this.enums);

    for (ViewTag v : this.views) {
      v.validate(daosTag, config, fragmentConfig, adapter, currentCS);
    }
    Collections.sort(this.views, new Comparator<ViewTag>() {
      @Override
      public int compare(final ViewTag a, final ViewTag b) {
        return a.getId().compareTo(b.getId());
      }
    });
    super.addChildren(this.views);

    for (ExecutorTag x : this.executors) {
      try {
        x.validate(daosTag, config, fragmentConfig, adapter);
      } catch (InvalidConfigurationFileException e1) {
        throw e1;
      }
    }
    Collections.sort(this.executors, new Comparator<ExecutorTag>() {
      @Override
      public int compare(final ExecutorTag a, final ExecutorTag b) {
        return a.getJavaClassName().compareTo(b.getJavaClassName());
      }
    });
    super.addChildren(this.executors);

    for (FacetTag f : this.facets) {
      f.validate(config, daosTag, fragmentConfig, adapter);
    }
    super.addChildren(this.facets);

    // Fragments

    for (FragmentTag f : this.fragments) {
      f.validate(config, parentDir, fileRegistry, parentFile, daosTag, adapter, facetNames, currentCS);
      this.mergeFragment(f.getFragmentConfig());
      super.addChild(f);
    }

    // Assemble facets

    this.allFacets = new FacetTag();
    this.allFacets.mergeOther(this.tables, this.views, this.enums, this.executors);

    for (FacetTag f : this.facets) {
      FacetTag af = this.assembledFacets.get(f.getName());
      if (af == null) {
        af = new FacetTag();
        af.setName(f.getName());
        this.assembledFacets.put(f.getName(), af);
      }
      af.mergeOther(f);
      this.allFacets.mergeOther(f);
    }

    this.setChosenFacets(facetNames);

    // Validate extends (across all facets)

    for (TableTag t : this.getAllTables()) {
      t.validateExtendsAgainstAllTables(this.getAllTables(), this.getAllEnums());
    }

    // Validate extends (within selected facets only)

    for (TableTag t : this.getFacetTables()) {
      t.validateExtendsInSelectedFacets(this.getFacetTables(), this.getFacetEnums(), this.facetNames);
    }

    // display

    if (log.isDebugEnabled()) {
      logFacet(file, "[after] All", this.allFacets);
      for (FacetTag f : this.facets) {
        logFacet(file, f.getName(), f);
      }
    }

  }

  private void logFacet(final File file, final String name, final FacetTag f) {
    log.debug("* LISTING " + name + " (" + file.getName() + ") ...");

    for (TableTag t : f.getTables()) {
      log.debug(" - tables: " + t.getId().getCanonicalSQLName());
    }

    for (ViewTag v : f.getViews()) {
      log.debug(" - views: " + v.getId().getCanonicalSQLName());
    }

    for (EnumTag e : f.getEnums()) {
      log.debug(" - enum: " + e.getId().getCanonicalSQLName());
    }

    for (ExecutorTag dao : f.getExecutors()) {
      log.debug(" - daos: " + dao.getJavaClassName());
    }

  }

  private void mergeFragment(final AbstractHotRodConfigTag other) {
    this.tables.addAll(other.tables);
    this.views.addAll(other.views);
    this.enums.addAll(other.enums);
    this.executors.addAll(other.executors);
    this.facets.addAll(other.facets);
  }

  public void validateAgainstDatabase(final Metadata metadata, final Connection conn, final DatabaseAdapter adapter)
      throws InvalidConfigurationFileException {

    for (TableTag t : this.getFacetTables()) {
      t.validateAgainstDatabase(metadata);
    }

    for (ViewTag v : this.getFacetViews()) {
      v.validateAgainstDatabase(metadata);
    }

    for (EnumTag e : this.getFacetEnums()) {
      e.validateAgainstDatabase(metadata, conn, adapter);
    }

    for (ExecutorTag d : this.getFacetExecutors()) {
      d.validateAgainstDatabase(metadata);
    }

  }

  public TableTag getTableTag(final JdbcTable t) {
    log.debug("facet tables: " + this.getFacetTables().size());
    for (TableTag tag : this.getFacetTables()) {
      log.debug("Comparing table '" + t.getName() + "' -- tag=" + tag.getId().getCanonicalSQLName() + " --> equal="
          + tag.getId().getCanonicalSQLName().equals(t.getName()));
      // TODO: this comparison fails to include the catalog/schema. Fix!
      if (tag.getId().getCanonicalSQLName().equals(t.getName())) {
        return tag;
      }
    }
    return null;
  }

  public boolean includesTable(final JdbcTable t) {
    return this.getTableTag(t) != null;
  }

  public EnumTag getEnumTag(final JdbcTable t) {
    for (EnumTag tag : this.getFacetEnums()) {
      log.debug("enum tag=" + tag.getId().getCanonicalSQLName());
      // TODO: this comparison fails to include the catalog/schema. Fix!
      if (tag.getId().getCanonicalSQLName().equals(t.getName())) {
        return tag;
      }
    }
    return null;
  }

  public boolean includesEnum(final JdbcTable t) {
    return this.getEnumTag(t) != null;
  }

  public ViewTag getViewTag(final JdbcTable t) {
    for (ViewTag tag : this.getFacetViews()) {
      // TODO: this comparison fails to include the catalog/schema. Fix!
      if (tag.getId().getCanonicalSQLName().equals(t.getName())) {
        return tag;
      }
    }
    return null;
  }

  public boolean includesView(final JdbcTable t) {
    return this.getViewTag(t) != null;
  }

  // Getters

  public Set<String> getFacetNames() {
    return facetNames;
  }

  private FacetTag selected = null;

  private void computeSelectedFacets() {
    if (this.selected == null) {
      FacetTag selected = new FacetTag();
      for (FacetTag f : this.chosenFacets) {
        selected.mergeOther(f);
      }
    }
  }

  public List<TableTag> getFacetTables() {
    log.debug("this.chosenFacets=" + this.chosenFacets.stream().map(f -> f.getName()).collect(Collectors.joining(",")));
    if (this.chosenFacets.isEmpty()) {
      return this.allFacets.getTables();
    } else {
      computeSelectedFacets();
      return selected.getTables();
    }
  }

  public void includeInAllFacets(JdbcTable t, boolean isView, final DaosTag daosTag, final HotRodConfigTag config,
      final DatabaseAdapter adapter) throws InvalidConfigurationFileException {
    if (isView) {
      this.allFacets.includeView(t, daosTag, config, adapter);
    } else {
      this.allFacets.includeTable(t, daosTag, config, adapter);
    }
  }

  public List<TableTag> getAllTables() {
    return this.allFacets.getTables();
  }

  public List<ViewTag> getFacetViews() {
    if (this.chosenFacets.isEmpty()) {
      return this.allFacets.getViews();
    } else {
      computeSelectedFacets();
      return selected.getViews();
    }
  }

  public List<ViewTag> getAllViews() {
    return this.allFacets.getViews();
  }

  public List<EnumTag> getFacetEnums() {
    if (this.chosenFacets.isEmpty()) {
      return this.allFacets.getEnums();
    } else {
      computeSelectedFacets();
      return selected.getEnums();
    }
  }

  public List<EnumTag> getAllEnums() {
    return this.allFacets.getEnums();
  }

  public List<ExecutorTag> getFacetExecutors() {
    if (this.chosenFacets.isEmpty()) {
      return this.allFacets.getExecutors();
    } else {
      computeSelectedFacets();
      return selected.getExecutors();
    }
  }

  public List<ExecutorTag> getAllExecutors() {
    return this.allFacets.getExecutors();
  }

  public List<FacetTag> getFacets() {
    return facets;
  }

  public FacetTag getAssembledFacet(final String name) {
    return assembledFacets.get(name);
  }

  public TableTag findFacetTable(final DataSetMetadata metadata, final DatabaseAdapter adapter) {
    if (metadata == null) {
      return null;
    }
    for (TableTag t : this.getFacetTables()) {
      if (metadata.getId().equals(t.getId())) {
        return t;
      }
    }
    return null;
  }

  // TODO: Why is this method used?
  public EnumTag findFacetEnum(final DataSetMetadata metadata, final DatabaseAdapter adapter) {
    if (metadata == null) {
      return null;
    }
    for (EnumTag e : this.getFacetEnums()) {
      if (metadata.getId().equals(e.getId())) {
        return e;
      }
    }
    return null;
  }

  // TODO: Why is this method used?
  public ViewTag findFacetView(final DataSetMetadata metadata, final DatabaseAdapter adapter) {
    if (metadata == null) {
      return null;
    }
    for (ViewTag v : this.getFacetViews()) {
      if (metadata.getId().equals(v.getId())) {
        return v;
      }
    }
    return null;
  }

  // Location Listener

  public static class LocationListener extends Listener {

    private File file;

    private XMLStreamReader xsr;
    private Map<Object, Location> locations;

    public LocationListener(final File file, final XMLStreamReader xsr) {
      this.file = file;
      this.xsr = xsr;
      this.locations = new HashMap<Object, Location>();
    }

    @Override
    public void beforeUnmarshal(final Object target, final Object parent) {
      Location location = this.xsr.getLocation();
      this.locations.put(target, location);
      try {
        AbstractConfigurationTag locatable = (AbstractConfigurationTag) target;
        SourceLocation sourceLocation = new SourceLocation(this.file, location.getLineNumber(),
            location.getColumnNumber(), location.getCharacterOffset());
        locatable.setSourceLocation(sourceLocation);
      } catch (ClassCastException e) {
        // Not a SourceLocatable; do nothing.
      }
    }

    public File getFile() {
      return this.file;
    }

    public Location getLocation(final Object obj) {
      return this.locations.get(obj);
    }

  }

}
