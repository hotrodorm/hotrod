package app;

import java.io.File;
import java.io.IOException;

import app.model.Model;
import app.model.R;

public class SchemaGen {

  private static final long RANDOM_SEED = 1234;

  private static final int TABLES = 1200;
  private static final int VIEWS = TABLES / 3;
  private static final double FK_PER_TABLE = 2.0;
  private static final int COLUMNS_PER_TABLE = 20;

  public static void main(String[] args) throws IOException {
    R.seed(RANDOM_SEED);
    Model m = Model.of(TABLES, VIEWS, FK_PER_TABLE, COLUMNS_PER_TABLE);
    m.create();
    
    m.build(new File("./src/main/database/1.0.0/build.sql"));
    m.clean(new File("./src/main/database/1.0.0/clean.sql"));
    
    System.out.println("Build complete.");
  }

}
