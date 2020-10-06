package org.hotrod.generator.mybatisspring;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.hotrod.config.HotRodConfigTag;
import org.hotrod.config.MyBatisSpringTag;
import org.hotrod.exceptions.UncontrolledException;
import org.hotrod.generator.FileGenerator;
import org.hotrod.generator.FileGenerator.TextWriter;
import org.hotrod.metadata.ForeignKeyMetadata;

public class AvailableFKs {

  private final static String FILE_NAME = "available-fks.xml";

  private HotRodConfigTag config;
  private MyBatisSpringTag mybatisTag;
  private List<ForeignKeyMetadata> fks;

  public AvailableFKs(final HotRodConfigTag config, final List<ForeignKeyMetadata> fks) {
    this.config = config;
    this.mybatisTag = (MyBatisSpringTag) this.config.getGenerators().getSelectedGeneratorTag();
    this.fks = fks;
  }

  public void generate(final FileGenerator fileGenerator) throws UncontrolledException {

    File afks = new File(this.mybatisTag.getMappers().getPrimitivesDir(), FILE_NAME);

    try {
      try (TextWriter w = fileGenerator.createWriter(afks)) {
        w.write("<foreign-keys>\n");
        String q = this.fks.stream().map(a -> new ComparableFKM(a)).sorted().map(c -> c.getFK())
            .map(fk -> String.format(
                "  <foreign-key parent=\"%s (%s)\" children=\"%s (%s)\" get-parent-method=\"\" get-children-method=\"\" />\n", //
                esc(fk.getRemote().getTableMetadata().getId().toString()), //
                esc(fk.getRemote().getColumns().stream().map(c -> c.getColumnName()).collect(Collectors.joining(", "))), //
                esc(fk.getLocal().getTableMetadata().getId().toString()), //
                esc(fk.getLocal().getColumns().stream().map(c -> c.getColumnName()).collect(Collectors.joining(", "))) //
            )).collect(Collectors.joining());
        w.write(q);
        w.write("</foreign-keys>\n");
      }
    } catch (IOException e) {
      throw new UncontrolledException("Could not generate list of available FKs file at" + afks, e);
    }
  }

  private class ComparableFKM implements Comparable<ComparableFKM> {

    private ForeignKeyMetadata fkm;
    private String key;

    public ComparableFKM(final ForeignKeyMetadata fkm) {
      this.fkm = fkm;
      this.key = fkm.getRemote().getTableMetadata().getId().toString() //
          + ":" //
          + fkm.getRemote().getColumns().stream().map(c -> c.getColumnName()).collect(Collectors.joining(".")) //
          + "-" //
          + fkm.getLocal().getTableMetadata().getId().toString() //
          + ":" //
          + fkm.getLocal().getColumns().stream().map(c -> c.getColumnName()).collect(Collectors.joining(".")) //
      ;
    }

    @Override
    public int compareTo(final ComparableFKM o) {
      return this.key.compareTo(o.key);
    }

    public ForeignKeyMetadata getFK() {
      return fkm;
    }

  }

  private String esc(final String s) {
    return s.replaceAll("&", "&amp;") //
        .replaceAll("<", "&lt;") //
        .replaceAll("\"", "&quot;") //
    ;
  }

}
