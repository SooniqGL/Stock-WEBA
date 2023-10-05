package com.greenfield.ui.handler.group;


import java.util.ArrayList;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class MessageObject {
	@JsonIgnore
	private static final long serialVersionUID = 1L;

	@JsonIgnore
    private String id;    // msg id, unique
    
    private ArrayList<String> g;    // group list
    
    private String s;     // subject
    private String c;    // content
    
    private Date d;    // date
    private String n;  // user name
    private String u;   // user id
    
    public MessageObject(String id, String subject, String content, 
    		Date messageDate, String userName, 
    		String userId, ArrayList<String> groupList) {
    	this.id = id;
    	this.s = subject;
    	this.c = content;
    	this.d = messageDate;
    	this.n = userName;
    	this.u = userId;
    	this.g = groupList;
        
    }

    @JsonIgnore
	public String getId() {
		return id;
	}

    @JsonIgnore
	public void setId(String id) {
		this.id = id;
	}

	public ArrayList<String> getG() {
		return g;
	}

	public void setG(ArrayList<String> g) {
		this.g = g;
	}

	public String getS() {
		return s;
	}

	public void setS(String s) {
		this.s = s;
	}

	public String getC() {
		return c;
	}

	public void setC(String c) {
		this.c = c;
	}

	public Date getD() {
		return d;
	}

	public void setD(Date d) {
		this.d = d;
	}

	public String getN() {
		return n;
	}

	public void setN(String n) {
		this.n = n;
	}

	public String getU() {
		return u;
	}

	public void setU(String u) {
		this.u = u;
	}

	@Override
    public String toString() {
        return "MessageObject: [id=" + id + ", subject=" + s + ", content=" + c + ", userName=" + n
                + ", messageDate=" + d + ", userId=" + u + ", groupList=" + g + "]";
    }
}
