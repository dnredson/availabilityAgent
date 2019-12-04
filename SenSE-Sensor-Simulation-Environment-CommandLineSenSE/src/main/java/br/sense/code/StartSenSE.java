package br.sense.code;

import br.sense.code.Param;
import br.sense.model.DataRate;
import br.sense.model.EDSensor;
import br.sense.model.Lora;
import br.sense.model.TDSensor;

public class StartSenSE {
	// private static int blue = 1;
	// private static int red = 1;
	// private static int green = 1;
	// private static int waste = 10000;

	public static void main(String[] args) {
		MqttPublish mqttp = new MqttPublish();
		for (int i = 0; i < args.length; i++) {
			if (args[i].equalsIgnoreCase("-sensor")) {
				Param.number_of_devices = Integer.parseInt(args[i + 1]);
			} else if (args[i].equalsIgnoreCase("-lambda")) {
				Param.lambda = Integer.parseInt(args[i + 1]);
			} else if (args[i].equalsIgnoreCase("-rep")) {
				Param.number_of_replications = Integer.parseInt(args[i + 1]);
			} else if (args[i].equalsIgnoreCase("-h")) {
				Param.address = args[i + 1];
			} else if (args[i].equalsIgnoreCase("-t")) {
				Param.topic = args[i + 1];
			} else if (args[i].equalsIgnoreCase("-time")) {
				Param.time_of_experiment = Integer.parseInt(args[i + 1]);
			} else if (args[i].equalsIgnoreCase("-time_b")) {
				Param.time_between_exp = Integer.parseInt(args[i + 1]);
			} else if (args[i].equalsIgnoreCase("-exp")) {
				Param.experiment_num = args[i + 1];
			} else if (args[i].equalsIgnoreCase("-orion")) {
				Param.orionAddress = args[i + 1];
			} else if (args[i].equalsIgnoreCase("-channel")) {
				Param.channel = Integer.parseInt(args[i + 1]);
			} else if (args[i].equalsIgnoreCase("-codeRate")) {
				Param.codeRate = args[i + 1];
			} else if (args[i].equalsIgnoreCase("-crcStatus")) {
				Param.crcStatus = Integer.parseInt(args[i + 1]);
			} else if (args[i].equalsIgnoreCase("-bandwitdh")) {
				Param.bandwidth = Integer.parseInt(args[i + 1]);
			} else if (args[i].equalsIgnoreCase("-modulation")) {
				Param.modulation = args[i + 1];
			} else if (args[i].equalsIgnoreCase("-bitrate")) {
				Param.bitrate = Integer.parseInt(args[i + 1]);
			} else if (args[i].equalsIgnoreCase("-frequency")) {
				Param.frequency = Integer.parseInt(args[i + 1]);
			} else if (args[i].equalsIgnoreCase("-loraSNR")) {
				Param.loraSNR = Integer.parseInt(args[i + 1]);
			} else if (args[i].equalsIgnoreCase("-mac")) {
				Param.mac = args[i + 1];
			} else if (args[i].equalsIgnoreCase("-rfChain")) {
				Param.rfChain = Integer.parseInt(args[i + 1]);
			} else if (args[i].equalsIgnoreCase("-rssi")) {
				Param.rssi = Integer.parseInt(args[i + 1]);
			} else if (args[i].equalsIgnoreCase("-size")) {
				Param.size = Integer.parseInt(args[i + 1]);
			} else if (args[i].equalsIgnoreCase("-addr")) {
				Param.endDevice = args[i + 1];
			} else if (args[i].equalsIgnoreCase("-netkey")) {
				Param.nwKey = args[i + 1];
			} else if (args[i].equalsIgnoreCase("-appkey")) {
				Param.appKey = args[i + 1];
			} else if (args[i].equalsIgnoreCase("-sensorType")) {
				Param.sensorType = args[i + 1];
			} else if (args[i].equalsIgnoreCase("-host")) {
				Param.host = args[i + 1];
			} else if (args[i].equalsIgnoreCase("-port")) {
				Param.port = Integer.parseInt(args[i + 1]);
			} else if (args[i].equalsIgnoreCase("-periodicity")) {
				Param.periodicity = Integer.parseInt(args[i + 1]);
				Param.probe_periodicity =  Integer.parseInt(args[i + 1]);
			} else if (args[i].equalsIgnoreCase("-help")) {
				System.out.println("-sensor : number of time driven sensors (only for time driven experiments)");
				System.out.println("-rep : number of repetitions");
				System.out.println("-h : broker address");
				System.out.println("-t : mqtt topic");
				System.out.println("-time : time of experiments (in seconds)");
				System.out.println("-time_b : time between experiments");
				System.out.println("-exp : name of experiment");
				System.out.println("-orion : orion address");
				System.out.println("-netkey : network key of LoRa (only for LoRa)");
				System.out.println("-appKey : application key of LoRa (only for LoRa)");
				System.out.println("-sensorType : [ udp | ul | ed-lora | lora | ed-app-lora | app-lora | reasoner | http ]");
				System.out.println("-exp : name of experiment");
				System.out.println("-event : event driven sensor");
				System.out.println("-temp : time driven sensor");
				System.out.println("-devAddress : LoRa device address (only for LoRa)");
				System.out.println("-probe_periodicity : periodicity of messages send by probe - default 600000 ms (10 min)(only for UL)");
				System.out.println("-port : UDP port (only UDP)");
				System.out.println("-host : UDP host (only UDP)");
				System.out.println("-periodicity : UDP sensor periodicity (only UDP)");
				System.exit(0);
			} else if (args[i].equalsIgnoreCase("-event")) {
				Param.eventDriven = true;
			} else if (args[i].equalsIgnoreCase("-temp")) {
				Param.eventDriven = false;
			} else if (args[i].equalsIgnoreCase("-devAddress")) {
				Param.endDevice = args[i + 1];
			} else if(args[i].equalsIgnoreCase("-probe_periodicity")) {
				Param.probe_periodicity = Integer.parseInt(args[i + 1]);
			}

			// } else if (args[i].equalsIgnoreCase("-blue")) {
			// blue = Integer.parseInt(args[i + 1]);
			// } else if (args[i].equalsIgnoreCase("-red")) {
			// red = Integer.parseInt(args[i + 1]);
			// } else if (args[i].equalsIgnoreCase("-green")) {
			// green = Integer.parseInt(args[i + 1]);
			// } else if (args[i].equalsIgnoreCase("-waste")) {
			// waste = Integer.parseInt(args[i + 1]);
			// }
		}
		
		Param.number_of_devices = 50000;
		Param.time_between_exp = 3;
		Param.time_of_experiment = 300;
		Param.time_between_exp = 5;
		Param.number_of_replications = 1;
		Param.lambda = 10;
		Param.address = "localhost:1883";
		Param.sensorType = "http";
		Param.eventDriven = false;
		Param.orionAddress = "172.17.139.217:1026";
		
		// if (Param.number_of_devices > 0) {
		// System.out.println("REASONER - Starting experiment with: " +
		// Param.number_of_devices + " sensors, "
		// + Param.number_of_replications + "replications," + Param.address + "
		// address," + Param.topic
		// + " topic");
		// for (int i = 0; i < Param.number_of_replications; i++) {
		// System.out.println("\n REPLICATION " + (i + 1) + " STARTING \n");
		// TDSensor[] tdSensorArray = new TDSensor[1];
		// TDSensor t = new TDSensor();
		// // t.setType("probe_ul");
		// // t.setTopic(Param.topic);
		// t.setType("reasoner");
		// t.setNumberOfDevices(Param.number_of_devices);
		// tdSensorArray[0] = t;
		//
		// // LoRa information setted
		//
		// mqttp.setTimeSensors(tdSensorArray);
		// Thread threadDoPdf = new Thread(mqttp);
		// threadDoPdf.start();
		//
		// while (!TimeControl.isDone()) {
		// try {
		// Thread.sleep(1000);
		// } catch (InterruptedException e1) {
		// // TODO Auto-generated catch block
		// e1.printStackTrace();
		// }
		// }
		// try {
		// TimeControl.setExperimentStarted(false);
		// TimeControl.setTimeControl(true);
		// IdController.resetIds();
		// Thread.sleep(Param.time_between_exp);
		// } catch (InterruptedException e1) {
		// // TODO Auto-generated catch block
		// e1.printStackTrace();
		// }
		// // mqttp.stop();
		// Param.replication++;
		// }

		// if (Param.number_of_devices > 0) {
		// System.out.println("Starting experiment with: " + Param.number_of_devices +
		// "sensors, " + Param.lambda
		// + " lambda," + Param.number_of_replications + " replications," +
		// Param.address + " address,"
		// + Param.topic + " topic");
		// for (int i = 0; i < Param.number_of_replications; i++) {
		// System.out.println("\n REPLICATION " + (i + 1) + " STARTING \n");
		// TDSensor[] tdSensorArray = new TDSensor[1];
		// TDSensor t = new TDSensor();
		// // t.setType("probe_ul");
		// // t.setTopic(Param.topic);
		// t.setType("http");
		// t.setNumberOfDevices(Param.number_of_devices);
		// tdSensorArray[0] = t;
		//
		//
		// // LoRa information setted
		//
		// mqttp.setTimeSensors(tdSensorArray);
		// Thread threadDoPdf = new Thread(mqttp);
		// threadDoPdf.start();
		//
		// while (!TimeControl.isDone()) {
		// try {
		// Thread.sleep(1000);
		// } catch (InterruptedException e1) {
		// // TODO Auto-generated catch block
		// e1.printStackTrace();
		// }
		// }
		// try {
		// TimeControl.setExperimentStarted(false);
		// TimeControl.setTimeControl(true);
		// IdController.resetIds();
		// Thread.sleep(Param.time_between_exp);
		// } catch (InterruptedException e1) {
		// // TODO Auto-generated catch block
		// e1.printStackTrace();
		// }
		// // mqttp.stop();
		// Param.replication++;
		// }
		//
		//

		// if (Param.number_of_devices > 0) {
		// System.out.println("Starting experiment with: " + Param.number_of_devices +
		// "sensors, " + Param.lambda
		// + " lambda," + Param.number_of_replications + " replications," +
		// Param.address + " address,"
		// + Param.topic + " topic");
		// for (int i = 0; i < Param.number_of_replications; i++) {
		// System.out.println("\n REPLICATION " + (i + 1) + " STARTING \n");
		// TDSensor[] tdSensorArray = new TDSensor[1];
		// TDSensor t = new TDSensor();
		// // t.setType("probe_ul");
		// // t.setTopic(Param.topic);
		// t.setType("app-lora");
		// t.setNumberOfDevices(Param.number_of_devices);
		// tdSensorArray[0] = t;
		//
		// // setting LoRa information
		// Lora lora = new Lora();
		// lora.setChannel(Param.channel);
		// lora.setCodeRate(Param.codeRate);
		// lora.setCrcStatus(Param.crcStatus);
		//
		// DataRate dataRate = new DataRate();
		// dataRate.setBandwith(Param.bandwidth);
		// dataRate.setModulation(Param.modulation);
		// dataRate.setBitrate(Param.bitrate);
		//
		// lora.setDatarate(dataRate);
		// lora.setFrequency(Param.frequency);
		// lora.setLoraSNR(Param.loraSNR);
		// lora.setMac(Param.mac);
		// lora.setRfChain(Param.rfChain);
		// lora.setRssi(Param.rssi);
		// lora.setSize(Param.size);
		//
		// MqttPublish.setLora(lora);
		// // LoRa information setted
		//
		// mqttp.setTimeSensors(tdSensorArray);
		// Thread threadDoPdf = new Thread(mqttp);
		// threadDoPdf.start();
		//
		// while (!TimeControl.isDone()) {
		// try {
		// Thread.sleep(1000);
		// } catch (InterruptedException e1) {
		// // TODO Auto-generated catch block
		// e1.printStackTrace();
		// }
		// }
		// try {
		// TimeControl.setExperimentStarted(false);
		// TimeControl.setTimeControl(true);
		// IdController.resetIds();
		// Thread.sleep(Param.time_between_exp);
		// } catch (InterruptedException e1) {
		// // TODO Auto-generated catch block
		// e1.printStackTrace();
		// }
		// // mqttp.stop();
		// Param.replication++;
		// }

		if (Param.number_of_devices > 0) {

			for (int i = 0; i < Param.number_of_replications; i++) {
				System.out.println("\n REPLICATION " + (i + 1) + " STARTING \n");
				if (Param.eventDriven) {
					EDSensor[] edSensorArray = new EDSensor[1];
					EDSensor e = new EDSensor();

					if (Param.sensorType.equals("ul")) {
						e.setType("ul");
					} else {
						// setting LoRa information
						Lora lora = new Lora();
						lora.setChannel(Param.channel);
						lora.setCodeRate(Param.codeRate);
						lora.setCrcStatus(Param.crcStatus);

						DataRate dataRate = new DataRate();
						dataRate.setBandwith(Param.bandwidth);
						dataRate.setModulation(Param.modulation);
						dataRate.setBitrate(Param.bitrate);

						lora.setDatarate(dataRate);
						lora.setFrequency(Param.frequency);
						lora.setLoraSNR(Param.loraSNR);
						lora.setMac(Param.mac);
						lora.setRfChain(Param.rfChain);
						lora.setRssi(Param.rssi);
						lora.setSize(Param.size);

						MqttPublish.setLora(lora);
						// LoRa information setted

						if (Param.sensorType.equals("ed-lora"))
							e.setType("ed-lora");
						else if (Param.sensorType.equals("udp"))
							e.setType("udp");
						else
							e.setType("ed-app-lora");
					}
					e.setLambda(Param.lambda);// 133

					edSensorArray[0] = e;
					e = new EDSensor();
					mqttp.setEventSensors(edSensorArray);

					Thread threadDoPdf = new Thread(mqttp);
					threadDoPdf.start();
				} else {

					TDSensor[] tdSensorArray = new TDSensor[1];
					TDSensor t = new TDSensor();
					t.setType(Param.sensorType);

					if (Param.sensorType.equals("lora") || Param.sensorType.equals("udp") || Param.sensorType.equals("app-lora")) {
						t.setNumberOfDevices(Param.number_of_devices);
						tdSensorArray[0] = t;
						// setting LoRa information
						Lora lora = new Lora();
						lora.setChannel(Param.channel);
						lora.setCodeRate(Param.codeRate);
						lora.setCrcStatus(Param.crcStatus);

						DataRate dataRate = new DataRate();
						dataRate.setBandwith(Param.bandwidth);
						dataRate.setModulation(Param.modulation);
						dataRate.setBitrate(Param.bitrate);

						lora.setDatarate(dataRate);
						lora.setFrequency(Param.frequency);
						lora.setLoraSNR(Param.loraSNR);
						lora.setMac(Param.mac);
						lora.setRfChain(Param.rfChain);
						lora.setRssi(Param.rssi);
						lora.setSize(Param.size);

						MqttPublish.setLora(lora);
						// LoRa information setted

						mqttp.setTimeSensors(tdSensorArray);

					} else {
						t.setTopic(Param.topic);
						// t.setType("reasoner");
						t.setNumberOfDevices(Param.number_of_devices);
						tdSensorArray[0] = t;
						mqttp.setTimeSensors(tdSensorArray);
					}
					Thread threadDoPdf = new Thread(mqttp);
					threadDoPdf.start();

				}

				while (!TimeControl.isDone()) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				try {
					TimeControl.setExperimentStarted(false);
					TimeControl.setTimeControl(true);
					IdController.resetIds();
					Thread.sleep(Param.time_between_exp);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				// mqttp.stop();
				Param.replication++;
			}
		}

		// if (Param.number_of_devices > 0 || Param.lambda > 0) {
		// System.out.println("Starting experiment with: " + Param.number_of_devices +
		// "sensors, " + Param.lambda
		// + " lambda," + Param.number_of_replications + " replications," +
		// Param.address + " address,"
		// + Param.topic + " topic");
		// for (int i = 0; i < Param.number_of_replications; i++) {
		//
		// System.out.println("\n REPLICATION " + (i + 1) + " STARTING \n");
		// EDSensor[] edSensorArray = new EDSensor[1];
		// if (Param.number_of_devices > 0) {
		//
		// int number_of_tds = 0;
		// if (blue > 0)
		// number_of_tds++;
		// if (green > 0)
		// number_of_tds++;
		// if (red > 0)
		// number_of_tds++;
		// if (waste > 0)
		// number_of_tds++;
		//
		// TDSensor[] tdSensorArray = new TDSensor[number_of_tds];
		// TDSensor t = new TDSensor();
		//
		// String[] data = new String[1];
		// data[0] = "booleanText";
		//
		// int ind = 0;
		//
		// if (blue > 0) {
		// t.setType("blue");
		// t.setPeriodicity(blue);
		// t.setData(data);
		// t.setMax(new String[] { "blue_line" });
		// t.setMin(new String[] { "blue_line" });
		// t.setTopic(Param.topic);
		// t.setNumberOfDevices(1);
		// tdSensorArray[ind++] = t;
		// }
		// if (green > 0) {
		// t = new TDSensor();
		// t.setType("green");
		// t.setData(data);
		// t.setMax(new String[] { "green_line" });
		// t.setMin(new String[] { "green_line" });
		// t.setPeriodicity(green);
		// t.setTopic(Param.topic);
		// t.setNumberOfDevices(1);
		// tdSensorArray[ind++] = t;
		// }
		// if (red > 0) {
		// t = new TDSensor();
		// t.setType("red");
		// t.setData(data);
		// t.setMax(new String[] { "red_line" });
		// t.setMin(new String[] { "red_line" });
		// t.setPeriodicity(red);
		// t.setTopic(Param.topic);
		// t.setNumberOfDevices(1);
		// tdSensorArray[ind++] = t;
		// }
		// if (waste > 0) {
		// t = new TDSensor();
		// t.setType("waste");
		// t.setTopic(Param.topic);
		// t.setNumberOfDevices(waste);
		// tdSensorArray[ind++] = t;
		// }
		// mqttp.setTimeSensors(tdSensorArray);
		// }
		// if (Param.lambda > 0) {
		// EDSensor e = new EDSensor();
		// e.setType("motionDetector");
		// e.setMode("continuously");
		// // e.setType("lightController");
		// e.setTopic(Param.topic); // TesteEvento1
		// // System.out.println("for:" + edSensorArray[ind]);
		// e.setLambda(Param.lambda);// 133
		// edSensorArray[0] = e;
		// e = new EDSensor();
		// mqttp.setEventSensors(edSensorArray);
		// }
		// Thread threadDoPdf = new Thread(mqttp);
		// threadDoPdf.start();
		// TimeControl.startTime();
		// while (!TimeControl.isDone()) {
		// try {
		// Thread.sleep(250);
		// } catch (InterruptedException e1) {
		// // TODO Auto-generated catch block
		// e1.printStackTrace();
		// }
		// }
		//
		// System.out.println("\n\n*******************************************
		// REPLICATION " + Param.replication
		// + " ENDED *******************************************\n\n");
		// Param.replication++;
		// try {
		// Thread.sleep(1000 * Param.time_between_exp);
		// } catch (InterruptedException e1) {
		// // TODO Auto-generated catch block
		// e1.printStackTrace();
		// }
		// // mqttp.stop();
		// }
		else {
			System.out.println("invalid parameters");
		}
	}
}
