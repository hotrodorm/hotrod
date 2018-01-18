package org.hotrod.eclipseplugin.jdbc;

import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.dialogs.MessageDialog;
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
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.hotrod.eclipseplugin.jdbc.LocalProperties.CouldNotLoadPropertiesException;
import org.hotrod.eclipseplugin.jdbc.LocalProperties.CouldNotSavePropertiesException;
import org.hotrod.eclipseplugin.jdbc.LocalProperties.FileProperties;
import org.hotrod.eclipseplugin.treeview.MainConfigFace;
import org.hotrod.eclipseplugin.utils.FUtil;
import org.hotrod.eclipseplugin.utils.SUtil;

public class ConnectToDatabaseWizard extends Wizard implements NavigationAwareWizard {

  private MainConfigFace mainConfigFace;
  private Shell shell;

  private LocalProperties properties;
  private FileProperties fileProperties;

  protected DriverPage driverPage;
  protected ConnectionPage connectionPage;
  protected SelectionPage selectionPage;
  protected ConfirmationPage confirmationPage;

  protected DynamicallyLoadedDriver driver = null;
  protected SQLException driverException = null;

  protected Connection connection = null;
  protected SQLException connectionException = null;

  private List<String> availableCatalogs = null;
  private List<String> availableSchemas = null;

  private String selectionMessage = null;

  private boolean onLastPage = false;

  private static String[] AVAILABLE_GENERATORS = { "MyBatis", "Spring JDBC", "MyBatis Spring" };
  private static Set<String> AVAILABLE_GENERATORS_SET = new HashSet<String>(Arrays.asList(AVAILABLE_GENERATORS));

  public ConnectToDatabaseWizard(final MainConfigFace mainConfigFace, final Shell shell) {
    super();
    this.mainConfigFace = mainConfigFace;
    this.shell = shell;
    try {
      this.properties = LocalProperties.load(this.mainConfigFace.getProject());
    } catch (CouldNotLoadPropertiesException e) {
      MessageDialog.openError(this.shell, "Invalid HotRod Plugin's properties file",
          "Error: " + e.getMessage() + "\n\nWill load an empty properties file that will overwrite the existing one.");
      this.properties = new LocalProperties(this.mainConfigFace.getProject());
    }

    this.fileProperties = this.properties.getFileProperties(this.mainConfigFace.getRelativePath());
    if (this.fileProperties == null) {
      this.fileProperties = new FileProperties(this.mainConfigFace.getRelativePath());
    }

    this.driverPage = new DriverPage();
    this.connectionPage = new ConnectionPage();
    this.selectionPage = new SelectionPage();
    this.confirmationPage = new ConfirmationPage();
    super.setHelpAvailable(false);
  }

  @Override
  public void preprocessBackButton(final IWizardPage currentPage) {
    System.out.println("... preprocessBackButton() currentPage=" + currentPage);
    if (currentPage == this.confirmationPage) {
      this.onLastPage = false;
    }
  }

  @Override
  public void preprocessNextButton(final IWizardPage currentPage) {
    System.out.println("... preprocessNextButton()");

    if (currentPage == this.driverPage) {

      // Next on the Driver Page

      this.driver = null;
      this.driverException = null;
      System.out.println("... --> on DriverPage");
      IProject project = this.mainConfigFace.getProject();
      List<String> driverClassPath = Arrays.asList(this.driverPage.fieldClassPathEntries.getItems());
      String driverClassName = this.driverPage.fieldDriverClassName.getText();
      try {
        this.driver = new DynamicallyLoadedDriver(project, driverClassPath, driverClassName);
        System.out.println("... --> driver, success");
      } catch (SQLException e) {
        this.driverException = e;
        System.out.println("... --> driver, failure");
      }

    } else if (currentPage == this.connectionPage) {

      // Next on the Connection Page

      System.out.println("... --> on ConnectionPage");

      this.connection = null;
      this.connectionException = null;
      try {
        String url = this.connectionPage.getFieldURL().getText();
        String username = this.connectionPage.getFieldUsername().getText();
        String password = this.connectionPage.getFieldPassword().getText();
        this.connection = this.driver.getConnection(url, username, password);
        retrieveCatalogsAndSchemas();
        this.selectionPage.updateAvailableCatalogsAndSchemas();
        System.out.println("... --> conn, success");

      } catch (SQLException e) {
        if (this.connection != null) {
          try {
            this.connection.close();
          } catch (SQLException e1) {
            // Ignore
          } finally {
            this.connection = null;
          }
        }
        this.connectionException = e;
        System.out.println("... --> conn, failure");
      }

    } else if (currentPage == this.selectionPage) {

      // Next on the Selection Page

      this.selectionMessage = null;

      if (this.availableCatalogs != null) {
        if (!this.availableCatalogs.contains(this.selectionPage.getFieldCatalog().getText())) {
          this.selectionMessage = "Invalid catalog name.";
          return;
        }
      }

      if (this.availableSchemas != null) {
        if (!this.availableSchemas.contains(this.selectionPage.getFieldSchema().getText())) {
          this.selectionMessage = "Invalid schema name.";
          return;
        }
      }

      if (!AVAILABLE_GENERATORS_SET.contains(this.selectionPage.getFieldGenerator().getText())) {
        this.selectionMessage = "Invalid generator name.";
        return;
      }

      this.onLastPage = true;

    }

  }

