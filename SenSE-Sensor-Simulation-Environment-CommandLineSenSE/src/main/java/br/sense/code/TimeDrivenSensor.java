package br.sense.code;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import br.sense.model.Entity;
import br.sense.model.Lora;

public class TimeDrivenSensor extends GenericSensor implements Runnable, MqttCallback {
	private volatile boolean exit = false;

	private final int nSensors;
	private Lora loraSensor;
	private LinkedList<Sensor> sensor = new LinkedList<>();

	public TimeDrivenSensor(String sensorType, int number_of_sensors, String topic, CountDownLatch latch) {
		super(sensorType, topic, latch);

		this.duration = setSensorPeriodicity(sensorType);// * 1000 * 60;
		this.nSensors = number_of_sensors;
	}

	public TimeDrivenSensor(String sensorType, String[] messageType, int duration, int number_of_sensors, String topic,
			CountDownLatch latch) {
		super(sensorType, messageType, duration, topic, latch);

		this.duration = setSensorPeriodicity(sensorType); //duration;// * 1000;
		this.nSensors = number_of_sensors;

	}

	public TimeDrivenSensor(String sensorType, String[] messageType, String[] max, String[] min, int duration,
			int number_of_sensors, String topic, CountDownLatch latch) {
		super(sensorType, messageType, max, min, duration, topic, latch);
		// this.idController.setId();
		this.duration = duration; // * 1000;
		this.nSensors = number_of_sensors;
	}

	public TimeDrivenSensor(Lora loraSensor, String topic, String sensorType, int number_of_sensors,
			CountDownLatch latch) {
		super(sensorType, topic, latch); // "gateway/" + loraSensor.getMac() + "/rx"
		System.out.println("--------CREATING LoRA SENSOR--------");
		this.loraSensor = loraSensor;

		this.duration = setSensorPeriodicity(sensorType);// * 1000 * 60;
		this.nSensors = number_of_sensors;
	}

