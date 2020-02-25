package com.webapp.restapi.Repositories;

import com.mongodb.BasicDBObject;
import com.webapp.restapi.Models.Objects;

import org.bson.Document;
import org.springframework.stereotype.Repository;
import java.util.*;
@Repository
public interface ObjectsRepository {

	Document save(Document object);
//
//    List<Users> saveAll(List<Users> persons);

    List<BasicDBObject> findAll();

//    List<Users> findAll(List<String> ids);
//
    Document findOne(String id);
//
//    long count();
//
//    long delete(String id);
//
//    long delete(List<String> ids);
//
//    long deleteAll();
//
    Document update(Document object, String uid);
//
//    long update(List<Users> persons);

}