  private void retrieveCatalogsAndSchemas() throws SQLException {
    DatabaseMetaData dm = this.connection.getMetaData();

    // Catalogs

    if (dm.supportsCatalogsInTableDefinitions()) {
      ResultSet rs = null;
      try {
        this.availableCatalogs = new ArrayList<String>();
        rs = dm.getCatalogs();
        while (rs.next()) {
          String catalog = rs.getString(1);
          this.availableCatalogs.add(catalog);
        }
      } finally {
        if (rs != null) {
          try {
            rs.close();
          } catch (Exception e) {
            // Ignore
          }
        }
      }
    }

    // Schemas

    if (dm.supportsSchemasInTableDefinitions()) {
      ResultSet rs = null;
      try {
        this.availableSchemas = new ArrayList<String>();
        rs = dm.getSchemas();
        while (rs.next()) {
          String schema = rs.getString(1);
          this.availableSchemas.add(schema);
        }
      } finally {
        if (rs != null) {
          try {
            rs.close();
          } catch (Exception e) {
            // Ignore
          }
        }
      }
    }

  }

  // private WizardProperties loadProperties() {
  // WizardProperties p = new WizardProperties();
  //
  // p.driverClassPathJars = new ArrayList<String>();
  // p.driverClassPathJars.add("lib/jdbc/postgres-1.1.jar");
  // p.driverClassPathJars.add("lib/my-license.jar");
  //
  // p.driverClassName = "my.postgres.driver.DriverClassName";
  //
  // p.url = "jdbc:derby://192.168.56.26:1527/hotrod;create=true";
  // // p.url = "";
  // p.username = "myusername";
  // p.password = "mypassword";
  //
  // p.catalog = "cat001";
  // p.availableCatalogs = new ArrayList<String>();
  // p.availableCatalogs.add("catalog1");
  // p.availableCatalogs.add("util");
  // p.availableCatalogs.add("system");
  // p.availableCatalogs.add("admin");
  // p.availableCatalogs.add("cat001");
  //
  // p.schema = "STAGING";
  // p.availableSchemas = new ArrayList<String>();
  // p.availableSchemas.add("MAIN");
  // p.availableSchemas.add("PROD");
  // p.availableSchemas.add("DEV1");
  // p.availableSchemas.add("DEV2");
  // p.availableSchemas.add("STAGING");
  // p.availableSchemas.add("QA");
  //
  // p.generator = "MyBatis Spring";
  // p.availableGenerators = new ArrayList<String>();
  // p.availableGenerators.add("MyBatis");
  // p.availableGenerators.add("Spring JDBC");
  // p.availableGenerators.add("MyBatis Spring");
  //
  // return p;
  // }

  @Override
  public String getWindowTitle() {
    return "Connect to the database";
  }

  @Override
  public void addPages() {
    addPage(this.driverPage);
    addPage(this.connectionPage);
    addPage(this.selectionPage);
    addPage(this.confirmationPage);
  }

  @Override
  public boolean isHelpAvailable() {
    return false;
  }

