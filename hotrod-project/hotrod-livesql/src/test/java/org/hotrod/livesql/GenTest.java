package org.hotrod.livesql;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;

class GenTest {

//  @Test
  void generatesSourceFromCommandLine() throws Exception {
//    run("env", 10);
    run(new String[] { "echo", "'Starting...'" }, 10);
    run(new String[] { "rm", "-r", "src/gen/java/app/persistence" }, 10);
    run(new String[] { "echo", "'Old code deleted'" }, 10);
    run(new String[] { "mvn", "-P", "h2unit", "hotrod:gen" }, 10);
  }

  private void run(String[] command, long timeout) throws IOException, InterruptedException {
    Process process = new ProcessBuilder(command).redirectErrorStream(true).start();
    boolean finished = process.waitFor(timeout, TimeUnit.SECONDS);
    if (!finished) {
      process.destroyForcibly();
      fail("Timed out");
    }
    String output = this.readAllBytes(process.getInputStream());
    output = output == null ? "" : output.trim();
    System.out.println(output);
    assertEquals(0, process.exitValue(), output);
  }

  private String readAllBytes(InputStream inputStream) throws IOException {
    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    byte[] data = new byte[1024];
    int nRead;
    while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
      buffer.write(data, 0, nRead);
    }
    return buffer.toString("UTF-8");
  }

}