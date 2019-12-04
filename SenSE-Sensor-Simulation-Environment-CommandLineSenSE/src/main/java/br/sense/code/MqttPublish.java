package br.sense.code;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import br.sense.model.*;

public class MqttPublish implements Runnable {
	public volatile boolean exit = false;
	private final Random random = new Random();
	private static TDSensor[] timeSensors = null;
	private static EDSensor[] eventSensors = null;
	private static ArrayList<Thread> threads = new ArrayList<Thread>();
	private static Lora lora;

	// TODO permitir instanciar até x sensores genéricos
	// setar max e min para cada valor de dado de sensor
	public MqttPublish() {
	}

	public MqttPublish(TDSensor[] timeSensors, EDSensor[] eventSensors) {
		// System.out.println("construtor");
		// this.timeSensors = timeSensors;
		// this.eventSensors = eventSensors;
	}

	public MqttPublish(EDSensor[] eventSensors) {
		// this.eventSensors = eventSensors;
	}

	public MqttPublish(TDSensor[] timeSensors) {
		// this.timeSensors = timeSensors;
	}

	@Override
	public void run() {
		// while (!exit)
		publish();
		// System.out.println("STOP!");

	}

	public static Lora getLora() {
		return lora;
	}

	public static void setLora(Lora lora) {
		MqttPublish.lora = lora;
	}

	private static void setTemplateTimeDrivenSensor(String sensorType, String topic, int number_of_sensors,
			CountDownLatch l) {
		TimeDrivenSensor newSensor;
		newSensor = new TimeDrivenSensor(sensorType, number_of_sensors, topic, l);
		Thread thread = new Thread(newSensor);
		threads.add(thread);
		thread.start();
	}

	private static void setLoraTimeDrivenSensor(Lora lora, String sensorType, String topic, int number_of_sensors,
			CountDownLatch l) {
		TimeDrivenSensor newSensor;
		if (sensorType.equals("lora"))
			newSensor = new TimeDrivenSensor(lora, "gateway/" + lora.getMac() + "/rx", sensorType, number_of_sensors,
					l);
		else
			newSensor = new TimeDrivenSensor(lora, "application/1/device/0102030405060708/rx", sensorType,
					number_of_sensors, l);

		Thread thread = new Thread(newSensor);
		threads.add(thread);
		thread.start();
	}

	private static void setLoraEventDrivenSensor(Lora lora, String sensorType, String topic, double lambda,
			CountDownLatch l) {
		System.out.println("--- SETTING EVENT-DRIVEN LORA SENSOR ---");
		EventDrivenSensor newSensor;
		if (sensorType.equals("lora"))
			newSensor = new EventDrivenSensor("gateway/" + lora.getMac() + "/rx", sensorType, lambda, l, lora);
		else
			newSensor = new EventDrivenSensor("application/1/device/0102030405060708/rx", sensorType, lambda, l, lora);

		Thread thread = new Thread(newSensor);
		threads.add(thread);
		thread.start();
	}

	private static void setUlEventDrivenSensor(String sensorType, String topic, double lambda, CountDownLatch l) {
		System.out.println("--- SETTING EVENT-DRIVEN UL SENSOR ---");
		EventDrivenSensor newSensor;
		newSensor = new EventDrivenSensor(Param.topic, "ul", lambda, l);
		Thread thread = new Thread(newSensor);
		threads.add(thread);
		thread.start();
	}

	private static void setProbeSensor(String topic, int number_of_sensors, CountDownLatch l) {
		TimeDrivenSensor newSensor;
		newSensor = new TimeDrivenSensor("probe_t", number_of_sensors, topic, l);
		Thread thread = new Thread(newSensor);
		threads.add(thread);

		newSensor = new TimeDrivenSensor("probe_h", number_of_sensors, topic, l);
		thread = new Thread(newSensor);
		threads.add(thread);

		newSensor = new TimeDrivenSensor("probe_c", number_of_sensors, topic, l);
		thread = new Thread(newSensor);
		threads.add(thread);
		for (int i = 0; i < threads.size(); i++) {
			threads.get(i).start();
		}
	}

