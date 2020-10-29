package jpa.spring.poc.model;

import org.springframework.data.repository.CrudRepository;

// https://www.baeldung.com/spring-data-jpa-query
public interface RequestItemRepository extends CrudRepository<RequestItem, RequestItemKey> {

}