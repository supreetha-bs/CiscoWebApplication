package com.webapp.restapi.Models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.bson.types.ObjectId;

import org.bson.Document;


public class Objects {

	
	@JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;
	private String uid;
	
	public ObjectId getId() {
        return id;
    }
	
	public void setUId(String uid) {
        this.uid = uid;
    }
}

