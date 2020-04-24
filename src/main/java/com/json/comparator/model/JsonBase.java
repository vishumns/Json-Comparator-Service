package com.json.comparator.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

/**
 * This is the model class 
 */
@Entity
public class JsonBase {
    
	// This is the id for given base json
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	int id;
	
	// Base json is converted to string and then saved to database
	@Lob
	String jsonData;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getJsonData() {
		return jsonData;
	}
	public void setJsonData(String jsonData) {
		this.jsonData = jsonData;
	}
	
}
