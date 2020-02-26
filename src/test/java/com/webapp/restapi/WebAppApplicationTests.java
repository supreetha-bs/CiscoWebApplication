package com.webapp.restapi;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.webapp.restapi.Repositories.ObjectsRepository;

import org.bson.Document;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.annotation.PostConstruct;

import java.util.*;
import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WebAppApplicationTests {
	
	@LocalServerPort
	private int port;
	@Autowired
	private TestRestTemplate rest;
	@Autowired
	private ObjectsRepository objectRepository;
	private String URL;
	@Value("${spring.data.url}")
    private String apiUrl;
	
	@Autowired
	WebAppApplicationTests(MongoClient mongoClient) {
        createObjectCollectionIfNotPresent(mongoClient);
    }
	
	private void createObjectCollectionIfNotPresent(MongoClient mongoClient) {
        MongoDatabase db = mongoClient.getDatabase("rest-apis");
        if (!db.listCollectionNames().into(new ArrayList<>()).contains("objects"))
            db.createCollection("objects");
    }

	 @PostConstruct
	 void setUp() {
		 URL = "http://localhost:" + port + "/api";
	 }

	 @AfterEach
	 void tearDown() {
		 objectRepository.deleteAll();
	 }
	 
	 @DisplayName("GET /objects with no objects")
	 @Test
	 void getObjects() {
		 ResponseEntity<String> result = rest.exchange(URL + "/objects/", HttpMethod.GET, null,
                 													String.class);
	 	 assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
	     assertThat(result.getBody()).isEqualTo("[]");
	 }

	 @DisplayName("POST /object with 1 object")
	 @Test
	 void postObject() {
		 String obj = "{\n" + 
		 		"    \"firstName\": \"Andrey\",\n" + 
		 		"    \"lastName\": \"Kolmogorov\",\n" + 
		 		"    \"dob\": \"25 April 1903\"\n" + 
		 		"}";
	     ResponseEntity<Document> result = rest.postForEntity(URL + "/objects", obj, Document.class);

	     Document document = Document.parse(obj);
	     assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
	     Document objectResult = result.getBody();
	     assertThat(objectResult.get("uid")).isNotNull();
	     objectResult.remove("uid");
	     assertThat(document).isEqualTo(objectResult);
	     assertThat(objectRepository.count()).isEqualTo(1L);
	 }
	 
	 @DisplayName("POST /object with an arbitrary object")
	 @Test
	 void postArbitraryObject() {
		 String obj = "{\n" + 
		 		"		\"index\": 0,\n" + 
		 		"		\"guid\": \"cc72768c-237b-4e03-8382-b2319a661965\",\n" + 
		 		"		\"isActive\": true,\n" + 
		 		"		\"balance\": \"$3,144.93\",\n" + 
		 		"		\"picture\": \"http://placehold.it/32x32\",\n" + 
		 		"		\"age\": 25,\n" + 
		 		"		\"eyeColor\": \"blue\",\n" + 
		 		"		\"name\": \"Michael Bray\",\n" + 
		 		"		\"gender\": \"male\",\n" + 
		 		"		\"company\": \"ANDRYX\",\n" + 
		 		"		\"email\": \"michaelbray@andryx.com\",\n" + 
		 		"		\"phone\": \"+1 (855) 408-3531\",\n" + 
		 		"		\"address\": \"116 Portland Avenue, Volta, Oregon, 443\",\n" + 
		 		"		\"about\": \"Ex ipsum ipsum duis ex ipsum sit. Mollit officia non veniam dolore id anim Lorem. Ut Lorem esse sint aliqua enim aliquip veniam ex fugiat veniam irure. Consectetur id reprehenderit consectetur velit sunt elit in ullamco enim cupidatat ex. Aliqua tempor incididunt ipsum consectetur officia ipsum dolor nostrud velit id deserunt nostrud aliquip ipsum. Nisi aute amet non velit quis pariatur in minim. Culpa aliquip dolor fugiat do eiusmod quis laboris nisi incididunt sit ea ea nisi enim.\\r\\n\",\n" + 
		 		"		\"registered\": \"2015-04-23T12:57:10 +04:00\",\n" + 
		 		"		\"latitude\": -84.043965,\n" + 
		 		"		\"longitude\": 34.889473,\n" + 
		 		"		\"tags\": [\n" + 
		 		"			\"aliquip\",\n" + 
		 		"			\"in\",\n" + 
		 		"			\"esse\",\n" + 
		 		"			\"qui\",\n" + 
		 		"			\"esse\",\n" + 
		 		"			\"culpa\",\n" + 
		 		"			\"deserunt\"\n" + 
		 		"		],\n" + 
		 		"		\"friends\": [{\n" + 
		 		"				\"id\": 0,\n" + 
		 		"				\"name\": \"Rodriquez Mccoy\"\n" + 
		 		"			},\n" + 
		 		"			{\n" + 
		 		"				\"id\": 1,\n" + 
		 		"				\"name\": \"Vicky Lester\"\n" + 
		 		"			},\n" + 
		 		"			{\n" + 
		 		"				\"id\": 2,\n" + 
		 		"				\"name\": \"Aimee Watts\"\n" + 
		 		"			}\n" + 
		 		"		],\n" + 
		 		"		\"greeting\": \"Hello, Michael Bray! You have 1 unread messages.\",\n" + 
		 		"		\"favoriteFruit\": \"strawberry\"\n" + 
		 		"	}";
	     ResponseEntity<Document> result = rest.postForEntity(URL + "/objects", obj, Document.class);
	     assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
	     Document objectResult = result.getBody();
	     assertThat(objectResult.get("uid")).isNotNull();
	     assertThat(objectRepository.count()).isEqualTo(1L);
	 }
	 
	 @DisplayName("POST /object with invalid JSON")
	 @Test
	 void postInvalidObject() {
		 String obj ="{\n" + 
			 		"		\"index\": 0,\n" + 
			 		"		\"guid\": \"cc72768c-237b-4e03-8382-b2319a661965\",\n" + 
			 		"		\"isActive\": true,\n" + 
			 		"		\"balance\": \"$3,144.93\",\n" + 
			 		"		\"picture\": \"http://placehold.it/32x32\",\n" + 
			 		"		\"age\": 25,\n" + 
			 		"		\"eyeColor\": \"blue\",\n" + 
			 		"		\"name\": \"Michael Bray\",\n" + 
			 		"		\"gender\": \"male\",\n" + 
			 		"		\"company\": \"ANDRYX\",\n" + 
			 		"		\"email\": \"michaelbray@andryx.com\",\n" + 
			 		"		\"phone\": \"+1 (855) 408-3531\",\n" + 
			 		"		\"address\": \"116 Portland Avenue, Volta, Oregon, 443\",\n" + 
			 		"		\"about\": \"Ex ipsum ipsum duis ex ipsum sit. Mollit officia non veniam dolore id anim Lorem. Ut Lorem esse sint aliqua enim aliquip veniam ex fugiat veniam irure. Consectetur id reprehenderit consectetur velit sunt elit in ullamco enim cupidatat ex. Aliqua tempor incididunt ipsum consectetur officia ipsum dolor nostrud velit id deserunt nostrud aliquip ipsum. Nisi aute amet non velit quis pariatur in minim. Culpa aliquip dolor fugiat do eiusmod quis laboris nisi incididunt sit ea ea nisi enim.\\r\\n\",\n" + 
			 		"		\"registered\": \"2015-04-23T12:57:10 +04:00\",\n" + 
			 		"		\"latitude\": -84.043965,\n" + 
			 		"		\"longitude\": 34.889473,\n" + 
			 		"		\"tags\": [\n" + 
			 		"			\"aliquip\",\n" + 
			 		"			\"in\",\n" + 
			 		"			\"esse\",\n" + 
			 		"			\"qui\",\n" + 
			 		"			\"esse\",\n" + 
			 		"			\"culpa\",\n" + 
			 		"			\"deserunt\"\n" + 
			 		"		],\n" + 
			 		"		\"friends\": {\n" + //Missing bracket
			 		"				\"id\": 0,\n" + 
			 		"				\"name\": \"Rodriquez Mccoy\"\n" + 
			 		"			},\n" + 
			 		"			{\n" + 
			 		"				\"id\": 1,\n" + 
			 		"				\"name\": \"Vicky Lester\"\n" + 
			 		"			},\n" + 
			 		"			{\n" + 
			 		"				\"id\": 2,\n" + 
			 		"				\"name\": \"Aimee Watts\"\n" + 
			 		"			}\n" + 
			 		"		],\n" + 
			 		"		\"greeting\": \"Hello, Michael Bray! You have 1 unread messages.\",\n" + 
			 		"		\"favoriteFruit\": \"strawberry\"\n" + 
			 		"	}";
	     ResponseEntity<Document> result = rest.postForEntity(URL + "/objects", obj, Document.class);
	     assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	     assertThat(objectRepository.count()).isEqualTo(0L);
	 }
	 
	 @DisplayName("POST /object with duplicate entry")
	 @Test
	 void postDuplicateObjects() {
		 String obj = "{\n" + 
		 		"    \"firstName\": \"Andrey\",\n" + 
		 		"    \"lastName\": \"Kolmogorov\",\n" + 
		 		"    \"dob\": \"25 April 1903\"\n" + 
		 		"}";
	     ResponseEntity<Document> result = rest.postForEntity(URL + "/objects", obj, Document.class);
	     assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
	     Document objectResult = result.getBody();
	     assertThat(objectResult.get("uid")).isNotNull();
	     Object firstUid = objectResult.get("uid");
	     
	     String obj2 = "{\n" + 
			 		"    \"firstName\": \"Andrey\",\n" + 
			 		"    \"lastName\": \"Kolmogorov\",\n" + 
			 		"    \"dob\": \"25 April 1903\"\n" + 
			 		"}";
	     ResponseEntity<Document> result2 = rest.postForEntity(URL + "/objects", obj2, Document.class);
		 assertThat(result2.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		 Document objectResult2 = result2.getBody();
		 assertThat(objectResult2.get("uid")).isNotNull();
		 Object secondUid = objectResult2.get("uid");
		 assertThat(firstUid).isNotEqualTo(secondUid);
	     assertThat(objectRepository.count()).isEqualTo(2L);
	 }
	 
	 @DisplayName("GET /objects with all objects")
	 @Test
	 void getAllObjects() {
		 Object uid = getUid();
		 ResponseEntity<String> getResult = rest.exchange(URL + "/objects/", HttpMethod.GET, null,
																					String.class);

	     assertThat(getResult.getStatusCode()).isEqualTo(HttpStatus.OK);
	     assertThat(getResult.getBody()).isEqualTo("[{\"url\":\""+ apiUrl+uid+ "\"}]");
	 }
	 
	 @DisplayName("GET /object/{uid}")
	 @Test
	 void getObjectByUid() {
		 String obj = "{\n" + 
			 		"    \"firstName\": \"Andrey\",\n" + 
			 		"    \"lastName\": \"Kolmogorov\",\n" + 
			 		"    \"dob\": \"25 April 1903\"\n" + 
			 		"}";
		 Document parseObj = Document.parse(obj);
		 ResponseEntity<Document> result = rest.postForEntity(URL + "/objects", obj, Document.class);
		 assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		 Document objectResult = result.getBody();
		 Object uid = objectResult.get("uid");
	     ResponseEntity<Document> getResult = rest.getForEntity(URL + "/objects/" + uid, Document.class);
	     assertThat(getResult.getStatusCode()).isEqualTo(HttpStatus.OK);
	     Document doc = getResult.getBody();
	     doc.remove("uid");
	     assertThat(doc).isEqualTo(parseObj);
	 }
	 
	 @DisplayName("GET /object/{uid} with invalid uid")
	 @Test
	 void getObjectByInvalidUid() {
	     ResponseEntity<Document> getResult = rest.getForEntity(URL + "/objects/123", Document.class);

	     assertThat(getResult.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	 }
	 
	 @DisplayName("DELETE /object/{id}")
	 @Test
	 void deleteObjectById() {
		 Object idInserted = getUid();
		 ResponseEntity<String> result = rest.exchange(URL + "/objects/" + idInserted, HttpMethod.DELETE, null,
																									String.class);
	     assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
	     assertThat(objectRepository.count()).isEqualTo(0L);
	 }
	 
	 @DisplayName("DELETE /object/{id}")
	 @Test
	 void deleteObjectByNonexistingId() {
		 Object idInserted = "whatever456h";
	     ResponseEntity<String> result = rest.exchange(URL + "/objects/" + idInserted, HttpMethod.DELETE, null,
																									String.class);
	     assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	     assertThat(objectRepository.count()).isEqualTo(0L);
	 }
	 
	 @DisplayName("PUT /object/{uid} with an existing id")
	 @Test
	 void putObject() {
	     Object idInserted = getUid();
	     String obj = "{\n" + 
	     		"    \"uid\": \""+idInserted+"\",\n" + 
	     		"    \"firstName\": \"Andrey\",\n" + 
	     		"    \"lastName\": \"Kolmogorov\",\n" + 
	     		"    \"dob\": \"19030425\",\n" + 
	     		"    \"dod\": \"19871020\"\n" + 
	     		"}";
	     HttpEntity<String> body = new HttpEntity<>(obj);
	     ResponseEntity<Document> result = rest.exchange(URL + "/objects/"+idInserted, HttpMethod.PUT, body, 
	    	new ParameterizedTypeReference<Document>() {
	     });
	     assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
	     Document objRes = objectRepository.findOne(idInserted.toString());
	     objRes.remove("_id");
	     assertThat(result.getBody()).isEqualTo(objRes);
	     assertThat(objectRepository.count()).isEqualTo(1L);
	     assertThat(result.getBody().toString()).isNotEqualTo(obj);
	 }
	 
	 @DisplayName("PUT /object/{uid} with a non existing id")
	 @Test
	 void putNonexistingObject() {
		 String obj = "{\"hey\":\"hai\"}";
	     HttpEntity<String> body = new HttpEntity<>(obj);
	     ResponseEntity<Document> result = rest.exchange(URL + "/objects/167yhb399047hnkk", HttpMethod.PUT, body, 
	    	new ParameterizedTypeReference<Document>() {
	     });
	     assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	     assertThat(objectRepository.count()).isEqualTo(0L);
	     assertThat(result.getBody().toString()).isNotEqualTo(obj);
	 }
	 
	 @DisplayName("PUT /object/{uid} with invalid JSON")
	 @Test
	 void putInvalidJSONObject() {
		 Object idInserted = getUid();
		 String obj = "[\n" + 
		 		"    \"test\" : 123\n" + 
		 		"]";
		 HttpEntity<String> body = new HttpEntity<>(obj);
	     ResponseEntity<Document> result = rest.exchange(URL + "/objects/" + idInserted, HttpMethod.PUT, body, 
	    	new ParameterizedTypeReference<Document>() {
	     });
	     assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	     assertThat(objectRepository.count()).isEqualTo(1L);
	     assertThat(result.getBody().toString()).isNotEqualTo(obj);
	 }
	 
	 @DisplayName("PUT /object/{uid} with completely different JSON")
	 @Test
	 void putDifferentJSONObject() {
		 Object idInserted = getUid();
		 String obj = "{\n" + 
		 		"  \"model\": {\n" + 
		 		"    \"largest\": [\n" + 
		 		"      762224629,\n" + 
		 		"      false,\n" + 
		 		"      true,\n" + 
		 		"      \"review\",\n" + 
		 		"      true,\n" + 
		 		"      1065036788\n" + 
		 		"    ],\n" + 
		 		"    \"day\": false,\n" + 
		 		"    \"quiet\": \"rise\",\n" + 
		 		"    \"plan\": true,\n" + 
		 		"    \"report\": -625094081.9526668,\n" + 
		 		"    \"pupil\": \"tea\"\n" + 
		 		"  },\n" + 
		 		"  \"stick\": 1110022222,\n" + 
		 		"  \"happily\": false,\n" + 
		 		"  \"secret\": false,\n" + 
		 		"  \"scientific\": 2111744359,\n" + 
		 		"  \"create\": false\n" + 
		 		"}";
		 HttpEntity<String> body = new HttpEntity<>(obj);
	     ResponseEntity<Document> result = rest.exchange(URL + "/objects/" + idInserted, HttpMethod.PUT, body, 
	    	new ParameterizedTypeReference<Document>() {
	     });
	     assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
	     assertThat(objectRepository.count()).isEqualTo(1L);
	     assertThat(result.getBody().toString()).isNotEqualTo(obj);
	 }
	 
	 @DisplayName("POST object by passing a uid")
	 @Test
	 void postWithUid() {
		 String obj = "{\n" + 
			 		"    \"firstName\": \"Andrey\",\n" + 
			 		"    \"lastName\": \"Kolmogorov\",\n" + 
			 		"    \"dob\": \"25 April 1903\"\n" + 
			 		"}";
		 ResponseEntity<Document> result = rest.postForEntity(URL + "/objects/5e56e94e10a88a53a39690a3", obj, Document.class);
		 assertThat(result.getStatusCode()).isEqualTo(HttpStatus.METHOD_NOT_ALLOWED);
	     assertThat(objectRepository.count()).isEqualTo(0L);
	 }

	 private Object getUid() {
		 String obj = "{\n" + 
			 		"    \"firstName\": \"Andrey\",\n" + 
			 		"    \"lastName\": \"Kolmogorov\",\n" + 
			 		"    \"dob\": \"25 April 1903\"\n" + 
			 		"}";
		 ResponseEntity<Document> result = rest.postForEntity(URL + "/objects", obj, Document.class);
		 assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		 Document objectResult = result.getBody();
		 Object uid = objectResult.get("uid");
		 return uid;
	 }
}