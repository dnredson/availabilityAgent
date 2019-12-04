package br.sense.model;

public class Entity {
	private String id;
	private String type = "Probe";
	private Timestamp timestamp = new Timestamp();

	public Entity(int id) {
		this.id = "Probe"+String.valueOf(id);
		timestamp.setValue();
	}

	public String getId() {
		return id;
	}
	
	public int getIdInt() {
		return Integer.parseInt(this.id);
	}

	public void setId(int id) {
		this.id = "Probe"+String.valueOf(id);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp() {
		this.timestamp.setValue();
	}

}
