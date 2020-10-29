package jpa.spring.poc.model;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

// https://www.baeldung.com/spring-data-jpa-query
public interface RequestRepository extends CrudRepository<Request, Long> {

	@Query("SELECT i FROM RequestItem i WHERE i.key.idRequest = ?1")
	List<RequestItem> findAllItems(long requestId);

}