	private void setUID() {
		MqttSubscribe sub = new MqttSubscribe();
		Thread threadSub = new Thread(sub);
		threadSub.start();
	}

	private static void setTimeDrivenDevices(TDSensor temp, CountDownLatch l) {
		switch (temp.getType()) {
		case "air":
			setTemplateTimeDrivenSensor("air", temp.getTopic(), temp.getNumberOfDevices(), l);
			break;
		case "waste":
			setTemplateTimeDrivenSensor("waste", temp.getTopic(), temp.getNumberOfDevices(), l);
			break;
		case "noise":
			setTemplateTimeDrivenSensor("noise", temp.getTopic(), temp.getNumberOfDevices(), l);
			break;
		case "structural":
			setTemplateTimeDrivenSensor("structural", temp.getTopic(), temp.getNumberOfDevices(), l);
			break;
		case "traffic":
			setTemplateTimeDrivenSensor("traffic", temp.getTopic(), temp.getNumberOfDevices(), l);
			break;
		case "probe":
			setProbeSensor(temp.getTopic(), temp.getNumberOfDevices(), l);
			break;
		case "ul":
			setTemplateTimeDrivenSensor("probe_ul", temp.getTopic(), temp.getNumberOfDevices(), l);
			break;
		case "http":
			setTemplateTimeDrivenSensor("http", temp.getTopic(), temp.getNumberOfDevices(), l);
			break;
		case "reasoner":
			setTemplateTimeDrivenSensor("reasoner", temp.getTopic(), temp.getNumberOfDevices(), l);
			break;
		case "lora":
			setLoraTimeDrivenSensor(lora, "lora", temp.getTopic(), temp.getNumberOfDevices(), l);
			break;
		case "udp":
			setLoraTimeDrivenSensor(lora, "udp", temp.getTopic(), temp.getNumberOfDevices(), l);
			break;
		case "app-lora":
			setLoraTimeDrivenSensor(lora, "app-lora", temp.getTopic(), temp.getNumberOfDevices(), l);
			break;
		default:
			setTimeDrivenSensor(temp.getType(), temp.getData(), temp.getPeriodicity(), temp.getNumberOfDevices(),
					temp.getTopic(), temp.getMax(), temp.getMin(), l);
			break;
		}
	}

	private static void setEventDrivenDevices(EDSensor event, CountDownLatch l) {
		if (event.getType().equals("lightController")) {
			Long temp = ((Param.time_of_experiment * 3) / 4);
			setTemplateEventDrivenSensor(event.getType(), event.getTopic(), event.getLambda(), temp.intValue(), l);
		} else if (event.getType().equals("queueExperiment")) {
			Long temp = Param.time_of_experiment - 1;
			setTemplateEventDrivenSensor(event.getType(), event.getTopic(), event.getLambda(), temp.intValue(), l);

		} else if (event.getType().equals("ed-lora")) {
			setLoraEventDrivenSensor(lora, "lora", event.getTopic(), event.getLambda(), l);
		}  else if (event.getType().equals("udp")) {
			setLoraEventDrivenSensor(lora, "udp", event.getTopic(), event.getLambda(), l);
		}else if (event.getType().equals("ul")) {
			setUlEventDrivenSensor("ul", event.getTopic(), event.getLambda(), l);
		} else {
			Long temp = ((Param.time_of_experiment * 3) / 4);
			if (event.getMode().equals("variable")) {
				setEventDrivenSensor(event.getType(), event.getTopic(), event.getData(), event.getLambda(),
						temp.intValue(), event.getMode(), event.getMax(), event.getMin(), l);
			} else {
				setEventDrivenSensor(event.getType(), event.getTopic(), event.getData(), event.getLambda(),
						((int) Param.time_of_experiment - 1), event.getMode(), event.getMax(), event.getMin(), l);
			}

		}
	}

