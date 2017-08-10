package edu.kit.annotation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpPostClient {

	public static void main(String[] args) {
		getPost("digitalObjectID","xmlString");
	}

	private static void getPost(String string, String string2) {
		
		 try {

				URL url = new URL("http://localhost:8080/AnnotationWADM_v1.0/rest/jsonld?digitalObjID=tempName123");
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setDoOutput(true);
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Content-Type", "application/xml");

				String input = "<hi>vaibhav</hi>";

				OutputStream outputStream = conn.getOutputStream();
				outputStream.write(input.getBytes());
				outputStream.flush();

				if (conn.getResponseCode() != 200) {
					throw new RuntimeException("Failed : HTTP error code : "
						+ conn.getResponseCode());
				}

				BufferedReader br = new BufferedReader(new InputStreamReader(
						(conn.getInputStream())));

				String output;
				System.out.println("Output from Server .... \n");
				while ((output = br.readLine()) != null) {
					System.out.println(output);
				}

				conn.disconnect();

			  } catch (MalformedURLException e) {

				e.printStackTrace();

			  } catch (IOException e) {

				e.printStackTrace();

			 }

		
	}
}
