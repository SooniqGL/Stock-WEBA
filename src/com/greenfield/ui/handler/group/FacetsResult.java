package com.greenfield.ui.handler.group;

import java.util.List;

public class FacetsResult {
	private int total;
	private List<GroupCount> termList;
	private List<MessageObject> messageList;
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public List<GroupCount> getTermList() {
		return termList;
	}
	public void setTermList(List<GroupCount> termList) {
		this.termList = termList;
	}
	public List<MessageObject> getMessageList() {
		return messageList;
	}
	public void setMessageList(List<MessageObject> messageList) {
		this.messageList = messageList;
	}

}