	// Event Driven Sensors, each thread represents a event (ex: a football
	// match)
	// Constructor EventDrivenSensor(String sensorType,int idSensor, double
	// lambda, int duration, latch)
	// DURATION IN MINUTES
	private static void setTemplateEventDrivenSensor(String sensorType, String topic, double lambda, int duration,
			CountDownLatch l) {
		EventDrivenSensor newSensor;
		newSensor = new EventDrivenSensor(sensorType, lambda, duration, topic, l);
		Thread thread = new Thread(newSensor);
		threads.add(thread);
		thread.start();

	}

	private static void setTimeDrivenSensor(String sensorType, String[] messageType, int duration,
			int number_of_sensors, String topic, String[] max, String[] min, CountDownLatch latch) {
		// System.out.println("max: "+max[0]+" min: "+min[0]);
		TimeDrivenSensor newSensor = new TimeDrivenSensor(sensorType, messageType, max, min, duration,
				number_of_sensors, topic, latch);
		Thread thread = new Thread(newSensor);
		threads.add(thread);
		thread.start();
	}

	// private static void setTimeDrivenSensor(String sensorType, String[]
	// messageType, int duration, int number_of_sensors,
	// String topic,CountDownLatch latch) {
	// TimeDrivenSensor newSensor = new TimeDrivenSensor(sensorType, messageType,
	// duration, number_of_sensors, topic,
	// latch);
	// Thread thread = new Thread(newSensor);
	// threads.add(thread);
	// thread.start();
	// }
	private static void setEventDrivenSensor(String sensorType, String topic, String[] messageType, double lambda,
			int duration, String mode, String[] max, String[] min, CountDownLatch latch) {
		EventDrivenSensor newSensor = new EventDrivenSensor(sensorType, messageType, max, min, lambda, mode, duration,
				topic, latch);
		Thread thread = new Thread(newSensor);
		threads.add(thread);
		thread.start();
	}

	public static void publish() {
		// System.out.println(Param.experiment_num + " is going to start in " +
		// Param.time_between_exp + "s");
		RandomController.setSeed();
		TimeControl.setTimeOfExperiment(Param.time_of_experiment);
		TimeControl.startTime();
		CountDownLatch latch = new CountDownLatch(1);
		if (timeSensors != null) {
			TDSensor temp = new TDSensor();
			for (int i = 0; i < timeSensors.length; i++) {
				temp = timeSensors[i];
				setTimeDrivenDevices(temp, latch);
			}
		}
		if (eventSensors != null) {
			EDSensor event = new EDSensor();
			for (int i = 0; i < eventSensors.length; i++) {
				event = eventSensors[i];
				setEventDrivenDevices(event, latch);
			}
		}
		// try {
		// latch.await(); // main thread is waiting on CountDownLatch to finish
		// Thread.sleep(1000);
		// } catch (InterruptedException ie) {
		// }

		System.out.println("All Threads have already been instantiated");

		while (TimeControl.getTime() <= TimeControl.getTimeOfExperiment()) {

			// System.out.println("waiting for experiment to end " + TimeControl.getTime() +
			// ", ends "
			// + TimeControl.getTimeOfExperiment());
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			// IdController.resetIds();
			// MessageArray.clean();
		}
		// System.out.println("RESETANDO EXPERIMENTO");
		MessageArray.clean();
		abort();
	}

	@SuppressWarnings("deprecation")
	public static void abort() {
		// TimeControl.s
		TimeControl.setExperimentStarted(false);
		TimeControl.setTimeControl(true);
		for (Thread thread : threads) {
			IdController.resetIds();
			thread.stop();
		}
	}

	// public void stop() {
	// TimeControl.setExperimentStarted(false);
	// TimeControl.setTimeControl(true);
	// for (Thread thread : threads) {
	// IdController.resetIds();
	// thread.stop();
	// }
	// exit = true;
	// }

	public static TDSensor[] getTimeSensors() {
		return timeSensors;
	}

	public static void setTimeSensors(TDSensor[] timeSensors) {
		MqttPublish.timeSensors = timeSensors;
	}

	public static EDSensor[] getEventSensors() {
		return eventSensors;
	}

	public static void setEventSensors(EDSensor[] eventSensors) {
		MqttPublish.eventSensors = eventSensors;
	}

}
