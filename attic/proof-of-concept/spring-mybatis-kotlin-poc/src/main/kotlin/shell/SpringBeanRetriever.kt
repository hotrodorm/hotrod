package shell

import org.springframework.context.ApplicationContext
import org.springframework.context.support.ClassPathXmlApplicationContext

class SpringBeanRetriever {

  private var context: ApplicationContext

  private constructor() {
    context = ClassPathXmlApplicationContext("spring-configuration.xml")
  }

  companion object { // static members here

    var instance3: SpringBeanRetriever? = null

    fun getInstance(): SpringBeanRetriever {
      if (instance3 == null) {
        instance3 = SpringBeanRetriever()
        return instance3!!
      } else {
        return instance3!!
      }
    }

    fun getBean(name: String): Any? {
      return SpringBeanRetriever.getInstance().context.getBean(name)
    }

  }

}
