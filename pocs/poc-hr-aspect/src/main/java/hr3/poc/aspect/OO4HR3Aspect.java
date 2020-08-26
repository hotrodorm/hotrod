package hr3.poc.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Aspect
@Configuration
@Order(150)
public class OO4HR3Aspect {

	@Before("execution(* hr3.poc.dao.action.NewsImpl.get*(..))")
	public void superclassLoader(JoinPoint joinPoint) {
		System.out.println("> SuperclassLoader Aspect triggered. joinpoint: " + joinPoint);
		try {
			LazySuperclassLoading target = (LazySuperclassLoading) joinPoint.getTarget();
			target.loadSuperclass();
		} catch (Exception e) {
			// do nothing
		}
	}

}
