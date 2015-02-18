package com.with;

public class MessagesInfo {
	public String message;
	public String from;
	
	public MessagesInfo(String message,String from){
		this.message = message;
		this.from = from;
	}
	
	
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	
}
