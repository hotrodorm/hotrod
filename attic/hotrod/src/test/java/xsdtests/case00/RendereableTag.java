package xsdtests.case00;

import org.hotrod.exceptions.InvalidConfigurationFileException;

public interface RendereableTag {

  public void render(StringBuilder sb) throws InvalidConfigurationFileException;

}