	@Override
	public void run() {
		System.out.println(
				sensorType + " sends a message  in each " + this.duration + " ms. We will generate " + nSensors + "\n");
		// random.setSeed(Param.seed);
		if (this.initializeSensors()) {
			try {
				if (this.sensorType == "lora") {
					this.publishLora();
				} else if (this.sensorType == "app-lora") {
					this.publishAppLora();
				} else if (this.sensorType == "probe_ul") {
					this.publishProbe();
				} else if (this.sensorType == "http") {
					this.publishOrionHttp();
				} else if (this.sensorType == "reasoner") {
					this.publishReasonerHttp();
				} else if (this.sensorType == "udp") {
					this.publishUDP();
				} else {
					this.publishGeneric();
				}
			} catch (IOException ex) {
				Logger.getLogger(TimeDrivenSensor.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	public void stop() {
		exit = true;
	}

	private boolean initializeSensors() {

		int ind;
		for (int i = 0; i < nSensors; i++) {
			Sensor s1 = new Sensor();
			s1.setDuration(duration);
			s1.setId(IdController.getId(sensorType));
			sensor.add(s1);
		}

		List<Integer> values = new ArrayList<Integer>();
		for (int j = 0; j < duration; j += 10)
			values.add(j);

		int sensorCount = 0;
		do {
			if (values.size() == 1) {
				ind = 0;
			} else {
				ind = RandomController.nextInt(values.size());
			}
			sensor.get(sensorCount++).setStartSend(values.get(ind));
			values.remove(ind);
		} while (sensorCount != sensor.size() && values.size() != 0);

		if (values.size() == 0 && sensorCount != sensor.size()) {
			System.out.println("SenSE is not able to simulate that quantity of devices");
			return false;
		} else {
			return true;
		}

	}

	private int setSensorPeriodicity(String name) {
		int p;
		switch (name) {
		case "lora":
			p = Param.probe_periodicity;
			break;
		case "udp":
			p = Param.periodicity;
			break;
		case "app-lora":
			p = Param.probe_periodicity;
			break;
		case "structural":
			p = 10 * 1000 * 60;
			break;
		case "air":
			p = 30 * 1000 * 60;
			break;
		case "traffic":
			p = 10 * 1000 * 60;
			break;
		case "noise":
			p = 10 * 1000 * 60;
			break;
		case "waste":
			p = 60 * 1000 * 60;
			break;
		case "probe_c":
			p = Param.probe_periodicity;
			break;
		case "probe_h":
			p = Param.probe_periodicity;
			break;
		case "probe_t":
			p = Param.probe_periodicity;
			break;
		case "probe_ul":
			p = Param.probe_periodicity;
			break;
		case "http":
			p = Param.probe_periodicity;
			break;
		case "reasoner":
			p = Param.probe_periodicity;
			break;
		default:
			p = 0;
			break;
		}
		return p;
	}

	private String setSensorInfo(String name, int idSensor) {

		String msg = "";
		if (messageType == null) {
			float temp;
			switch (name) {
			// case "lora":
			//
			// return msg;
			case "structural":
				msg += "type=structural;resource=" + idSensor + ";message=";
				for (int i = 0; i < 7; i++) {
					temp = (RandomController.nextInt(99) + RandomController.nextFloat());
					msg += (temp + ",");
				}
				return msg;
			case "air":
				msg += "type=air;resource=" + idSensor + ";message=";
				for (int i = 0; i < 6; i++) {
					temp = (RandomController.nextInt(99) + RandomController.nextFloat());
					msg += (temp + ",");
				}
				return msg;
			case "traffic":
				msg += "type=traffic;resource=" + idSensor + ";message=";
				for (int i = 0; i < 20; i++) {
					temp = RandomController.nextInt(99) + RandomController.nextFloat();
					msg += (temp + ",");
				}
				return msg;
			case "noise":
				msg += "type=noise;resource=" + idSensor + ";message=";
				msg += RandomController.nextInt(300);
				return msg;
			case "waste":
				msg += "type=waste;resource=" + idSensor + ";message=";
				msg += RandomController.nextFloat() + "";
				return msg;
			case "probe_t":
				msg += "{\"id\":\"" + idSensor + "\",\"t\":[" + (RandomController.nextInt(40) + 10) + ","
						+ (RandomController.nextInt(40) + 10) + "," + (RandomController.nextInt(40) + 10) + "],";
				return msg;
			case "probe_h":
				msg += "{\"id\":\"" + idSensor + "\",\"h\":[" + (RandomController.nextInt(75) + 10) + ","
						+ (RandomController.nextInt(75) + 10) + "," + (RandomController.nextInt(75) + 10) + "],";
				return msg;
			case "probe_c":
				temp = RandomController.nextInt(5) + RandomController.nextFloat();
				msg += "{\"id\":\"" + idSensor + "\",\"c\":" + temp + ",";
				return msg;
			case "probe_ul":
				temp = RandomController.nextInt(5) + RandomController.nextFloat();
				msg += "t1|" + (RandomController.nextInt(40) + 10) + "|t2|" + (RandomController.nextInt(40) + 10)
						+ "|t3|" + (RandomController.nextInt(40) + 10) + "|m1|" + (RandomController.nextInt(75) + 10)
						+ "|m2|" + (RandomController.nextInt(75) + 10) + "|m3|" + (RandomController.nextInt(75) + 10)
						+ "|c1|" + (RandomController.nextInt(5) + RandomController.nextFloat())
						+ "|c2|" + (RandomController.nextInt(5) + RandomController.nextFloat())
						+ "|c3|" + (RandomController.nextInt(5) + RandomController.nextFloat());
				return msg;
			default:
				msg = "ERROR: Sensor Type not found";
				return msg;
			}
		} else {
			msg += "type=" + sensorType + ";resource=" + idController.getId() + ";message=";
			// for (int i = 0; i < messageType.length; i++) {
			if (!(messageType[0].equals("booleanText"))) {
				if (max == null)
					msg += getRandomData(messageType[0]) + ";";
				else
					msg += getRandomData(messageType[0], max[0], min[0]);
			} else {
				msg += (RandomController.nextInt() % 2 == 0) ? max[0] : min[0];
			}
			// }
			return msg;
		}
	}

	public void publishAppLora() throws IOException {

		// String m = this.setSensorInfo(sensorType, 1); caso dê merda, descomentar essa
		// linha e o setSensorInfo que se refere ao LoRa
		Sensor temp, temp2;
		int timeToSleep = 1;
		try {
			if (client == null) {
				this.connectMQTT();
			}
			Collections.sort(sensor);
			temp = sensor.peek();
			// start time!
			TimeControl.startTime();
			try {
				Thread.sleep(temp.getStartSend());
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			// MqttMessage message = new MqttMessage(); caso dê merda, descomentar essa
			// linha e o setSensorInfo que se refere ao LoRa
			// message.setPayload(m.getBytes()); caso dê merda, descomentar essa linha e o
			// setSensorInfo que se refere ao LoRa
			do {

				if (!sensor.isEmpty())
					temp = (Sensor) sensor.remove();

				String m = this.loraSensor.generateApplicationMessage();// caso dê merda, tirar o String dessa linha e
																		// descomentar
				// a linha no início dessa funcção que a declara
				client.publish(this.topic, m.getBytes(), Param.qos, false);
				System.out.println(this.topic + " " + m);

				if (Param.writeFile) {
					// writeFile(m, time, numberOfMsg);
					writeFile();
				}
				if (temp != null) {
					timeToSleep = temp.getStartSend();
					temp.setStartSend(temp.getStartSend() + duration);
					sensor.add(temp);
				}
				if (!sensor.isEmpty()) {
					temp2 = (Sensor) sensor.peek();
					timeToSleep = temp2.getStartSend() - timeToSleep;
					m = this.setSensorInfo(sensorType, temp2.getId());
				}
				try {
					Thread.sleep(timeToSleep);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} while (!exit);
		} catch (MqttException ex) {
			System.out.println(ex);
			Logger.getLogger(TimeDrivenSensor.class.getName()).log(Level.SEVERE, null, ex);
		}

		try {
			client.disconnect(); // problema?
		} catch (MqttException ex) {
			Logger.getLogger(TimeDrivenSensor.class.getName()).log(Level.SEVERE, null, ex);
		}
		latch.countDown();
	}

	public void publishLora() throws IOException {
		// String m = this.setSensorInfo(sensorType, 1); caso dê merda, descomentar essa
		// linha e o setSensorInfo que se refere ao LoRa
		Sensor temp, temp2;
		int timeToSleep = 1;
		try {
			if (client == null) {
				this.connectMQTT();
			}
			Collections.sort(sensor);
			temp = sensor.peek();
			// start time!
			TimeControl.startTime();
			try {
				Thread.sleep(temp.getStartSend());
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			// MqttMessage message = new MqttMessage(); caso dê merda, descomentar essa
			// linha e o setSensorInfo que se refere ao LoRa
			// message.setPayload(m.getBytes()); caso dê merda, descomentar essa linha e o
			// setSensorInfo que se refere ao LoRa
			do {

				if (!sensor.isEmpty())
					temp = (Sensor) sensor.remove();

				String m = this.loraSensor.generateMessage(); // caso dê merda, tirar o String dessa linha e descomentar
																// a linha no início dessa funcção que a declara
				client.publish(this.topic, m.getBytes(), Param.qos, false);
				System.out.println(this.topic + " " + m);

				if (Param.writeFile) {
					// writeFile(m, time, numberOfMsg);
					writeFile();
				}
				if (temp != null) {
					timeToSleep = temp.getStartSend();
					temp.setStartSend(temp.getStartSend() + duration);
					sensor.add(temp);
				}
				if (!sensor.isEmpty()) {
					temp2 = (Sensor) sensor.peek();
					timeToSleep = temp2.getStartSend() - timeToSleep;
					m = this.setSensorInfo(sensorType, temp2.getId());
				}
				try {
					Thread.sleep(timeToSleep);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} while (!exit);
		} catch (MqttException ex) {
			Logger.getLogger(TimeDrivenSensor.class.getName()).log(Level.SEVERE, null, ex);
		}

		try {
			client.disconnect(); // problema?
		} catch (MqttException ex) {
			Logger.getLogger(TimeDrivenSensor.class.getName()).log(Level.SEVERE, null, ex);
		}
		latch.countDown();
	}

	public void publishUDP() throws IOException {
		Sensor temp, temp2;
		int timeToSleep = 1;
		Entity entity = new Entity(0);
		Collections.sort(sensor);
		temp = sensor.peek();
		TimeControl.startTime();
		try {
			Thread.sleep(temp.getStartSend());
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		UDPSend udpSend = new UDPSend(Param.host,Param.port,loraSensor);
		do {
			if (!sensor.isEmpty())
				temp = (Sensor) sensor.remove();
			
			udpSend.SendUDPMessage("ts|"+System.currentTimeMillis());			
			
			if (Param.writeFile) {
				// writeFile(m, time, numberOfMsg);
				writeFile();
			}
			if (temp != null) {
				timeToSleep = temp.getStartSend();
				temp.setStartSend(temp.getStartSend() + duration);
				sensor.add(temp);
			}
			if (!sensor.isEmpty()) {
				temp2 = (Sensor) sensor.peek();
				timeToSleep = temp2.getStartSend() - timeToSleep;
			}
			try {
				Thread.sleep(timeToSleep);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} while (!exit);
		//closing connection
		udpSend.CloseUDPconnection();
		latch.countDown();
	}
	
	public void publishOrionHttp() throws IOException {
		Sensor temp, temp2;
		int timeToSleep = 1;
		Entity entity = new Entity(0);
		Collections.sort(sensor);
		temp = sensor.peek();
		// start time!
		TimeControl.startTime();
		try {
			Thread.sleep(temp.getStartSend());
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		do {
			if (!sensor.isEmpty())
				temp = (Sensor) sensor.remove();

			entity.setId(temp.getId());
			entity.setTimestamp();

			if (temp.isFirstMessage())
				JavaPostRequest.createEntity(entity);
			else
				JavaPostRequest.updateEntity(entity);

			System.out.println();

			if (Param.writeFile) {
				// writeFile(m, time, numberOfMsg);
				writeFile();
			}
			if (temp != null) {
				timeToSleep = temp.getStartSend();
				temp.setStartSend(temp.getStartSend() + duration);
				sensor.add(temp);
			}
			if (!sensor.isEmpty()) {
				temp2 = (Sensor) sensor.peek();
				timeToSleep = temp2.getStartSend() - timeToSleep;
			}
			try {
				Thread.sleep(timeToSleep);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} while (!exit);

		latch.countDown();
	}

	public void publishReasonerHttp() throws IOException {

		Sensor temp, temp2;
		int timeToSleep = 1;
		ReasonerMessage message = new ReasonerMessage();
		Collections.sort(sensor);
		temp = sensor.peek();
		// start time!
		TimeControl.startTime();
		try {
			Thread.sleep(temp.getStartSend());
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		do {
			if (!sensor.isEmpty())
				temp = (Sensor) sensor.remove();

			JavaPostRequest.sendReasonerPost(message);

			System.out.println();

			if (Param.writeFile) {
				// writeFile(m, time, numberOfMsg);
				writeFile();
			}
			if (temp != null) {
				timeToSleep = temp.getStartSend();
				temp.setStartSend(temp.getStartSend() + duration);
				sensor.add(temp);
			}
			if (!sensor.isEmpty()) {
				temp2 = (Sensor) sensor.peek();
				timeToSleep = temp2.getStartSend() - timeToSleep;
			}
			try {
				Thread.sleep(timeToSleep);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} while (!exit);

		latch.countDown();
	}

	public void publishProbe() throws IOException {
		String m = this.setSensorInfo(sensorType, 1);
		Sensor temp, temp2;
		int timeToSleep = 1;
		try {
			if (client == null) {
				this.connectMQTT();
			}
			Collections.sort(sensor);
			temp = sensor.peek();
			// start time!
			TimeControl.startTime();
			try {
				// System.out.println("tempo de espera de primeira msg é "+temp.getStartSend());
				Thread.sleep(temp.getStartSend());
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			MqttMessage message = new MqttMessage();
			message.setPayload(m.getBytes());
			do {

				if (!sensor.isEmpty())
					temp = (Sensor) sensor.remove();

				//m += "|ts|" + (System.currentTimeMillis()); uncomment for timestamp
				client.publish("/"+Param.appKey + "/probe" + (temp.getId()+1) + "/attrs", m.getBytes(), Param.qos, false);
				System.out.println("/"+Param.appKey + "/probe" + (temp.getId()+1) + "/attrs" + "  " + m);

				if (Param.writeFile) {
					// writeFile(m, time, numberOfMsg);
					writeFile();
				}
				if (temp != null) {
					timeToSleep = temp.getStartSend();
					temp.setStartSend(temp.getStartSend() + duration);
					sensor.add(temp);
				}
				if (!sensor.isEmpty()) {
					temp2 = (Sensor) sensor.peek();
					timeToSleep = temp2.getStartSend() - timeToSleep;
					m = this.setSensorInfo(sensorType, temp2.getId());
				}
				try {
					Thread.sleep(timeToSleep);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} while (!exit);
		} catch (MqttException ex) {
			Logger.getLogger(TimeDrivenSensor.class.getName()).log(Level.SEVERE, null, ex);
		}

		try {
			client.disconnect(); // problema?
		} catch (MqttException ex) {
			Logger.getLogger(TimeDrivenSensor.class.getName()).log(Level.SEVERE, null, ex);
		}
		latch.countDown();
	}

	public void publishGeneric() throws IOException {
		// if (client == null) {
		// this.connectMQTT();
		// }
		int numberOfMsg = 0;
		String m = this.setSensorInfo(sensorType, 1);
		Sensor temp, temp2;
		int timeToSleep = 1;
		try {
			if (client == null) {
				this.connectMQTT();
			}
			Collections.sort(sensor);
			temp = sensor.peek();
			// start time!
			TimeControl.startTime();
			try {
				// System.out.println("tempo de espera de primeira msg é "+temp.getStartSend());
				Thread.sleep(temp.getStartSend());
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			MqttMessage message = new MqttMessage();
			message.setPayload(m.getBytes());
			do {

				if (!sensor.isEmpty())
					temp = (Sensor) sensor.remove();

				// numberOfMsg++;// P1=14735427300;P2=?;P3=?;P4=?;P5=?;P6=?;P7=?;REP=-1
				// m = timestamp.replaceFirst(":", "T") +"Z|" +m;
				// this.topic += "/probe"+temp.getId()+"/attrs";
				// if (temp.getId() > this.nSensors) {
				// IdController.resetIds();
				// System.out.println(
				// "\n\n>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>ERROR<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<\n\n\n\n");
				// }

				m += ";REP=" + Param.replication + ";EXP=" + Param.experiment_num + ";timestamp="
						+ (System.currentTimeMillis()) + ";";
				client.publish(this.topic, m.getBytes(), Param.qos, false);
				System.out.println(this.topic + "  " + m);

				if (Param.writeFile) {
					// writeFile(m, time, numberOfMsg);
					writeFile();
				}
				if (temp != null) {
					timeToSleep = temp.getStartSend();
					temp.setStartSend(temp.getStartSend() + duration);
					sensor.add(temp);
				}
				if (!sensor.isEmpty()) {
					temp2 = (Sensor) sensor.peek();
					timeToSleep = temp2.getStartSend() - timeToSleep;
					m = this.setSensorInfo(sensorType, temp2.getId());
				}
				try {
					Thread.sleep(timeToSleep);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} while (!exit);
		} catch (MqttException ex) {
			Logger.getLogger(TimeDrivenSensor.class.getName()).log(Level.SEVERE, null, ex);
		}

		try {
			client.disconnect(); // problema?
		} catch (MqttException ex) {
			Logger.getLogger(TimeDrivenSensor.class.getName()).log(Level.SEVERE, null, ex);
		}
		latch.countDown();
	}

	private static int cont = 0;

	static private void writeFile() throws IOException {
		new Thread() {
			@Override
			public void run() {
				cont++;
				File arquivo = new File(Param.name_experiment + ".csv");
				try (FileWriter fw = new FileWriter(arquivo, true); BufferedWriter bw = new BufferedWriter(fw)) {

					bw.write(String.valueOf(cont) + ";" + Param.replication);
					bw.newLine();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}.start();
	}

}