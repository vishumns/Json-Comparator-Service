package com.json.comparator.service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.wnameless.json.flattener.JsonFlattener;
import com.github.wnameless.json.unflattener.JsonUnflattener;
import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.json.comparator.model.JsonBase;
import com.json.comparator.model.JsonCompareException;
import com.json.comparator.repository.JsonRepository;

/**
 * The service class to handle the controller requests.
 */
@Service
public class JsonCompService {

	@Autowired
	private JsonRepository jsonRepository;
	
	/*
	 * Save/update the given base json in database with given id.
	 */
	public void save(JsonBase jsonObj) {
		jsonRepository.save(jsonObj);
	}
	
	/*
	 * Returns the base json from database by given id.
	 */
	public JsonBase findById(int id) {
		Optional<JsonBase> base=  jsonRepository.findById(id);
		return base.orElse(null);
	}
	
	/*
	 * Returns the base json from database by given id.
	 */
	public JSONObject [] findAll () {
		List<JsonBase>   base=  (List<JsonBase>) jsonRepository.findAll();
		JSONObject [] jsonObjects = new JSONObject[base.size()];
		JSONParser parser = new JSONParser();
		int i=0;
		for(JsonBase b: base) {
			try {
			JSONObject jsonObject = (JSONObject) parser.parse(b.getJsonData());
			jsonObject.put("id", b.getId());
			jsonObjects[i++] = jsonObject;
			} catch (Exception ex) {
				throw new JsonCompareException(ex.getMessage());
			}
		}
		return jsonObjects;
	}
	
	/*
	 * Delete the base json from database by given id.
	 */
	public void deleteById(int id) {
		jsonRepository.deleteById(id);
	}
	
	/*
	 * Delete all the base json from database.
	 */
	public void deleteAll() {
		jsonRepository.deleteAll();
	}
	
	/*
	 * It checks the base json availability for given id.
	 */
	public boolean isAvailable(int id) {
		return jsonRepository.existsById(id);
	}

	/*
	 * This method compares two json and returns the difference available in base json.
	 * It finds base json by request id and then it compares the base json with input json
	 * and return the difference available in base json.
	 */
	public String compareJson(String base, String input) {
		JSONParser parser = new JSONParser();
		String nestedJson ="";
		try {
			JSONObject jsonObject = (JSONObject) parser.parse(base);
			JSONObject jsonObject2 = (JSONObject) parser.parse(input);

			Map<String, Object> flattenedJsonMap 
						= JsonFlattener.flattenAsMap(jsonObject.toString());
			Map<String, Object> flattenedJsonMap2 
						= JsonFlattener.flattenAsMap(jsonObject2.toString());
			
			MapDifference<String, Object> difference 
						= Maps.difference(flattenedJsonMap, flattenedJsonMap2);
			Map<String,Object> mapp =  difference.entriesOnlyOnLeft();
			if(mapp.isEmpty())
				return "There is no difference available in both json";
			nestedJson = unflattenMap(mapp);
		} catch (Exception e) {
			throw new JsonCompareException(e.getMessage());
		}
		return nestedJson;
	}
	
	/*
	 * This method unflattern the map and returns as a string.
	 */
	private String unflattenMap(Map<String,Object> mapp) {
		StringBuilder sb = new StringBuilder("{");
		String nestedJson = "";
		mapp.entrySet().parallelStream().forEach(entry -> {
		String value = Optional.ofNullable(entry.getValue()).map(Objects::toString).orElse("null");
		if(!( isNumeric(value) || isBoolean(value)
				|| isEmptySubJsonElement(value)))
			value = "\""+value+"\"";
		sb.append("\"" + entry.getKey() + "\""+":" + value+",");
		});
		sb.deleteCharAt(sb.length()-1);
		sb.append("}");
		nestedJson = JsonUnflattener.unflatten(sb.toString());
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		JsonParser jp = new JsonParser();
		JsonElement je = jp.parse(nestedJson);
		nestedJson = gson.toJson(je);
		nestedJson = nestedJson.replace("null,", "");
		return nestedJson;
	}
	
	/*
	 * 
	 */
	private boolean isNumeric(String str) {
		if(str.matches("^[a-zA-Z]*$"))
			return false;
		try {
			Float.valueOf(str);
			return true;
		} catch (NumberFormatException ex) {
			try {
				Double.valueOf(str);
				return true;
			} catch (NumberFormatException e) {
				return false;
			}
		}
	}
	
	/*
	 * 
	 */
	private boolean isBoolean(String str) {
		return (str.contains("true") || str.contains("false"));
	}
	
	/*
	 * 
	 */
	private boolean isEmptySubJsonElement(String str) {
		return (str.equals("{}") || str.equals("[]"));
	}

	
}
