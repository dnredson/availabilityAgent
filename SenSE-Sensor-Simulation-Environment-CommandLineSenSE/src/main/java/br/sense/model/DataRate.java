package br.sense.model;

public class DataRate {
	private int bandwith = 125;
	private String modulation = "LORA";
	private int spreadFactor = 7;
	private int bitrate = 0;
	public int getBandwith() {
		return bandwith;
	}
	public void setBandwith(int bandwith) {
		this.bandwith = bandwith;
	}
	public String getModulation() {
		return modulation;
	}
	public void setModulation(String modulation) {
		this.modulation = modulation;
	}
	public int getSpreadFactor() {
		return spreadFactor;
	}
	public void setSpreadFactor(int spreadFactor) {
		this.spreadFactor = spreadFactor;
	}
	public int getBitrate() {
		return bitrate;
	}
	public void setBitrate(int bitrate) {
		this.bitrate = bitrate;
	}
	
}
