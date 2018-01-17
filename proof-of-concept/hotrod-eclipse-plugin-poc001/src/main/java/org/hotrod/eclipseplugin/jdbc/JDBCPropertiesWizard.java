package org.hotrod.eclipseplugin.jdbc;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.hotrod.eclipseplugin.utils.SUtil;

public class JDBCPropertiesWizard extends Wizard {

  public class WizardProperties {
    private List<String> driverClassPathJars;
    private String driverClassName;
    private String url;
    private String username;
    private String password;
    private String catalog;
    private List<String> availableCatalogs;
    private String schema;
    private List<String> availableSchemas;
    private String generator;
    private List<String> availableGenerators;
  }

  private WizardProperties properties;

  private File projectPath;

  protected DriverPage driverPage;
  protected ConnectionPage connectionPage;
  protected SelectionPage selectionPage;

  public JDBCPropertiesWizard(final File projectPath) {
    super();
    this.projectPath = projectPath;
    this.properties = loadProperties();
    this.driverPage = new DriverPage(this.properties);
    this.connectionPage = new ConnectionPage(this.properties);
    this.selectionPage = new SelectionPage(this.properties);
    super.setHelpAvailable(false);
  }

  private WizardProperties loadProperties() {
    WizardProperties p = new WizardProperties();

    p.driverClassPathJars = new ArrayList<String>();
    p.driverClassPathJars.add("lib/jdbc/postgres-1.1.jar");
    p.driverClassPathJars.add("lib/my-license.jar");

    p.driverClassName = "my.postgres.driver.DriverClassName";

    p.url = "jdbc:derby://192.168.56.26:1527/hotrod;create=true";
    // p.url = "";
    p.username = "myusername";
    p.password = "mypassword";

    p.catalog = "cat001";
    p.availableCatalogs = new ArrayList<String>();
    p.availableCatalogs.add("catalog1");
    p.availableCatalogs.add("util");
    p.availableCatalogs.add("system");
    p.availableCatalogs.add("admin");
    p.availableCatalogs.add("cat001");

    p.schema = "STAGING";
    p.availableSchemas = new ArrayList<String>();
    p.availableSchemas.add("MAIN");
    p.availableSchemas.add("PROD");
    p.availableSchemas.add("DEV1");
    p.availableSchemas.add("DEV2");
    p.availableSchemas.add("STAGING");
    p.availableSchemas.add("QA");

    p.generator = "MyBatis Spring";
    p.availableGenerators = new ArrayList<String>();
    p.availableGenerators.add("MyBatis");
    p.availableGenerators.add("Spring JDBC");
    p.availableGenerators.add("MyBatis Spring");

    return p;
  }

  @Override
  public String getWindowTitle() {
    return "Connect to the database";
  }

  @Override
  public void addPages() {
    addPage(this.driverPage);
    addPage(this.connectionPage);
    addPage(this.selectionPage);
  }

  @Override
  public boolean performFinish() {
    System.out.println("username=" + properties.username);
    return true;
  }

  private boolean onLastPage = false;

  @Override
  public boolean canFinish() {
    boolean complete = !SUtil.isEmpty(this.driverPage.getFieldDriverClassName().getText()) //
        && !SUtil.isEmpty(this.connectionPage.getFieldURL().getText()) //
    ;
    boolean finish = complete && this.onLastPage;
    System.out
        .println("[canFinish()]: complete=" + complete + " onLastPage=" + this.onLastPage + " -> finish=" + finish);
    return this.onLastPage;
  }

  // =======================
  // === Page 1 - Driver ===
  // =======================

  public static class DriverPage extends WizardPage {

    private static final String[] FILTER_NAMES = { "Java Archive (*.jar)", "All Files (*.*)" };
    private static final String[] FILTER_EXTS = { "*.jar", "*.*" };

    private WizardProperties properties;

    private Composite container;
    private org.eclipse.swt.widgets.List fieldJars;
    private Text fieldDriverClassName;

    public DriverPage(final WizardProperties properties) {
      super("Select the JDBC driver");
      this.properties = properties;
      setTitle("Select the JDBC driver");
      setDescription("Add the JDBC driver jar file(s) and specify the driver class name");
    }

