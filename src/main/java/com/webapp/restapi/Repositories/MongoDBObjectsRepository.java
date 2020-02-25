package com.webapp.restapi.Repositories;
import com.mongodb.BasicDBObject;
import com.mongodb.ReadConcern;
import com.mongodb.ReadPreference;
import com.mongodb.TransactionOptions;
import com.mongodb.WriteConcern;
import com.mongodb.client.ClientSession;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.FindOneAndReplaceOptions;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.ReplaceOneModel;
import com.mongodb.client.model.WriteModel;
import com.webapp.restapi.Models.Objects;
import org.bson.BsonDocument;
import org.bson.BsonNull;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Accumulators.avg;
import static com.mongodb.client.model.Aggregates.group;
import static com.mongodb.client.model.Aggregates.project;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.in;
import static com.mongodb.client.model.Projections.excludeId;
import static com.mongodb.client.model.ReturnDocument.AFTER;
import com.mongodb.client.*;
import static java.util.Arrays.asList;

@Repository
public class MongoDBObjectsRepository implements ObjectsRepository {
	
	private static final TransactionOptions txnOptions = TransactionOptions.builder()
																			.readPreference(ReadPreference.primary())
																			.readConcern(ReadConcern.MAJORITY)
																			.writeConcern(WriteConcern.MAJORITY)
																			.build();
	@Autowired
	private MongoClient client;
	private MongoCollection<Document> objectCollection;

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
        return objectCollection.findOneAndReplace(eq("uid", uid), object, options);
    }
	
	@Override
	public Document findOne(String uid) {
		return objectCollection.find(eq("uid", uid)).first();
	}
	
	@Override
	public List<BasicDBObject> findAll() {
		List<BasicDBObject> docs = new ArrayList<BasicDBObject>();
		FindIterable<Document> fi = objectCollection.find();
		MongoCursor<Document> cursor = fi.iterator();
        try {
            while(cursor.hasNext()) {
            	Object uid = cursor.next().get("uid");
            	BasicDBObject db = new BasicDBObject("url", "https://myrestapp.cisco.com/api/objects/"+uid); 
            	docs.add(db);
            }
        } finally {
            cursor.close();
        }
        return docs;
//		return objectCollection.find().projection(excludeId()).into(new ArrayList<>());
	}
}
