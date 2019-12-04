package br.sense.model;

public class Timestamp {
	private String value;
	private String type = "Integer";
	
	public String getValue() {
		return value;
	}
	public void setValue() {
		this.value = String.valueOf(System.currentTimeMillis());
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	
	
}