    @Override
    public void createControl(final Composite parent) {
      this.container = new Composite(parent, SWT.NONE);
      {
        GridLayout layout = new GridLayout();
        layout.numColumns = 5;
        this.container.setLayout(layout);
      }

      // Driver JAR files

      Label label = new Label(this.container, SWT.NONE);
      label.setText("Driver jar files:");
      {
        GridData gridData = new GridData(GridData.FILL, GridData.CENTER, true, false);
        gridData.horizontalSpan = 5;
        label.setLayoutData(gridData);
      }

      this.fieldJars = new org.eclipse.swt.widgets.List(this.container, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
      {
        GridData gridData = new GridData(GridData.FILL, GridData.VERTICAL_ALIGN_BEGINNING, true, true);
        gridData.horizontalSpan = 4;
        gridData.verticalSpan = 2;
        gridData.minimumHeight = 172;
        gridData.verticalAlignment = GridData.VERTICAL_ALIGN_BEGINNING;
        this.fieldJars.setLayoutData(gridData);
      }
      for (String jarPath : this.properties.driverClassPathJars) {
        this.fieldJars.add(jarPath);
      }

      Button addJar = new Button(this.container, SWT.PUSH);
      {
        GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_BEGINNING);
        addJar.setLayoutData(gridData);
      }
      addJar.setText("Add...");
      addJar.addSelectionListener(new SelectionAdapter() {
        @Override
        public void widgetSelected(final SelectionEvent event) {
          FileDialog fileDialog = new FileDialog(getShell(), SWT.MULTI);
          fileDialog.setFilterNames(FILTER_NAMES);
          fileDialog.setFilterExtensions(FILTER_EXTS);
          String basePath;
          // TODO: get project path in here!
          // basePath = projectPath.getPath();
          basePath = "/home/valarcon/git/hotrod/proof-of-concept/hotrod-eclipse-plugin-poc001/test-workspace/project002";
          fileDialog.setFilterPath(basePath);
          fileDialog.open();
          String path = fileDialog.getFilterPath();
          File folder = new File(path);
          System.out.println("path=" + path);
          for (String fileName : fileDialog.getFileNames()) {
            System.out.println("fileName=" + fileName);
            File f = new File(folder, fileName);
            fieldJars.add(f.getPath());
          }
        }
      });

      Button deleteJar = new Button(this.container, SWT.PUSH);
      {
        GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_BEGINNING);
        deleteJar.setLayoutData(gridData);
      }
      deleteJar.setText("Delete");
      deleteJar.addSelectionListener(new SelectionListener() {

        @Override
        public void widgetSelected(final SelectionEvent event) {
          System.out.println("-> [deleteJar] selected.");
          for (String fileName : fieldJars.getSelection()) {
            System.out.println(" * deleting: " + fileName);
            fieldJars.remove(fileName);
          }
        }

        @Override
        public void widgetDefaultSelected(final SelectionEvent event) {
          System.out.println("-> [deleteJar] default selected.");
        }

      });

      // Driver class name

      Label label1 = new Label(this.container, SWT.NONE);
      label1.setText("Driver class name:");
      {
        GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING, GridData.VERTICAL_ALIGN_BEGINNING, true,
            true);
        gridData.verticalAlignment = GridData.VERTICAL_ALIGN_BEGINNING;
        label1.setLayoutData(gridData);
      }

      this.fieldDriverClassName = new Text(this.container, SWT.BORDER | SWT.SINGLE);
      {
        GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
        // GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL,
        // GridData.CENTER, true, true);
        // gridData.horizontalSpan = 4;
        gridData.grabExcessHorizontalSpace = true;
        this.fieldDriverClassName.setLayoutData(gridData);
      }
      this.fieldDriverClassName.setText(this.properties.driverClassName);
      this.fieldDriverClassName.addKeyListener(new KeyListener() {

        @Override
        public void keyPressed(final KeyEvent e) {
          // Ignore
        }

        @Override
        public void keyReleased(final KeyEvent e) {
          validate();
        }

      });

      // Page assembled

      setControl(this.container);

