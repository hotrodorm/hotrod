package org.hotrod.config;

import java.io.File;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.Unmarshaller.Listener;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.stream.Location;
import javax.xml.stream.XMLStreamReader;

import org.apache.log4j.Logger;
import org.hotrod.ant.ControlledException;
import org.hotrod.ant.UncontrolledException;
import org.hotrod.database.DatabaseAdapter;
import org.hotrod.exceptions.FacetNotFoundException;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.metadata.DataSetMetadata;
import org.hotrod.metadata.SelectDataSetMetadata;
import org.hotrod.runtime.dynamicsql.SourceLocation;
import org.nocrala.tools.database.tartarus.core.JdbcDatabase;
import org.nocrala.tools.database.tartarus.core.JdbcTable;

public abstract class AbstractHotRodConfigTag extends AbstractConfigurationTag {

  // Constants

  private static final Logger log = Logger.getLogger(AbstractHotRodConfigTag.class);

  // Properties

  private List<TableTag> tables = new ArrayList<TableTag>();
  private List<ViewTag> views = new ArrayList<ViewTag>();
  private List<EnumTag> enums = new ArrayList<EnumTag>();
  private List<CustomDAOTag> daos = new ArrayList<CustomDAOTag>();
  private List<SelectTag> selects = new ArrayList<SelectTag>();
  private List<FragmentTag> fragments = new ArrayList<FragmentTag>();
  private List<FacetTag> facets = new ArrayList<FacetTag>();
  private Map<String, FacetTag> assembledFacets = new HashMap<String, FacetTag>();

  private FacetTag allFacets = null;

  private Set<String> facetNames = null;
  private Set<FacetTag> chosenFacets = null;

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
  public void setDAO(final CustomDAOTag dao) {
    this.daos.add(dao);
  }

