package com.webapp.restapi.Repositories;


import org.bson.Document;
import org.springframework.stereotype.Repository;
import java.util.*;
@Repository
public interface ObjectsRepository {

	Document save(Document object);

    List<Document> findAll();

    Document findOne(String id);

    long delete(String id);

    Document update(Document object, String uid);
    
    long deleteAll();
    
    long count();

}
