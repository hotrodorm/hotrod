package xsdtests.case06;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Employee {

  private FirstName firstName = null;
  private List<Alias> aliases = new ArrayList<Alias>();

  // Getters & Setters

  public FirstName getFirstName() {
    return firstName;
  }

  @XmlElement(name = "firstname")
  public void setFirstName(FirstName firstName) {
    System.out.println("***   " + Employee.class.getSimpleName() + ".setFirstName(" + firstName + ")");
    this.firstName = firstName;
  }

  public List<Alias> getAliases() {
    return this.aliases;
  }

  @XmlElement
  public void setAlias(Alias alias) {
    System.out.println("***   " + Employee.class.getSimpleName() + ".setAlias(" + alias + ")");
    this.aliases.add(alias);
  }

  // toString

}
