package com.webapp.restapi.Controller;

import com.webapp.restapi.Repositories.ObjectsRepository;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api")

public class ObjectsController {
    private final ObjectsRepository objectsRepository;
    
	@Value("${spring.data.url}")
    private String apiUrl;
    
    public ObjectsController(ObjectsRepository objectsRepository) {
        this.objectsRepository = objectsRepository;
    }
    
    @PostMapping("objects")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Document> postObject(@RequestBody String object) {
    	Document document;
    	try {
    		document = Document.parse(object);
    	} catch (Exception ex) {
    		Map<String, String> notFound = this.getErrorMessage("POST",  "Not a JSON object");
    		return new ResponseEntity(notFound, HttpStatus.BAD_REQUEST);
    	}
	    Document savedObj = objectsRepository.save(document);
	    return new ResponseEntity(savedObj, HttpStatus.CREATED);
    }

    @GetMapping("objects/")
    public List<Document> getUsers() {
    	List<Document> objects = objectsRepository.findAll();
        return objects;
    }

    @PutMapping("objects/{uId}")
    public ResponseEntity<Document> putObject(@PathVariable String uId, @RequestBody String object) {
    	Document obj = objectsRepository.findOne(uId);
        if (obj == null) {
        	Map<String, String> notFound = this.getErrorMessage("PUT",  "JSON Object not found");
            return new ResponseEntity(notFound, HttpStatus.NOT_FOUND);
        }
        Document document;
    	try {
    		document = Document.parse(object);
    	} catch (Exception ex) {
    		Map<String, String> notFound = this.getErrorMessage("PUT",  "Not a JSON object");
    		return new ResponseEntity(notFound, HttpStatus.BAD_REQUEST);
    	}
	    Document savedObj = objectsRepository.update(document, uId);
	    return new ResponseEntity(savedObj, HttpStatus.OK);
    }

    @GetMapping("objects/{uId}")
    public ResponseEntity<Document> getObject(@PathVariable String uId) {
        Document object = objectsRepository.findOne(uId);
        if (object == null) {
        	Map<String, String> notFound = this.getErrorMessage("GET",  "JSON Object not found");
            return new ResponseEntity(notFound, HttpStatus.NOT_FOUND);
        }
        object.remove("_id");
        return ResponseEntity.ok(object);
    }
    
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public final ResponseEntity<Document> handleAllExceptions(RuntimeException e) {
    	Map<String, String> notFound = this.getErrorMessage(e.getMessage(), "");
        return new ResponseEntity(notFound, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    public Map<String,String> getErrorMessage(String method, String message) {
    	Map<String, String> customMessage = new HashMap<String, String>();
    	customMessage.put("verb", method);
    	customMessage.put("url", apiUrl);
    	customMessage.put("message", message);
    	return customMessage;
    }
    
    @DeleteMapping("objects/{id}")
    public ResponseEntity deleteObject(@PathVariable String id) {
    	Document object = objectsRepository.findOne(id);
        if (object == null) {
        	Map<String, String> notFound = this.getErrorMessage("DELETE",  "JSON Object not found");
            return new ResponseEntity(notFound, HttpStatus.NOT_FOUND);
        }
        objectsRepository.delete(id);
        return ResponseEntity.ok().build();
    }
}
