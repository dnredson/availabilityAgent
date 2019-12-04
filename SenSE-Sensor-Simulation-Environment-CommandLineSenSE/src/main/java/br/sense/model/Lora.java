package br.sense.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.commons.codec.binary.Base64;

import br.sense.code.CommandExecutor;
import br.sense.code.Param;
import br.sense.code.RandomController;

public class Lora {
	private String phyPayload = "QFyihgAAAAABUTjd7g3rTDF7g/k=";
	private int channel = 0;
	private String codeRate = "4/5";
	private int crcStatus = 1;
	private DataRate datarate;
	private int frequency = 915000000;
	private int loraSNR = 7;
	private String mac;
	private int rfChain = 1;
	private int rssi = -57;
	private int size = 23;
	private String time; // ou Date?
	private String timestamp;
	//private CommandExecutor commandExecutor = new CommandExecutor();
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

	public Lora() {
		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
	}

	public String getPhyPayload() {
		return phyPayload;
	}

	public void setPhyPayload(String phyPayload) {
		this.phyPayload = phyPayload;
	}

	public int getChannel() {
		return channel;
	}

	public void setChannel(int channel) {
		this.channel = channel;
	}

	public String getCodeRate() {
		return codeRate;
	}

	public void setCodeRate(String codeRate) {
		this.codeRate = codeRate;
	}

	public int getCrcStatus() {
		return crcStatus;
	}

	public void setCrcStatus(int crcStatus) {
		this.crcStatus = crcStatus;
	}

	public DataRate getDatarate() {
		return datarate;
	}

	public void setDatarate(DataRate datarate) {
		this.datarate = datarate;
	}

	public int getFrequency() {
		return frequency;
	}

	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}

	public int getLoraSNR() {
		return loraSNR;
	}

	public void setLoraSNR(int loraSNR) {
		this.loraSNR = loraSNR;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public int getRfChain() {
		return rfChain;
	}

	public void setRfChain(int rfChain) {
		this.rfChain = rfChain;
	}

	public int getRssi() {
		return rssi;
	}

	public void setRssi(int rssi) {
		this.rssi = rssi;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String generateMessage() {
		Date dateNow = new Date();
		String msg = "{\"phyPayload\":\"" + this.generatePhyPayload(dateNow.getTime()) + "\","
				+ "\"rxInfo\":{\"channel\":" + this.channel + ",\"codeRate\":\"" + this.codeRate + "\",\"crcStatus\":"
				+ this.crcStatus + "," + "\"dataRate\":{\"bandwidth\":" + this.datarate.getBandwith()
				+ ",\"modulation\":\"" + this.datarate.getModulation() + "\",\"spreadFactor\":"
				+ this.datarate.getSpreadFactor() + ",\"bitrate\":" + this.datarate.getBitrate() + "},"
				+ " \"frequency\":" + this.frequency + ",\"loRaSNR\":" + this.loraSNR + ",\"mac\":\"" + this.mac
				+ "\",\"rfChain\":" + this.rfChain + ",\"rssi\":" + this.rssi + ",\"size\":" + this.size + ","; // "}}";

		msg += /* " \"time\":\"" + sdf.format(dateNow) + */"\"timestamp\":" + ((dateNow.getTime()) / 1000) + "}}";
		return msg;
	}

	public String generateApplicationMessage() {
		// {"applicationID":"1","applicationName":"application","deviceName":"device","devEUI":"0102030405060708","txInfo":{"frequency":915000000,"dr":5},"adr":false,"fCnt":0,"fPort":1,"data":"dHN8MTU0ODI2MzgyNDE1Ng=="}
		//Date dateNow = new Date();
		String ul = "ts|" + (new Date()).getTime();
		String msg = "{\"applicationID\":\"1\",\"applicationName\":\"application\",\"deviceName\":\"device\",\"devEUI\":\"0102030405060708\",\"txInfo\":"
				+ "{\"frequency\":915000000,\"dr\":5},\"adr\":false,\"fCnt\":0,\"fPort\":1,\"data\":\"" + encodeBase64(ul) + "\"}";

		// msg += /* " \"time\":\"" + sdf.format(dateNow) + */"\"timestamp\":" +
		// ((dateNow.getTime()) / 1000) + "}}";
		return msg;
	}
	
	private String encodeBase64(String msg) {
		// Encode data on your side using BASE64
		byte[] bytesEncoded = Base64.encodeBase64(msg.getBytes());
		//System.out.println("encoded value is " + new String(bytesEncoded));

		return new String(bytesEncoded);
	}

	public static String generatePhyPayload(long timestamp) {
		// float temp = RandomController.nextInt(5) + RandomController.nextFloat();
		// String msg = "t1|" + (RandomController.nextInt(40) + 10) + "|t2|" +
		// (RandomController.nextInt(40) + 10) + "|t3|"
		// + (RandomController.nextInt(40) + 10) + "|h1|" +
		// (RandomController.nextInt(75) + 10) + "|h2|"
		// + (RandomController.nextInt(75) + 10) + "|h3|" +
		// (RandomController.nextInt(75) + 10) + "|c|" + temp
		// + "|ts|" + timestamp;
		// String msg = "t1|" + (RandomController.nextInt(40) + 10) + "ts|" + timestamp;
		String msg = "ts|" + timestamp; // + "m1|" + /*(RandomController.nextInt(250) + 100)*/ (Param.counter++);

		String cmd = "./lorasim -f " + /* (Param.counter++) */0 + " -a " + Param.endDevice + " -nk " + Param.nwKey
				+ " -k " + Param.appKey + " -d " + msg;
		String output = (CommandExecutor.executeCommand(cmd)).replaceAll("\n", "");

		return output;
	}
	public String generatePhyPayload(String msg) {
		// float temp = RandomController.nextInt(5) + RandomController.nextFloat();
		// String msg = "t1|" + (RandomController.nextInt(40) + 10) + "|t2|" +
		// (RandomController.nextInt(40) + 10) + "|t3|"
		// + (RandomController.nextInt(40) + 10) + "|h1|" +
		// (RandomController.nextInt(75) + 10) + "|h2|"
		// + (RandomController.nextInt(75) + 10) + "|h3|" +
		// (RandomController.nextInt(75) + 10) + "|c|" + temp
		// + "|ts|" + timestamp;
		// String msg = "t1|" + (RandomController.nextInt(40) + 10) + "ts|" + timestamp;
		//String msg = "ts|" + timestamp; // + "m1|" + /*(RandomController.nextInt(250) + 100)*/ (Param.counter++);

		String cmd = "./lorasim -f " + /* (Param.counter++) */0 + " -a " + Param.endDevice + " -nk " + Param.nwKey
				+ " -k " + Param.appKey + " -d " + msg;
		String output = (CommandExecutor.executeCommand(cmd)).replaceAll("\n", "");

		return output;
	}

}
