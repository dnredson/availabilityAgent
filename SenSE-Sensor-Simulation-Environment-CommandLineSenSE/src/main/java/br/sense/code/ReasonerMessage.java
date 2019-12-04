package br.sense.code;

public class ReasonerMessage {
	private String type = "Probe";
	private String payload;
	private String timestamp;
	private String topic = "DoesNotMatter";

	ReasonerMessage() {
		this.timestamp = String.valueOf(System.currentTimeMillis());
		this.payload = String.valueOf(RandomController.nextInt(50));
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPayload() {
		return payload;
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public void setTimestamp() {
		this.timestamp = String.valueOf(System.currentTimeMillis());
	}
}
// {
// "type":"Probe" ,
// "payload": 20,
// "timestamp": "18378248648",
// "topic":"1"
// }
