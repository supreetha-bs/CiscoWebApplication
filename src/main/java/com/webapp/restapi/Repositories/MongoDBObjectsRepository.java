package com.webapp.restapi.Repositories;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.FindOneAndReplaceOptions;
import com.mongodb.client.model.Projections;
import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.ReturnDocument.AFTER;

@Repository
public class MongoDBObjectsRepository implements ObjectsRepository {

	@Autowired
	private MongoClient client;
	private MongoCollection<Document> objectCollection;
	
	@Value("${spring.data.url}")
    private String apiUrl;

	@PostConstruct
	void init() {
		objectCollection = client.getDatabase("rest_apis").getCollection("objects");
	}
	
	@Override
	public Document save(Document object) {
		ObjectId objectId = ObjectId.get();
		object.append("_id", objectId);
		object.append("uid", objectId.toHexString());
		objectCollection.insertOne(object);
		object.remove("_id");
		return object;
	}
	
	@Override
	public Document update(Document object, String uid) {
        FindOneAndReplaceOptions options = new FindOneAndReplaceOptions().returnDocument(AFTER).projection(Projections.excludeId());
        if (!object.containsKey("uid")) {
        	object.append("uid", uid);
        }
        return objectCollection.findOneAndReplace(eq("uid", uid), object, options);
    }
	
	@Override
	public Document findOne(String uid) {
		return objectCollection.find(eq("uid", uid)).first();
	}
	
	@Override
	public List<Document> findAll() {
		List<Document> docs = new ArrayList<Document>();
		FindIterable<Document> fi = objectCollection.find();
		MongoCursor<Document> cursor = fi.iterator();
        try {
            while(cursor.hasNext()) {
            	Object uid = cursor.next().get("uid");
            	Document db = new Document("url", apiUrl+uid.toString()); 
            	docs.add(db);
            }
        } finally {
            cursor.close();
        }
        return docs;
	}
	
	@Override
	public long delete(String id) {
		return objectCollection.deleteOne(eq("uid", id)).getDeletedCount();
	}
	
	@Override
    public long deleteAll() {
		return objectCollection.deleteMany(new BsonDocument()).getDeletedCount();
    }
	
	@Override
    public long count() {
        return objectCollection.countDocuments();
    }
}