      validate();

    }

    private void validate() {
      if (!SUtil.isEmpty(this.fieldDriverClassName.getText())) {
        setPageComplete(true);
      } else {
        setPageComplete(false);
      }
    }

    // Getters

    public WizardProperties getProperties() {
      return properties;
    }

    public org.eclipse.swt.widgets.List getFieldJars() {
      return fieldJars;
    }

    public Text getFieldDriverClassName() {
      return fieldDriverClassName;
    }

  }

  // === Page 2 - Connection ===

  public class ConnectionPage extends WizardPage {

    private WizardProperties properties;

    private Composite container;
    private Text fieldURL;
    private Text fieldUsername;
    private Text fieldPassword;

    public ConnectionPage(final WizardProperties properties) {
      super("Connection Properties");
      this.properties = properties;
      setTitle("Connection Properties");
      setDescription("Specify the JDBC connection properties");
    }

    @Override
    public void createControl(final Composite parent) {

      this.container = new Composite(parent, SWT.NONE);
      {
        GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        this.container.setLayout(layout);
      }

      // URL

      Label label1 = new Label(this.container, SWT.NONE);
      label1.setText("URL:");

      this.fieldURL = new Text(this.container, SWT.BORDER | SWT.SINGLE);
      this.fieldURL.setText(this.properties.url);
      {
        // GridData gridData = new GridData(GridData.FILL, GridData.CENTER,
        // true, false);
        GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
        // gridData.horizontalSpan = 3;
        // gridData.minimumWidth = 280;
        gridData.widthHint = 400;
        this.fieldURL.setLayoutData(gridData);
      }
      this.fieldURL.addKeyListener(new KeyListener() {

        @Override
        public void keyPressed(final KeyEvent e) {
          // Ignore
        }

        @Override
        public void keyReleased(final KeyEvent e) {
          validate();
        }

      });

      // Username

      Label label2 = new Label(this.container, SWT.NONE);
      label2.setText("Username:");

      this.fieldUsername = new Text(this.container, SWT.BORDER | SWT.SINGLE);
      this.fieldUsername.setText(this.properties.username);

      // Password

      Label label3 = new Label(this.container, SWT.NONE);
      label3.setText("Password:");

      this.fieldPassword = new Text(this.container, SWT.BORDER | SWT.SINGLE);
      this.fieldPassword.setText(this.properties.password);

      // Page assembled

      setControl(this.container);

      this.validate();

    }

    @Override
    public IWizardPage getNextPage() {
      onLastPage = true;
      return super.getNextPage();
    }

    private void validate() {
      if (!SUtil.isEmpty(this.fieldURL.getText())) {
        setPageComplete(true);
      } else {
        setPageComplete(false);
      }
    }

    // Getters

    public Text getFieldURL() {
      return fieldURL;
    }

    public Text getFieldUsername() {
      return fieldUsername;
    }

    public Text getFieldPassword() {
      return fieldPassword;
    }

  }

  // === Page 3 - Selection ===

  // TODO

  public class SelectionPage extends WizardPage {

    private WizardProperties properties;

    private Composite container;
    private Combo fieldCatalog;
    private Combo fieldSchema;
    private Combo fieldGenerator;

    public SelectionPage(final WizardProperties properties) {
      super("Select the database schema");
      System.out.println("[[ SelectionPage ]]");
      this.properties = properties;
      setTitle("Select the database schema");
      setDescription("Pick a database schema and a generator");
      // setControl(text1);
    }

    @Override
    public void createControl(final Composite parent) {
      System.out.println("[[ SelectionPage ]] - createControls()");

      this.container = new Composite(parent, SWT.NONE);
      {
        GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        this.container.setLayout(layout);
      }

      // Catalog

      Label label1 = new Label(this.container, SWT.NONE);
      label1.setText("Catalog:");

      this.fieldCatalog = new Combo(this.container, SWT.NULL);
      this.fieldCatalog.setItems(this.properties.availableCatalogs.toArray(new String[0]));
      this.fieldCatalog.setText(this.properties.catalog);

      // Schema

      Label label2 = new Label(this.container, SWT.NONE);
      label2.setText("Schema:");

      this.fieldSchema = new Combo(this.container, SWT.NULL);
      this.fieldSchema.setItems(this.properties.availableSchemas.toArray(new String[0]));
      this.fieldSchema.setText(this.properties.schema);

      // Generator

      Label label3 = new Label(this.container, SWT.NONE);
      label3.setText("Generator:");

      this.fieldGenerator = new Combo(this.container, SWT.NULL);
      this.fieldGenerator.setItems(this.properties.availableGenerators.toArray(new String[0]));
      this.fieldGenerator.setText(this.properties.generator);

      // Page assembled

      setControl(this.container);

      this.validate();
    }

    @Override
    public IWizardPage getPreviousPage() {
      onLastPage = false;
      return super.getPreviousPage();
    }

    private void validate() {
      // if (!SUtil.isEmpty(this.fieldURL.getText())) {
      setPageComplete(true);
      // } else {
      // setPageComplete(false);
      // }
    }

  }

}