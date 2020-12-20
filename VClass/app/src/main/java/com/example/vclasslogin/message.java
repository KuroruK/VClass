package com.example.vclasslogin;

public class message {
    String receiverID;
    String senderID;
    String message;
    String time;
    Boolean isImage;

    public Boolean getImage() {
        return isImage;
    }

    public void setImage(Boolean image) {
        isImage = image;
    }

    public message(String receiverID, String senderID, String message, String time, Boolean isImage) {
        this.receiverID = receiverID;
        this.senderID = senderID;
        this.message = message;
        this.time = time;
        this.isImage=isImage;
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
