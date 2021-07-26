package com.example.vclasslogin;

public class message {
    String receiverID;
    String senderID;
    String message;
    String time;
    Boolean isResource;
    String resourceType;
    String resourceBy;


    public Boolean getResource() {
        return isResource;
    }

    public String getResourceBy() {
        return resourceBy;
    }

    public void setResourceBy(String resourceBy) {
        this.resourceBy = resourceBy;
    }

    public void setResource(Boolean resource) {
        isResource = resource;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public message(String receiverID, String senderID, String message, String time, Boolean isResource, String resourceType,String resourceBy) {
        this.receiverID = receiverID;
        this.senderID = senderID;
        this.message = message;
        this.time = time;
        this.isResource=isResource;
        this.resourceType=resourceType;
        this.resourceBy=resourceBy;
    }
    public message(){

    }

    public String getReceiverID() {
        return receiverID;
    }

    public void setReceiverID(String receiverID) {
        this.receiverID = receiverID;
    }

    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
