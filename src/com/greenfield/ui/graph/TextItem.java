package com.greenfield.ui.graph;

public class TextItem {
	private String text;
	private int xPos;
	private int yPos;
	
	public TextItem(String text, int x, int y) {
		this.text = text;
		this.xPos = x;
		this.yPos = y;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public int getXPos() {
		return xPos;
	}
	public void setXPos(int xPos) {
		this.xPos = xPos;
	}
	public int getYPos() {
		return yPos;
	}
	public void setYPos(int yPos) {
		this.yPos = yPos;
	}

}
