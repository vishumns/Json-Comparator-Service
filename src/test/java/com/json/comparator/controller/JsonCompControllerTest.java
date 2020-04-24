package com.json.comparator.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.json.comparator.model.JsonBase;
import com.json.comparator.model.JsonCompareException;
import com.json.comparator.service.JsonCompService;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class JsonCompControllerTest {

	@InjectMocks
	JsonCompController jsonCompController;
	
	@Mock
	JsonCompService jsonCompService;
	
	@BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }
	
	@Test
    public void findById()
    {
		String json = "{\"name\":\"sonoo\",\"salary\":600000.0,\"age\":27}";
        JsonBase base= new JsonBase();
        base.setJsonData(json);
        when(jsonCompService.findById(1)).thenReturn(base);
        when(jsonCompService.findById(2)).thenReturn(null);
         
        ResponseEntity<String> entity = jsonCompController.findById(1);
        ResponseEntity<String> entity2 = jsonCompController.findById(2);
        
        assertEquals(HttpStatus.NOT_FOUND,entity2.getStatusCode());
        assertEquals(HttpStatus.OK,entity.getStatusCode());
    }
	
	@Test
	public void save() {
		String json = "{\"name\":\"sonoo\",\"salary\":600000.0,\"age\":27}";
        JsonBase base= new JsonBase();
        base.setJsonData(json);
        verify(jsonCompService,times(0)).save(base);
        ResponseEntity<?> entity = jsonCompController.save(json);
        assertEquals(HttpStatus.CREATED,entity.getStatusCode());
	}
	
	@Test
	public void findAll() throws ParseException {
		JSONParser parser = new JSONParser();
		String json = "{\"name\":\"sonoo\",\"salary\":600000.0,\"age\":27}";
		JsonBase base= new JsonBase();
		base.setJsonData(json);
		JSONObject jsonObject = (JSONObject) parser.parse(base.getJsonData());
        JSONObject[] jsonBase = {jsonObject,jsonObject};
        
        when(jsonCompService.findAll()).thenReturn(jsonBase);
        ResponseEntity<?> entity = jsonCompController.findAll();
        assertEquals(HttpStatus.OK,entity.getStatusCode());
        
        jsonBase = new JSONObject[0];
        when(jsonCompService.findAll()).thenReturn(jsonBase);
        ResponseEntity<?> entity2 = jsonCompController.findAll();
        assertEquals(HttpStatus.NO_CONTENT,entity2.getStatusCode());
	}
	
	@Test
	public void delete() {
        verify(jsonCompService,times(0)).deleteById(1);
        ResponseEntity<?> entity = jsonCompController.deleteById(1);
        assertEquals(HttpStatus.NOT_FOUND,entity.getStatusCode());
        
        when(jsonCompService.isAvailable(1)).thenReturn(true);
        ResponseEntity<?> entity2 = jsonCompController.deleteById(1);
        assertEquals(HttpStatus.OK,entity2.getStatusCode());
        
        verify(jsonCompService,times(0)).deleteAll();
        ResponseEntity<?> entity3 = jsonCompController.deleteAll();
        assertEquals(HttpStatus.OK,entity3.getStatusCode());
	}
	
	@Test
	public void comparejson() {
		String json = null;
		ResponseEntity<?> entity = jsonCompController.compareJson(json,1);
		assertEquals(HttpStatus.NO_CONTENT,entity.getStatusCode());
		
		JsonBase base= null;
		json = "{\"name\":\"vishal\",\"salary\":100.00\"age\":29,\"year\":1991}";
		when(jsonCompService.findById(1)).thenReturn(null);
		ResponseEntity<?> entity2 = jsonCompController.compareJson(json,1);
		assertEquals(HttpStatus.NOT_FOUND,entity2.getStatusCode());
		
		
		base= new JsonBase();
		base.setJsonData(json);
		json = "{\"name\":\"vishal\",\"salary\":100.00\"age\":29}";
		String baseData = base.getJsonData();
		String input = json;
		String difference = new String("{\"year\":1991}");
		when(jsonCompService.findById(1)).thenReturn(base);
		when(jsonCompService.compareJson(input, baseData)).thenReturn(difference);
		ResponseEntity<?> entity4 = jsonCompController.compareJson(json,1);
		assertEquals(HttpStatus.OK, entity4.getStatusCode());
		
		when(jsonCompService.findById(1)).thenReturn(base);
		doThrow(new JsonCompareException("Exception")).when(jsonCompService).compareJson(input, baseData);
		ResponseEntity<?> entity3 = jsonCompController.compareJson(json,1);
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, entity3.getStatusCode());
		
		
	}
}