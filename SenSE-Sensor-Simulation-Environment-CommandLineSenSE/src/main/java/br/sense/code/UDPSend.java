package br.sense.code;

import java.io.*;
import java.net.*;
import java.util.Base64;

import br.sense.model.Lora;

public class UDPSend {
	private int port = Param.port;
	private String host;
	private DatagramSocket dsocket;
	private InetAddress address;
	private byte[] message;
	private Lora lora;

	public UDPSend(String host, int port, Lora lora) {
		try {
			this.host = host;
			this.lora = lora;
			// Get the internet address of the specified host
			this.address = InetAddress.getByName(host);
			// Create a datagram socket, send the packet through it, close it.
			this.dsocket = new DatagramSocket();
		} catch (Exception e) {
			System.out.println("CANNOT CONNECTO TO UDP HOST");
			System.err.println(e);
		}
	}

	public Lora getLora() {
		return lora;
	}

	public void setLora(Lora lora) {
		this.lora = lora;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
		try {
			this.address = InetAddress.getByName(host);
		} catch (UnknownHostException e) {
			System.out.println("CANNOT CONNECT TO UDP HOST");
			e.printStackTrace();
		}
	}

	public DatagramSocket getDsocket() {
		return dsocket;
	}

	public void setDsocket(DatagramSocket dsocket) {
		this.dsocket = dsocket;
	}

	public InetAddress getAddress() {
		return address;
	}

	public void setAddress(InetAddress address) {
		this.address = address;
	}

	public byte[] getMessage() {
		return message;
	}

	public void setMessage(byte[] message) {
		this.message = message;
	}
	public void UDPconnect() {
		try {
			// Get the internet address of the specified host
			this.address = InetAddress.getByName(host);
			// Create a datagram socket, send the packet through it, close it.
			this.dsocket = new DatagramSocket();
		} catch (Exception e) {
			System.out.println("CANNOT CONNECTO TO UDP HOST");
			System.err.println(e);
		}
	}

	public void SendUDPMessage(String msg) {
		try {
			//Base64 base64 = new Base64();
			String loramsg = (lora.generatePhyPayload(System.currentTimeMillis()));
			System.out.println("PHYPayload:" +  loramsg);

			byte[] decodedBytes = Base64.getDecoder().decode(loramsg);
			byte[] sixBytes = hexStringToByteArray("1d1d1d1d1d1d");		
			byte payloadSize = (byte)decodedBytes.length;
			byte[] message = new byte[7 + decodedBytes.length];
			int i;
			for(i =0; i<sixBytes.length;i++) {
				message[i] = sixBytes[i];
			}
			message[i++] = payloadSize;
			for(int j=0; j < decodedBytes.length;i++) {
				message[i] = decodedBytes[j++];
			}
			System.out.print("Binary message sent: ");
			for(int j=0; j < message.length;j++) {
				System.out.print(message[j] + " ");
			}

			DatagramPacket packet = new DatagramPacket(message, message.length, address, port);
			System.out.println("SENDING UDP MESSAGE: "+ message + " TO " + address + ":"+port);

			dsocket.send(packet);
		} catch (Exception e) {
			System.out.println("ERROR SENDING UDP MESSAGE");
			System.err.println(e);
		}
	}
	public void CloseUDPconnection() {
		dsocket.close();
	}
	public static byte[] hexStringToByteArray(String s) {
	    int len = s.length();
	    byte[] data = new byte[len / 2];
	    for (int i = 0; i < len; i += 2) {
	        data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
	                             + Character.digit(s.charAt(i+1), 16));
	    }
	    return data;
	}
	public static final byte[] intToByteArray(int value) {
	    return new byte[] {
	            (byte)(value >>> 24),
	            (byte)(value >>> 16),
	            (byte)(value >>> 8),
	            (byte)value};
	}
}
