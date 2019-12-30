package org.cdn.anonchat.model;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import io.javalin.websocket.WsConnectContext;
import io.javalin.websocket.WsContext;

public class User {
	
	public String username;
	public String room;
	public WsContext ctx;

	public User(String _room, WsContext _ctx) {
		this.username = this.createUsername();
		this.ctx = _ctx;
		this.room = _room;
	}
	
	private String createUsername() {
		
		HttpClient client = HttpClient.newHttpClient(); 
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create("http://names.drycodes.com/1"))
				.build();
		try {
			
			HttpResponse<String> resp = client.send(request, HttpResponse.BodyHandlers.ofString());
			String r = resp.body();
			
			r = r.substring(r.indexOf("\"")+1);
			r = r.substring(0, r.indexOf("\""));
			
			return r;
			
		} catch (IOException e) {
			e.printStackTrace();
			return "white_error";
		} catch (InterruptedException e) {
			e.printStackTrace();
			return "black_error";
		}
		
		
	}
	
}