  @Override
  public boolean performFinish() {
    try {
      this.properties.save();
    } catch (CouldNotSavePropertiesException e) {
      MessageDialog.openError(this.shell, "Could not save HotRod Plugin's properties file",
          "Error: " + e.getMessage() + "\n\nCould not save HotRod Plugin's properties file.");
    }
    return true;
  }

  @Override
  public boolean canFinish() {
    // boolean complete =
    // !SUtil.isEmpty(this.driverPage.getFieldDriverClassName().getText()) //
    // && !SUtil.isEmpty(this.connectionPage.getFieldURL().getText()) //
    // ;
    // boolean finish = complete && this.onLastPage;
    // System.out
    // .println("[canFinish()]: complete=" + complete + " onLastPage=" +
    // this.onLastPage + " -> finish=" + finish);
    return this.onLastPage;
  }

  // =======================
  // === Page 1 - Driver ===
  // =======================

  public class DriverPage extends WizardPage {

    private final String[] FILTER_NAMES = { "Java Archive (*.jar)" };
    private final String[] FILTER_EXTS = { "*.jar" };

    private Composite container;
    private org.eclipse.swt.widgets.List fieldClassPathEntries;
    private Text fieldDriverClassName;

    public DriverPage() {
      super("Select the JDBC driver");
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

      this.fieldClassPathEntries = new org.eclipse.swt.widgets.List(this.container,
          SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
      {
        GridData gridData = new GridData(GridData.FILL, GridData.VERTICAL_ALIGN_BEGINNING, true, true);
        gridData.horizontalSpan = 4;
        gridData.verticalSpan = 2;
        gridData.minimumHeight = 172;
        gridData.verticalAlignment = GridData.VERTICAL_ALIGN_BEGINNING;
        this.fieldClassPathEntries.setLayoutData(gridData);
      }
      for (String entryPath : fileProperties.getDriverClassPathEntries()) {
        this.fieldClassPathEntries.add(entryPath);
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
          File baseFolder = mainConfigFace.getProject().getLocation().toFile();
          String basePath = baseFolder.getPath();
          System.out.println("--- basePath=" + basePath);
          fileDialog.setFilterPath(basePath);
          fileDialog.open();
          String path = fileDialog.getFilterPath();
          File folder = new File(path);
          System.out.println("path=" + path);
          for (String fileName : fileDialog.getFileNames()) {
            System.out.println("fileName=" + fileName);
            File f = new File(folder, fileName);
            File relFile = FUtil.getRelativeFile(f, baseFolder);
            if (relFile != null) {
              fieldClassPathEntries.add(relFile.getPath());
            } else {
              fieldClassPathEntries.add(f.getPath());
            }
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
          for (String fileName : fieldClassPathEntries.getSelection()) {
            System.out.println(" * deleting: " + fileName);
            fieldClassPathEntries.remove(fileName);
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
      this.fieldDriverClassName.setText(fileProperties.getDriverClassName());
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

    @Override
    public IWizardPage getNextPage() {

      System.out.println("*** getNextPage()");

      if (driver != null || driverException == null) {
        return super.getNextPage();
      } else {
        this.setErrorMessage(driverException.getMessage());
        return driverPage;
      }

    }

    // Getters

    public org.eclipse.swt.widgets.List getFieldJars() {
      return fieldClassPathEntries;
    }

    public Text getFieldDriverClassName() {
      return fieldDriverClassName;
    }

  }

  // TODO: nothing todo. Just a marker.

  // ===========================
  // === Page 2 - Connection ===
  // ===========================

  public class ConnectionPage extends WizardPage {

    private Composite container;
    private Text fieldURL;
    private Text fieldUsername;
    private Text fieldPassword;

    public ConnectionPage() {
      super("Connection Properties");
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
      this.fieldURL.setText(fileProperties.getUrl());
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
      this.fieldUsername.setText(fileProperties.getUsername());

      // Password

      Label label3 = new Label(this.container, SWT.NONE);
      label3.setText("Password:");

      this.fieldPassword = new Text(this.container, SWT.BORDER | SWT.SINGLE);
      this.fieldPassword.setText(fileProperties.getPassword());

      // Page assembled

      setControl(this.container);

      this.validate();

    }

    @Override
    public IWizardPage getNextPage() {
      if (connection != null || connectionException == null) {
        return super.getNextPage();
      } else {
        this.setErrorMessage(connectionException.getMessage());
        return connectionPage;
      }
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

  // ==========================
  // === Page 3 - Selection ===
  // ==========================

  // TODO: nothing todo. Just a marker.

  public class SelectionPage extends WizardPage {

    private Composite container;
    private Label labelCatalog;
    private Combo fieldCatalog;
    private Label labelSchema;
    private Combo fieldSchema;
    private Combo fieldGenerator;

    public SelectionPage() {
      super("Select the database schema");
      System.out.println("[[ SelectionPage ]]");
      setTitle("Select the database schema");
      setDescription("Pick a database schema and a generator");
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

      this.labelCatalog = new Label(this.container, SWT.NONE);
      this.labelCatalog.setText("Catalog:");
      this.fieldCatalog = new Combo(this.container, SWT.NULL);
      this.fieldCatalog.setText(fileProperties.getCatalog());

      // Schema

      this.labelSchema = new Label(this.container, SWT.NONE);
      this.labelSchema.setText("Schema:");
      this.fieldSchema = new Combo(this.container, SWT.NULL);
      this.fieldSchema.setText(fileProperties.getSchema());

      // Generator

      Label label3 = new Label(this.container, SWT.NONE);
      label3.setText("Generator:");
      this.fieldGenerator = new Combo(this.container, SWT.NULL);
      this.fieldGenerator.setItems(AVAILABLE_GENERATORS);
      this.fieldGenerator.setText(fileProperties.getGenerator());

      // Page assembled

      setControl(this.container);

      this.validate();
    }

    public void updateAvailableCatalogsAndSchemas() {
      if (availableCatalogs != null) {
        this.labelCatalog.setVisible(true);
        this.fieldCatalog.setItems(availableCatalogs.toArray(new String[0]));
        this.fieldCatalog.setVisible(true);
      } else {
        this.labelCatalog.setVisible(false);
        this.fieldCatalog.setItems(new String[0]);
        this.fieldCatalog.setVisible(false);
      }
      if (availableSchemas != null) {
        this.labelSchema.setVisible(true);
        this.fieldSchema.setItems(availableSchemas.toArray(new String[0]));
        this.fieldSchema.setVisible(true);
      } else {
        this.labelSchema.setVisible(false);
        this.fieldSchema.setItems(new String[0]);
        this.fieldSchema.setVisible(false);
      }
    }

    private void validate() {
      // if (!SUtil.isEmpty(this.fieldURL.getText())) {
      setPageComplete(true);
      // } else {
      // setPageComplete(false);
      // }
    }

    @Override
    public IWizardPage getNextPage() {
      if (selectionMessage == null) {
        return super.getNextPage();
      } else {
        this.setErrorMessage(selectionMessage);
        return selectionPage;
      }
    }

    public Combo getFieldCatalog() {
      return fieldCatalog;
    }

    public Combo getFieldSchema() {
      return fieldSchema;
    }

    public Combo getFieldGenerator() {
      return fieldGenerator;
    }

  }

  // =============================
  // === Page 4 - Confirmation ===
  // =============================

  // TODO: nothing todo. Just a marker.

  public class ConfirmationPage extends WizardPage {

    private Composite container;

    public ConfirmationPage() {
      super("Save the database connection");
      System.out.println("[[ ConfirmationPage ]]");
      setTitle("Save the database connection");
      setDescription("Save all changes.");
    }

    @Override
    public void createControl(final Composite parent) {
      System.out.println("[[ ConfirmationPage ]] - createControls()");

      this.container = new Composite(parent, SWT.NONE);
      {
        GridLayout layout = new GridLayout();
        // layout.numColumns = 2;
        this.container.setLayout(layout);
      }

      Label text = new Label(this.container, SWT.NONE);
      text.setText("The database connection properties have been verified and are ready to be saved.");

      // Page assembled

      setControl(this.container);

      setPageComplete(true);
    }

    @Override
    public IWizardPage getPreviousPage() {
      System.out.println("[Last] getPreviousPage()");
      return super.getPreviousPage();
    }

  }

}