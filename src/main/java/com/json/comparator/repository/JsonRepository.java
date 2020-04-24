package com.json.comparator.repository;

import org.springframework.data.repository.CrudRepository;

import com.json.comparator.model.JsonBase;

/*
 * Repository interface for database related operations.
 */
public interface JsonRepository extends CrudRepository<JsonBase,Integer> {
	
	
}
