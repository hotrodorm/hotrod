package org.hotrod.generator.jdbc;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Logger;

import org.hotrod.config.DisplayMode;
import org.hotrod.config.EnabledFKs;
import org.hotrod.config.HotRodConfigTag;
import org.hotrod.config.JDBCTag;
import org.hotrod.database.DatabaseAdapter;
import org.hotrod.exceptions.ControlledException;
import org.hotrod.exceptions.InvalidConfigurationFileException;
import org.hotrod.exceptions.UncontrolledException;
import org.hotrod.generator.Feedback;
import org.hotrod.generator.FileGenerator;
import org.hotrod.generator.Generator;
import org.hotrod.generator.HotRodContext;
import org.hotrod.generator.LiveGenerator;
import org.hotrod.generator.mybatisspring.DataSetLayout;
import org.hotrod.generator.mybatisspring.EntityDAORegistry;
import org.hotrod.generator.mybatisspring.EnumClass;
import org.hotrod.generator.mybatisspring.LayerConfigWriter;
import org.hotrod.generator.mybatisspring.Mapper;
import org.hotrod.generator.mybatisspring.ObjectAbstractVO;
import org.hotrod.generator.mybatisspring.ObjectDAO;
import org.hotrod.generator.mybatisspring.ObjectVO;
import org.hotrod.metadata.DataSetMetadata;
import org.hotrod.metadata.EnumDataSetMetadata;
import org.hotrod.metadata.Metadata;
import org.hotrod.metadata.VORegistry;
import org.nocrala.tools.database.tartarus.core.DatabaseLocation;
import org.nocrala.tools.database.tartarus.core.JdbcDatabase;

public class JDBCGenerator implements Generator, LiveGenerator {

  private static final Logger log = Logger.getLogger(JDBCGenerator.class.getName());

  private HotRodContext hc;

  protected DatabaseLocation dloc;
  protected DatabaseAdapter adapter;
  protected HotRodConfigTag config;
  protected JdbcDatabase db;
  private Metadata md;

  protected DisplayMode displayMode;
  protected Feedback feedback;

  private Long lastLog = null;

  private JDBCTag myBatisSpringTag;
  private DataSetLayout layout;

  private LinkedHashMap<DataSetMetadata, ObjectAbstractVO> abstractVos = new LinkedHashMap<DataSetMetadata, ObjectAbstractVO>();
  private LinkedHashMap<DataSetMetadata, ObjectVO> vos = new LinkedHashMap<DataSetMetadata, ObjectVO>();
  private LinkedHashMap<DataSetMetadata, ObjectDAO> daos = new LinkedHashMap<DataSetMetadata, ObjectDAO>();
  private LinkedHashMap<DataSetMetadata, Mapper> mappers = new LinkedHashMap<DataSetMetadata, Mapper>();
  private LinkedHashMap<EnumDataSetMetadata, EnumClass> enumClasses = new LinkedHashMap<EnumDataSetMetadata, EnumClass>();
  private List<ObjectAbstractVO> tableAbstractVOs = new ArrayList<ObjectAbstractVO>();

  private EntityDAORegistry entityDAORegistry = new EntityDAORegistry();

  private LayerConfigWriter layerConfigWriter;

  public JDBCGenerator(final HotRodContext hc, final EnabledFKs enabledFKs, final DisplayMode displayMode,
      final boolean incrementalMode, final Feedback feedback)
      throws UncontrolledException, ControlledException, InvalidConfigurationFileException {

    this.hc = hc;

    this.dloc = this.hc.getLoc();
    this.adapter = this.hc.getAdapter();
    this.config = this.hc.getConfig();
    this.db = this.hc.getDb();
    this.md = this.hc.getMetadata();

    this.displayMode = displayMode;
    this.feedback = feedback;

  }

  @Override
  public void prepareGeneration() throws UncontrolledException, ControlledException, InvalidConfigurationFileException {
  }

  @Override
  public void generate(FileGenerator fileGenerator) throws UncontrolledException, ControlledException {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void display(String txt) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public VORegistry getVORegistry() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public HotRodConfigTag getConfig() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void generate() throws UncontrolledException, ControlledException {
    // TODO Auto-generated method stub
    
  }

}