  @XmlElement
  public void setSelect(final SelectTag select) {
    this.selects.add(select);
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

  public void setChosenFacets(final Set<String> facetNames) throws FacetNotFoundException {
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

  protected void validateCommon(final HotRodConfigTag config, final File file, final Set<String> alreadyLoadedFileNames,
      final File parentFile, final DaosTag daosTag, final HotRodFragmentConfigTag fragmentConfig)
      throws InvalidConfigurationFileException, ControlledException, UncontrolledException {

    File basedir = file.getParentFile();

    log.debug("init");

    // DAOs

    for (TableTag t : this.tables) {
      t.validate(daosTag, config, fragmentConfig);
    }

    for (ViewTag v : this.views) {
      v.validate(daosTag, config, fragmentConfig);
    }

    for (EnumTag e : this.enums) {
      e.validate(daosTag, fragmentConfig);
    }

    for (CustomDAOTag dao : this.daos) {
      dao.validate(daosTag, fragmentConfig);
    }

    for (SelectTag s : this.selects) {
      s.validate(daosTag, config, fragmentConfig);
    }

    for (FacetTag f : this.facets) {
      f.validate(config, daosTag, fragmentConfig);
    }

    // Fragments

    for (FragmentTag f : this.fragments) {
      f.validate(config, basedir, alreadyLoadedFileNames, parentFile, daosTag);
      this.mergeFragment(f.getFragmentConfig());
    }

    // Assemble facets

    this.allFacets = new FacetTag();

    this.allFacets.mergeOther(this.tables, this.views, this.enums, this.daos, this.selects);

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
      log.debug(" - tables: " + t.getName());
    }

    for (ViewTag v : f.getViews()) {
      log.debug(" - views: " + v.getName());
    }

    for (EnumTag e : f.getEnums()) {
      log.debug(" - enum: " + e.getName());
    }

    for (CustomDAOTag dao : f.getDaos()) {
      log.debug(" - daos: " + dao.getJavaClassName());
    }

    for (SelectTag s : f.getSelects()) {
      log.debug(" - select '" + s.getJavaClassName() + "'");
    }
  }

  private void mergeFragment(final AbstractHotRodConfigTag other) {
    this.tables.addAll(other.tables);
    this.views.addAll(other.views);
    this.enums.addAll(other.enums);
    this.selects.addAll(other.selects);
    this.daos.addAll(other.daos);
    this.facets.addAll(other.facets);
  }

  public void validateAgainstDatabase(final JdbcDatabase db, final Connection conn, final DatabaseAdapter adapter)
      throws InvalidConfigurationFileException {

    for (TableTag t : this.getTables()) {
      t.validateAgainstDatabase(db, adapter);
    }

    for (ViewTag v : this.getViews()) {
      v.validateAgainstDatabase(db, adapter);
    }

    for (EnumTag e : this.getEnums()) {
      e.validateAgainstDatabase(db, conn, adapter);
    }

  }

  public TableTag getTableTag(final JdbcTable t) {
    for (TableTag tag : this.getTables()) {
      log.debug("table tag=" + tag.getName());
      if (tag.getName().equalsIgnoreCase(t.getName())) {
        return tag;
      }
    }
    return null;
  }

  public boolean includesTable(final JdbcTable t) {
    return this.getTableTag(t) != null;
  }

  public EnumTag getEnumTag(final JdbcTable t) {
    for (EnumTag tag : this.getEnums()) {
      log.debug("enum tag=" + tag.getName());
      if (tag.getName().equalsIgnoreCase(t.getName())) {
        return tag;
      }
    }
    return null;
  }

  public boolean includesEnum(final JdbcTable t) {
    return this.getEnumTag(t) != null;
  }

  public ViewTag getViewTag(final JdbcTable t) {
    for (ViewTag tag : this.getViews()) {
      if (tag.getName().equalsIgnoreCase(t.getName())) {
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

  public List<TableTag> getTables() {
    if (this.chosenFacets.isEmpty()) {
      return this.allFacets.getTables();
    } else {
      List<TableTag> subset = new ArrayList<TableTag>();
      for (FacetTag f : this.chosenFacets) {
        subset.addAll(f.getTables());
      }
      return subset;
    }
  }

  public List<TableTag> getAllTables() {
    return this.allFacets.getTables();
  }

  public List<ViewTag> getViews() {
    if (this.chosenFacets.isEmpty()) {
      return this.allFacets.getViews();
    } else {
      List<ViewTag> subset = new ArrayList<ViewTag>();
      for (FacetTag f : this.chosenFacets) {
        subset.addAll(f.getViews());
      }
      return subset;
    }
  }

  public List<ViewTag> getAllViews() {
    return this.allFacets.getViews();
  }

  public List<EnumTag> getEnums() {
    if (this.chosenFacets.isEmpty()) {
      return this.allFacets.getEnums();
    } else {
      List<EnumTag> subset = new ArrayList<EnumTag>();
      for (FacetTag f : this.chosenFacets) {
        subset.addAll(f.getEnums());
      }
      return subset;
    }
  }

  public List<EnumTag> getAllEnums() {
    return this.allFacets.getEnums();
  }

  public List<CustomDAOTag> getDAOs() {
    if (this.chosenFacets.isEmpty()) {
      return this.allFacets.getDaos();
    } else {
      List<CustomDAOTag> subset = new ArrayList<CustomDAOTag>();
      for (FacetTag f : this.chosenFacets) {
        subset.addAll(f.getDaos());
      }
      return subset;
    }
  }

  public List<CustomDAOTag> getAllDAOs() {
    return this.allFacets.getDaos();
  }

  public List<SelectTag> getSelects() {
    if (this.chosenFacets.isEmpty()) {
      return this.allFacets.getSelects();
    } else {
      List<SelectTag> subset = new ArrayList<SelectTag>();
      for (FacetTag f : this.chosenFacets) {
        subset.addAll(f.getSelects());
      }
      return subset;
    }
  }

  public List<SelectTag> getAllSelects() {
    return this.allFacets.getSelects();
  }

  public List<FacetTag> getFacets() {
    return facets;
  }

  public FacetTag getAssembledFacet(final String name) {
    return assembledFacets.get(name);
  }

  public TableTag findTable(final DataSetMetadata metadata, final DatabaseAdapter adapter) {
    if (metadata == null) {
      return null;
    }
    for (TableTag t : this.getTables()) {
      if (adapter.isTableIdentifier(metadata.getIdentifier().getSQLIdentifier(), t.getName())) {
        return t;
      }
    }
    return null;
  }

  public ViewTag findView(final DataSetMetadata metadata, final DatabaseAdapter adapter) {
    if (metadata == null) {
      return null;
    }
    for (ViewTag v : this.getViews()) {
      if (adapter.isTableIdentifier(metadata.getIdentifier().getSQLIdentifier(), v.getName())) {
        return v;
      }
    }
    return null;
  }

  public SelectTag findSelect(final SelectDataSetMetadata metadata, final DatabaseAdapter adapter) {
    if (metadata == null) {
      return null;
    }
    for (SelectTag v : this.getSelects()) {
      if (metadata.getSelectTag().getJavaClassName().equals(v.getJavaClassName())) {
        return v;
      }
    }
    return null;
  }

  // Location Listener

  public static interface SourceLocatable {

    void setSourceLocation(SourceLocation location);

    SourceLocation getSourceLocation();

  }

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
        SourceLocatable locatable = (SourceLocatable) target;
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
