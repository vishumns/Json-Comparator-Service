package com.json.comparator.controller;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
 import org.springframework.web.bind.annotation.RestController;

import com.json.comparator.model.JsonBase;
import com.json.comparator.model.JsonCompareException;
import com.json.comparator.service.JsonCompService;


/**
 * Controller class to handle the input request
 */
@RestController
public class JsonCompController {

	@Autowired
	JsonCompService jsonCompService;
	
	/*
	 * This method takes base json and save it into database with request id.
	 */
	@PostMapping("/base-jsons")
	public ResponseEntity<String> save(@RequestBody String input) {
		JsonBase jsonInput = new JsonBase();
		jsonInput.setJsonData(input);
		jsonCompService.save(jsonInput);
		int baseJsonsId = jsonInput.getId();
		return new ResponseEntity<>("Saved successfully for Id "+baseJsonsId,
				HttpStatus.CREATED);
	}
	
	/*
	 * This method compares two json and returns the difference available in base json.
	 * It finds base json by request id and then it compares the base json with input json
	 * and return the difference available in base json.
	 */
	@GetMapping("/base-jsons/compare/{id}")
	public ResponseEntity<String> compareJson(@RequestBody String input, @PathVariable int id) {
		try {
			if(input == null)
				return new ResponseEntity<>("input json can not be null", 
						HttpStatus.NO_CONTENT);
			JsonBase base = jsonCompService.findById(id);
			if(base == null)
				return new ResponseEntity<>("base json not found with id "+id, 
						HttpStatus.NOT_FOUND);
			String baseData = base.getJsonData();
			String difference = jsonCompService.compareJson(input,baseData);
			return new ResponseEntity<>(difference, 
					HttpStatus.OK);
		} catch (JsonCompareException e) {
			return new ResponseEntity<>("Exception occeured while comparing", 
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/*
	 * This method takes base json and update it into database with request id.
	 */
	@PutMapping("/base-jsons/{id}")
	public ResponseEntity<String> save(@RequestBody String input, @PathVariable int id) {
		JsonBase jsonInput = new JsonBase();
		jsonInput.setId(id);
		jsonInput.setJsonData(input);
		jsonCompService.save(jsonInput);
		return new ResponseEntity<>("Updated successfully for Id "+id, 
				HttpStatus.CREATED);
	}
	
	/*
	 * This method returns all base jsons from database.
	 */
	@GetMapping("/base-jsons")
	public ResponseEntity<?> findAll() {
		JSONObject [] jsonObjects = jsonCompService.findAll();
		if(jsonObjects.length==0)
			return new ResponseEntity<>("No base json available", HttpStatus.NO_CONTENT);
		
		return new ResponseEntity<>(jsonObjects, HttpStatus.OK);
	}
	
	/*	
	 * This method returns base json from database by given request id.
	 */
	@GetMapping("/base-jsons/{id}")
	public ResponseEntity<String> findById(@PathVariable int id) {
		JsonBase base = jsonCompService.findById(id);
		if(base != null && base.getJsonData().length()>=1) {
		return new ResponseEntity<>(base.getJsonData(), HttpStatus.OK);
		}
		return new ResponseEntity<>("base json not found with id "+id,
				HttpStatus.NOT_FOUND);
	}
	
	/*
	 * This method delete the base json from database of given request id.
	 */
	@DeleteMapping("/base-jsons/{id}")
	public ResponseEntity<String> deleteById(@PathVariable int id) {
		if(jsonCompService.isAvailable(id)) {
			jsonCompService.deleteById(id);
			return new ResponseEntity<>("Deleted base json for Id "+id, 
					HttpStatus.OK);
		} 
		return new ResponseEntity<>("Base json not available for Id "+id, 
				HttpStatus.NOT_FOUND);
	}
	
	/*
	 * This method delete all the base json from database.
	 */
	@DeleteMapping("/base-jsons")
	public ResponseEntity<String> deleteAll() {
			jsonCompService.deleteAll();
			return new ResponseEntity<>("Deleted all base jsons", 
					HttpStatus.OK);
		} 
	
}
