package org.hotrod.config;

import java.io.File;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

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
import org.hotrod.generator.HotRodGenerator;
import org.hotrod.metadata.DataSetMetadata;
import org.hotrod.metadata.SelectDataSetMetadata;
import org.hotrod.runtime.dynamicsql.SourceLocation;
import org.hotrod.utils.Correlator;
import org.hotrod.utils.Correlator.CorrelatedEntry;
import org.hotrod.utils.FileRegistry;
import org.nocrala.tools.database.tartarus.core.JdbcTable;

public abstract class AbstractHotRodConfigTag extends AbstractConfigurationTag
    implements GenerationUnit<AbstractHotRodConfigTag> {

  private static final long serialVersionUID = 1L;

  // Constants

  private static final Logger log = LogManager.getLogger(AbstractHotRodConfigTag.class);

  // Properties

  private List<TableTag> tables = new ArrayList<TableTag>();
  private List<ViewTag> views = new ArrayList<ViewTag>();
  private List<EnumTag> enums = new ArrayList<EnumTag>();
  private List<ExecutorTag> executors = new ArrayList<ExecutorTag>();
  private List<SelectClassTag> selects = new ArrayList<SelectClassTag>();
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
  public void setSelect(final SelectClassTag select) {
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

  protected void validateCommon(final HotRodConfigTag config, final File file, final FileRegistry fileRegistry,
      final File parentFile, final DaosTag daosTag, final HotRodFragmentConfigTag fragmentConfig,
      final DatabaseAdapter adapter)
      throws InvalidConfigurationFileException, ControlledException, UncontrolledException {

    log.debug("init");

    File parentDir = file.getParentFile();

    // DAOs

    for (TableTag t : this.tables) {
      t.validate(daosTag, config, fragmentConfig, adapter);
    }
    Collections.sort(this.tables, new Comparator<TableTag>() {
      @Override
      public int compare(final TableTag a, final TableTag b) {
        return a.getId().compareTo(b.getId());
      }
    });
    super.addChildren(this.tables);

    for (EnumTag e : this.enums) {
      e.validate(daosTag, config, fragmentConfig, adapter);
    }
    Collections.sort(this.enums, new Comparator<EnumTag>() {
      @Override
      public int compare(final EnumTag a, final EnumTag b) {
        return a.getId().compareTo(b.getId());
      }
    });
    super.addChildren(this.enums);

    for (ViewTag v : this.views) {
      v.validate(daosTag, config, fragmentConfig, adapter);
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

    for (SelectClassTag s : this.selects) {
      s.validate(daosTag, config, fragmentConfig, adapter);
    }
    Collections.sort(this.selects, new Comparator<SelectClassTag>() {
      @Override
      public int compare(final SelectClassTag a, final SelectClassTag b) {
        return a.getJavaClassName().compareTo(b.getJavaClassName());
      }
    });
    super.addChildren(this.selects);

    for (FacetTag f : this.facets) {
      f.validate(config, daosTag, fragmentConfig, adapter);
    }
    super.addChildren(this.facets);

    // Fragments

    for (FragmentTag f : this.fragments) {
      f.validate(config, parentDir, fileRegistry, parentFile, daosTag, adapter);
      this.mergeFragment(f.getFragmentConfig());
      super.addChild(f);
    }

    // Assemble facets

    this.allFacets = new FacetTag();

    this.allFacets.mergeOther(this.tables, this.views, this.enums, this.executors, this.selects);

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

    // Validate extends (across all facets)

    for (TableTag t : this.tables) {
      t.validateAllExtends(this.tables);
    }

    // Validate extends (within selected facets only)

    for (TableTag t : this.getFacetTables()) {
      t.validateFacetExtends(this.getFacetTables());
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

    for (SelectClassTag s : f.getSelects()) {
      log.debug(" - select '" + s.getJavaClassName() + "'");
    }
  }

  private void mergeFragment(final AbstractHotRodConfigTag other) {
    this.tables.addAll(other.tables);
    this.views.addAll(other.views);
    this.enums.addAll(other.enums);
    this.selects.addAll(other.selects);
    this.executors.addAll(other.executors);
    this.facets.addAll(other.facets);
  }

  public void validateAgainstDatabase(final HotRodGenerator generator, final Connection conn,
      final DatabaseAdapter adapter) throws InvalidConfigurationFileException {

    for (TableTag t : this.getFacetTables()) {
      t.validateAgainstDatabase(generator);
    }

    for (ViewTag v : this.getFacetViews()) {
      v.validateAgainstDatabase(generator);
    }

    for (EnumTag e : this.getFacetEnums()) {
      e.validateAgainstDatabase(generator, conn, adapter);
    }

    for (ExecutorTag d : this.getFacetExecutors()) {
      d.validateAgainstDatabase(generator);
    }

  }

  public TableTag getTableTag(final JdbcTable t) {
    for (TableTag tag : this.getFacetTables()) {
      log.debug("Comparing table '" + t.getName() + "' -- tag=" + tag.getId().getCanonicalSQLName());
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

  public List<TableTag> getFacetTables() {
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

  public List<ViewTag> getFacetViews() {
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

  public List<EnumTag> getFacetEnums() {
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

  public List<ExecutorTag> getFacetExecutors() {
    if (this.chosenFacets.isEmpty()) {
      return this.allFacets.getExecutors();
    } else {
      List<ExecutorTag> subset = new ArrayList<ExecutorTag>();
      for (FacetTag f : this.chosenFacets) {
        subset.addAll(f.getExecutors());
      }
      return subset;
    }
  }

  public List<ExecutorTag> getAllExecutors() {
    return this.allFacets.getExecutors();
  }

  public List<SelectClassTag> getFacetSelects() {
    if (this.chosenFacets.isEmpty()) {
      return this.allFacets.getSelects();
    } else {
      List<SelectClassTag> subset = new ArrayList<SelectClassTag>();
      for (FacetTag f : this.chosenFacets) {
        subset.addAll(f.getSelects());
      }
      return subset;
    }
  }

  public List<SelectClassTag> getAllSelects() {
    return this.allFacets.getSelects();
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

  // TODO: Why is this methods used?
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

  // TODO: Why is this methods used?
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

  // TODO: Why is this methods used?
  public SelectClassTag findFacetSelect(final SelectDataSetMetadata metadata, final DatabaseAdapter adapter) {
    if (metadata == null) {
      return null;
    }
    for (SelectClassTag v : this.getFacetSelects()) {
      if (metadata.getSelectTag().getJavaClassName().equals(v.getJavaClassName())) {
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

  // Update generated cache

  protected TableTag add(final TableTag t) {
    TableTag d = t.duplicate();
    this.tables.add(d);
    return d;
  }

  protected void remove(final TableTag t) {
    for (Iterator<TableTag> it = this.tables.iterator(); it.hasNext();) {
      TableTag current = it.next();
      if (current.getId().equals(t.getId())) {
        it.remove();
        return;
      }
    }
  }

  protected TableTag replace(final TableTag t) {
    for (ListIterator<TableTag> it = this.tables.listIterator(); it.hasNext();) {
      TableTag current = it.next();
      if (current.getId().equals(t.getId())) {
        TableTag d = t.duplicate();
        it.set(d);
        return d;
      }
    }
    return null;
  }

  protected EnumTag add(final EnumTag e) {
    EnumTag d = e.duplicate();
    this.enums.add(d);
    return d;
  }

  protected void remove(final EnumTag e) {
    for (Iterator<EnumTag> it = this.enums.iterator(); it.hasNext();) {
      EnumTag current = it.next();
      if (current.getId().equals(e.getId())) {
        it.remove();
        return;
      }
    }
  }

  protected EnumTag replace(final EnumTag e) {
    for (ListIterator<EnumTag> it = this.enums.listIterator(); it.hasNext();) {
      EnumTag current = it.next();
      if (current.getId().equals(e.getId())) {
        EnumTag d = e.duplicate();
        it.set(d);
        return d;
      }
    }
    return null;
  }

  protected ViewTag add(final ViewTag v) {
    ViewTag d = v.duplicate();
    this.views.add(d);
    return d;
  }

  protected void remove(final ViewTag v) {
    for (Iterator<ViewTag> it = this.views.iterator(); it.hasNext();) {
      ViewTag current = it.next();
      if (current.getId().equals(v.getId())) {
        it.remove();
        return;
      }
    }
  }

  protected ViewTag replace(final ViewTag v) {
    for (ListIterator<ViewTag> it = this.views.listIterator(); it.hasNext();) {
      ViewTag current = it.next();
      if (current.getId().equals(v.getId())) {
        ViewTag d = v.duplicate();
        it.set(d);
        return d;
      }
    }
    return null;
  }

  protected ExecutorTag add(final ExecutorTag x) {
    ExecutorTag d = x.duplicate();
    this.executors.add(d);
    return d;
  }

  protected void remove(final ExecutorTag x, final DatabaseAdapter adapter) {
    for (Iterator<ExecutorTag> it = this.executors.iterator(); it.hasNext();) {
      ExecutorTag current = it.next();
      if (adapter.equalConfigNames(current.getJavaClassName(), x.getJavaClassName())) {
        it.remove();
        return;
      }
    }
  }

  protected ExecutorTag replace(final ExecutorTag x, final DatabaseAdapter adapter) {
    for (ListIterator<ExecutorTag> it = this.executors.listIterator(); it.hasNext();) {
      ExecutorTag current = it.next();
      if (adapter.equalConfigNames(current.getJavaClassName(), x.getJavaClassName())) {
        ExecutorTag d = x.duplicate();
        it.set(d);
        return d;
      }
    }
    return null;
  }

  // Merging logic

  protected boolean commonSameKey(final AbstractConfigurationTag fresh) {
    return true;
  }

  protected boolean commonCopyNonKeyProperties(final AbstractConfigurationTag fresh) {
    try {
      AbstractHotRodConfigTag f = (AbstractHotRodConfigTag) fresh;
      boolean different = !same(fresh);

      super.coreCopyNonKeyProperties(fresh);
      this.tables = f.tables;
      this.views = f.views;
      this.enums = f.enums;
      this.executors = f.executors;
      this.selects = f.selects;
      this.fragments = f.fragments;
      this.facets = f.facets;
      this.assembledFacets = f.assembledFacets;
      this.allFacets = f.allFacets;
      this.facetNames = f.facetNames;
      this.chosenFacets = f.chosenFacets;

      return different;
    } catch (ClassCastException e) {
      return false;
    }
  }

  protected boolean commonSame(final AbstractConfigurationTag fresh) {
    try {
      @SuppressWarnings("unused")
      AbstractHotRodConfigTag f = (AbstractHotRodConfigTag) fresh;
      return true;
    } catch (ClassCastException e) {
      return false;
    }
  }

  // Update generated cache

  public boolean commonConcludeGeneration(final AbstractConfigurationTag unitCache, final DatabaseAdapter adapter) {

    log.debug("mark 1");

    boolean failedInnerGeneration = false;

    AbstractHotRodConfigTag cache = (AbstractHotRodConfigTag) unitCache;

    // Tables

    log.debug("mark 2 failedInnerGeneration=" + failedInnerGeneration);

    for (CorrelatedEntry<TableTag> cor : Correlator.correlate(this.getFacetTables(), cache.getFacetTables(),
        new Comparator<TableTag>() {
          @Override
          public int compare(TableTag o1, TableTag o2) {
            return o1.getId().compareTo(o2.getId());
          }
        })) {

      TableTag t = cor.getLeft();
      TableTag c = cor.getRight();

      log.info("mark 2 t=" + (t == null ? "null" : t.getId().getCanonicalSQLName()) + " - c="
          + (c == null ? "null" : c.getId().getCanonicalSQLName()));

      if (t != null && t.isToBeGenerated()) {
        log.debug("mark 2.1 - fail t=" + t.getId().getCanonicalSQLName());
        failedInnerGeneration = true;
      }
      if (t != null && c == null) {
        if (t.isGenerationComplete()) {
          c = cache.add(t); // add to the cache.
          if (!t.concludeGeneration(c, adapter)) {
            log.debug("mark 2.2 - fail.");
            failedInnerGeneration = true;
          }
        }
      }
      if (t == null && c != null) {
        cache.remove(t); // remove from the cache.
      }
      if (t != null && c != null) {
        log.debug("mark 2.3 generate=" + t.getGenerate());
        if (t.isGenerationComplete()) {
          log.debug("mark 2.3.1");
          c = cache.replace(t); // replace the cache.
        }
        if (!t.concludeGeneration(c, adapter)) {
          log.debug("mark 2.3.2 - fail.");
          failedInnerGeneration = true;
        }
      }

    }

    // Enums

    log.debug("mark 3 failedInnerGeneration=" + failedInnerGeneration);

    for (CorrelatedEntry<EnumTag> cor : Correlator.correlate(this.getFacetEnums(), cache.getFacetEnums(),
        new Comparator<EnumTag>() {
          @Override
          public int compare(EnumTag o1, EnumTag o2) {
            return o1.getId().compareTo(o2.getId());
          }
        })) {

      EnumTag t = cor.getLeft();
      EnumTag c = cor.getRight();

      if (t != null && t.isToBeGenerated()) {
        failedInnerGeneration = true;
      }
      if (t != null && c == null) {
        if (t.isGenerationComplete()) {
          c = cache.add(t); // add to the cache.
          if (!t.concludeGeneration(c, adapter)) {
            failedInnerGeneration = true;
          }
        }
      }
      if (t == null && c != null) {
        cache.remove(t); // remove from the cache.
      }
      if (t != null && c != null) {
        if (t.isGenerationComplete()) {
          c = cache.replace(t); // replace the cache.
        }
        if (!t.concludeGeneration(c, adapter)) {
          failedInnerGeneration = true;
        }
      }

    }

    // Views

    log.debug("mark 4 failedInnerGeneration=" + failedInnerGeneration);

    for (CorrelatedEntry<ViewTag> cor : Correlator.correlate(this.getFacetViews(), cache.getFacetViews(),
        new Comparator<ViewTag>() {
          @Override
          public int compare(ViewTag o1, ViewTag o2) {
            return o1.getId().compareTo(o2.getId());
          }
        })) {

      ViewTag t = cor.getLeft();
      ViewTag c = cor.getRight();

      log.info("view 2 t=" + (t == null ? "null" : t.getId().getCanonicalSQLName()) + " - c="
          + (c == null ? "null" : c.getId().getCanonicalSQLName()));

      if (t != null && t.isToBeGenerated()) {
        failedInnerGeneration = true;
      }
      if (t != null && c == null) {
        if (t.isGenerationComplete()) {
          c = cache.add(t); // add to the cache.
          if (!t.concludeGeneration(c, adapter)) {
            failedInnerGeneration = true;
          }
        }
      }
      if (t == null && c != null) {
        cache.remove(t); // remove from the cache.
      }
      if (t != null && c != null) {
        if (t.isGenerationComplete()) {
          c = cache.replace(t); // replace the cache.
        }
        if (!t.concludeGeneration(c, adapter)) {
          failedInnerGeneration = true;
        }
      }

    }

    // Executors

    log.debug("mark 5 failedInnerGeneration=" + failedInnerGeneration);

    for (CorrelatedEntry<ExecutorTag> cor : Correlator.correlate(this.getFacetExecutors(), cache.getFacetExecutors(),
        new Comparator<ExecutorTag>() {
          @Override
          public int compare(ExecutorTag o1, ExecutorTag o2) {
            return adapter.canonizeName(o1.getJavaClassName(), false)
                .compareTo(adapter.canonizeName(o2.getJavaClassName(), false));
          }
        })) {

      ExecutorTag t = cor.getLeft();
      ExecutorTag c = cor.getRight();

      if (t != null && t.isToBeGenerated()) {
        failedInnerGeneration = true;
      }
      if (t != null && c == null) {
        if (t.isGenerationComplete()) {
          c = cache.add(t); // add to the cache.
          if (!t.concludeGeneration(c, adapter)) {
            failedInnerGeneration = true;
          }
        }
      }
      if (t == null && c != null) {
        cache.remove(t, adapter); // remove from the cache.
      }
      if (t != null && c != null) {
        if (t.isGenerationComplete()) {
          c = cache.replace(t, adapter); // replace the cache.
        }
        if (!t.concludeGeneration(c, adapter)) {
          failedInnerGeneration = true;
        }
      }

    }

    log.debug("mark 10");

    // Fragments

    if (!this.concludeFragmentGeneration()) {
      failedInnerGeneration = true;
    }

    // Complete

    return !failedInnerGeneration;
  }

  // Conclude

  protected boolean concludeFragmentGeneration() {
    boolean concluded = true;
    for (TableTag t : this.tables) {
      concluded &= t.getStatus() == TagStatus.UP_TO_DATE;
    }
    for (ViewTag t : this.views) {
      concluded &= t.getStatus() == TagStatus.UP_TO_DATE;
    }
    for (EnumTag t : this.enums) {
      concluded &= t.getStatus() == TagStatus.UP_TO_DATE;
    }
    for (ExecutorTag t : this.executors) {
      concluded &= t.getStatus() == TagStatus.UP_TO_DATE;
    }
    for (FragmentTag f : this.fragments) {
      concluded &= f.concludeFragmentGeneration();
    }
    log.info("::::::::::::::::::::::: concluded=" + concluded + " (" + this.getInternalCaption() + ")");
    if (concluded) {
      markConcluded();
    }
    return concluded;
  }

}
