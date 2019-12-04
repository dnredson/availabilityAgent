/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.sense.code;

public class Param {
	// Metricas
	public static int probe = 0;
	public static String topic = "sensors";// "/4jggokgpepnvsb2uv4s40d59oh"; //v para outro experimentos
	public static int replication = 1;
	public static int uid = 1;
	public static String experiment_num = "experiment";
	public static String name_experiment = "SenSE_logFile";
	public static int number_of_experiments = 8;
	public static String path = "/home/ivan/";
	public static int number_of_replications = 1;
	public static int time_between_exp = 60;
	public static double lambda = 5;
	public static int number_of_topics = 1;
	public static int number_of_devices_Event = 5;
	public static int qos = 0; // 0 - 1 - 2
	public static long time_of_experiment = 300;// 5min?
	public static boolean writeFile = true;
	public static int eventDuration = 200;// evento 2/3 metade do exp
	// public static final String address = "tcp://localhost:1884";
	public static String address ="tcp://localhost:1883"; //"tcp://177.104.61.26:1883";// "tcp://172.31.95.127";//"tcp://172.17.132.211:1883";//"tcp://172.17.132.195:1883";"tcp://172.17.132.195:1883";
	public static int number_of_devices = 0;

	public static int probe_periodicity = 10 * 1000 * 60;
	
	// LoRa features
	public static int channel = 0;
	public static String codeRate = "4/5";
	public static int crcStatus = 1;
	public static int bandwidth = 125;
	public static String modulation = "LORA";
	public static int bitrate = 0;
	public static int frequency = 915000000;
	public static int loraSNR = 7;
	public static String mac = "1876585532657073";//"0102030405060708";// application/1/device/0102030405060708/rx
	public static int rfChain = 1;
	public static int rssi = -57;
	public static int size = 23;

	public static String orionAddress = "177.104.61.26";//"172.17.132.214:3001";//"localhost:1026"; //"177.104.61.20:1026";
	public static int counter = 1;
	public static String endDevice = "00fb0bc1";//"01ff606d"; //"01304300";
	public static String appKey = "b73485bb9c5e29a2c8b6a330f0bf2ed3";//"eafcfa5ce3f401d6d7f5b492fb38ca75";//"7e4a82ed1595833408ac24eaac28861e";
	public static String nwKey = "9c698235533b8865900aee3558dfc47b";//"4877be6af6d8ca137d67e5b226e70a4c";
	public static String sensorType = "ed-lora";
	public static Boolean eventDriven = false; 
	
	public static String host = "172.17.132.161";
	public static int port = 1777;
	public static int periodicity = 600000;
}