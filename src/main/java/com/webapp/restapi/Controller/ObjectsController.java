package com.webapp.restapi.Controller;

import com.mongodb.BasicDBObject;
import com.webapp.restapi.Models.Objects;
import com.webapp.restapi.Repositories.ObjectsRepository;

import org.bson.Document;
import org.json.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static com.mongodb.client.model.Projections.excludeId;
import static java.util.Arrays.asList;

@RestController
@RequestMapping("/api")

public class ObjectsController {
    private final ObjectsRepository objectsRepository;
    
    public ObjectsController(ObjectsRepository objectsRepository) {
        this.objectsRepository = objectsRepository;
    }
    
    @PostMapping("objects")
    @ResponseStatus(HttpStatus.CREATED)
    public Document postObject(@RequestBody Document object) throws Exception {
    	String jsonString = String.valueOf(object); 
//    	if (!this.isJSONValid(jsonString)) {
//    		System.out.println("bdman");
//    		throw new Exception("Invalid JSON");
//    	}
    	return objectsRepository.save(object);
    }
    
    @GetMapping("objects")
    public List<BasicDBObject> getUsers() {
        return objectsRepository.findAll();
    }
    
    @PutMapping("objects/{uId}")
    public Document putObject(@PathVariable String uId, @RequestBody Document object) {
    	System.out.println(uId);
        return objectsRepository.update(object, uId);
    }

    @GetMapping("objects/{uId}")
    public ResponseEntity<Document> getPerson(@PathVariable String uId) {
        Document object = objectsRepository.findOne(uId);
        if (object == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        object.remove("_id");
        return ResponseEntity.ok(object);
    }
    
//    public boolean isJSONValid(String test) {
//        try {
//        	System.out.println("1");
//            new JSONObject(test);
//        } catch (JSONException ex) {
//            try {
//            	System.out.println("2");
//                new JSONArray(test);
//            } catch (JSONException ex1) {
//            	System.out.println("3");
////            	throw new Exception("Invalid JSON");
//                return false;
//            }
//        }
//        return true;
//    }
}
