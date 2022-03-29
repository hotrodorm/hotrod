package ${package};

import java.util.List;
import java.util.Map;

import org.hotrod.runtime.livesql.LiveSQL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.myapp.persistence.primitives.EmployeeDAO;
import com.myapp.persistence.primitives.EmployeeDAO.EmployeeTable;
import com.myapp.persistence.EmployeeVO;

@RequestMapping("/employee")
@RestController
public class EmployeeRestController {

  @Autowired
  private EmployeeDAO employeeDAO;

  @Autowired
  private LiveSQL sql;

  @GetMapping("{sid}")
  public EmployeeVO getEmployee(@PathVariable String sid) {
    System.out.println("Retrieving employee " + sid + "...");
    try {
      Integer id = Integer.parseInt(sid);
      return this.employeeDAO.selectByPK(id);
    } catch (NumberFormatException e) {
      return null;
    }
  }

  @GetMapping("/search/{name}")
  public List<Map<String, Object>> searchEmployee(@PathVariable String name) {
    EmployeeTable e = EmployeeDAO.newTable();
    List<Map<String, Object>> r = sql.select().from(e).where(e.name.eq(name)).orderBy(e.id.asc()).execute();
    return r;
  }

}
