package br.sense.code;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import com.google.gson.*;
import java.lang.Thread;
import br.sense.model.Entity;
import br.sense.model.TimestampUpdate;

public class JavaPostRequest {
	public static void sendReasonerPost(final ReasonerMessage message) throws IOException {
		new Thread() {
			@Override
			public void run() {
				System.out.println("SEND MESSAGE TO REASONER");
				String postUrl = "http://" + Param.orionAddress + "/message"; // "https://ptsv2.com/t/95p62-1547585334/post";//
				// put in your url
				message.setTimestamp();
				Gson gson = new Gson();
				HttpClient httpClient = HttpClientBuilder.create().build();
				HttpPost post = new HttpPost(postUrl);
				StringEntity postingString;
				try {
					postingString = new StringEntity(gson.toJson(message));
					post.setEntity(postingString);
					post.setHeader("Content-type", "application/json");
				} catch (UnsupportedEncodingException e) {
					System.out.println("---ERROR1 SENDING MESSAGE TO REASONER ---");
					e.printStackTrace();
				} // gson.tojson() converts your pojo
				HttpResponse response; // to json
				try {
					response = httpClient.execute(post);
					System.out.println("sent: " + gson.toJson(message));
					System.out.println(String.valueOf(response));

				} catch (IOException e) {
					System.out.println("--- CONNECTION REFUSED ---");
					File arquivo = new File(Param.name_experiment + "ConRef.csv");
					try (FileWriter fw = new FileWriter(arquivo, true); BufferedWriter bw = new BufferedWriter(fw)) {
						System.out.println("WRITING FILE...");
						bw.write("CONNECTION REFUSED;" + Param.replication+";"+(System.currentTimeMillis()));
						bw.newLine();
					} catch (IOException e1) {
						System.out.println("--- ERROR WRITING FILE OF CONNECTION REFUSED ---");
						e1.printStackTrace();
					}
					e.printStackTrace();
				}

			}
		}.start();
	}

	public static void createEntity(final Entity entity) throws IOException {
		new Thread() {
			@Override
			public void run() {
				System.out.println("CREATE ENTITY");
				System.out.println("Entity ID:" + entity.getId());
				String postUrl = "http://" + Param.orionAddress + "/v2/entities"; // "https://ptsv2.com/t/95p62-1547585334/post";//
				// put in your url
				Gson gson = new Gson();
				HttpClient httpClient = HttpClientBuilder.create().build();
				HttpPost post = new HttpPost(postUrl);
				StringEntity postingString;
				try {
					postingString = new StringEntity(gson.toJson(entity));
					post.setEntity(postingString);
					post.setHeader("Content-type", "application/json");
				} catch (UnsupportedEncodingException e) {
					System.out.println("---ERROR1 CREATING ENTITY---");
					e.printStackTrace();
				} // gson.tojson() converts your pojo
				HttpResponse response; // to json
				try {
					response = httpClient.execute(post);
					System.out.println("sent: " + gson.toJson(entity));
					System.out.println(String.valueOf(response));

				} catch (IOException e) {
					System.out.println("---CONNECTION REFUSED---");
					File arquivo = new File(Param.name_experiment + "ConRef.csv");
					try (FileWriter fw = new FileWriter(arquivo, true); BufferedWriter bw = new BufferedWriter(fw)) {
						System.out.println("WRITING FILE...");
						bw.write("CONNECTION REFUSED;" + Param.replication+";"+(System.currentTimeMillis()));
						bw.newLine();
					} catch (IOException e1) {
						System.out.println("---ERROR WRITING FILE OF CONNECTION REFUSED---");
						e1.printStackTrace();
					}
					e.printStackTrace();
				}

			}
		}.start();
	}

	public static void updateEntity(final Entity entity) throws IOException {
		new Thread() {
			@Override
			public void run() {

				System.out.println("UPDATE ENTITY");
				String postUrl = "http://" + Param.orionAddress + "/v2/entities/" + entity.getId() + "/attrs"; // "https://ptsv2.com/t/95p62-1547585334/post";//

				Gson gson = new Gson();
				HttpClient httpClient = HttpClientBuilder.create().build();
				HttpPost post = new HttpPost(postUrl);
				TimestampUpdate timestampUpdate = new TimestampUpdate();
				StringEntity postingString;
				try {
					postingString = new StringEntity(gson.toJson((timestampUpdate)));
					post.setEntity(postingString);
				} catch (UnsupportedEncodingException e) {
					System.out.println("---ERROR1 UPDATING ENTITY---");
					e.printStackTrace();
				} // gson.tojson() converts
					// StringEntity postingString = new
					// StringEntity("{\"timestamp\":"+gson.toJson((entity.getTimestamp())) );//
					// gson.tojson() converts your pojo to json
				post.setHeader("Content-type", "application/json");
				HttpResponse response;
				try {
					response = httpClient.execute(post);
					System.out.println("sent: " + gson.toJson(entity));
					System.out.println(String.valueOf(response));

				} catch (IOException e) {
					System.out.println("---CONNECTION REFUSED---");
					File arquivo = new File(Param.name_experiment + "ConRef.csv");
					try (FileWriter fw = new FileWriter(arquivo, true); BufferedWriter bw = new BufferedWriter(fw)) {
						System.out.println("WRITING FILE...");
						bw.write("CONNECTION REFUSED;" + Param.replication+";"+(System.currentTimeMillis()));
						bw.newLine();
					} catch (IOException e1) {
						System.out.println("---ERROR WRITING FILE OF CONNECTION REFUSED---");
						e1.printStackTrace();
					}
					e.printStackTrace();
				}

			}
		}.start();
	}
}

// import java.io.BufferedReader;
// import java.io.DataOutputStream;
// import java.io.IOException;
// import java.io.InputStreamReader;
// import java.net.HttpURLConnection;
// import java.net.MalformedURLException;
// import java.net.ProtocolException;
// import java.net.URL;
// import java.nio.charset.StandardCharsets;
//
// public class JavaPostRequest {
//
// private static HttpURLConnection con;
//
// public static void sendHttpPost() throws MalformedURLException,
// ProtocolException, IOException {
//
// String url = "https://ptsv2.com/t/95p62-1547585334/post";
// //String url = "http://" + Param.orionAddress + "/v2/entities";
//
// String urlParameters = "id=Room1&type=Room&temperature=10";
// byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);
// try {
//
// URL myurl = new URL(url);
// con = (HttpURLConnection) myurl.openConnection();
//
// con.setDoOutput(true);
// con.setRequestMethod("POST");
// // con.setRequestProperty("User-Agent", "Java client");
// con.setRequestProperty("Content-Type", "application/json");
//
// try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
// wr.write(postData);
// }
//
// StringBuilder content;
//
// try (BufferedReader in = new BufferedReader(new
// InputStreamReader(con.getInputStream()))) {
//
// String line;
// content = new StringBuilder();
//
// while ((line = in.readLine()) != null) {
// content.append(line);
// content.append(System.lineSeparator());
// }
// }
//
// System.out.println(content.toString());
//
// } finally {
//
// con.disconnect();
// }
// }
// }