package org.cdn.anonchat.controllers;

import io.javalin.http.Context;
import io.javalin.websocket.WsContext;
import io.javalin.websocket.WsMessageContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.cdn.anonchat.model.User;

public class ChatController {
	
	private static Map<WsContext, User> usersConnected = new ConcurrentHashMap<>();
	
	public static void home(Context ctx) {
		ctx.render("public/index.html");
	}
	
	public static void login(Context ctx) {
		String chatroom = ctx.formParam("chatroom");
		ctx.redirect("/chat/" + chatroom);
	}
	
	public static void chat(Context ctx) {
		ctx.render("public/chat.html");
	}
	
	public static void connect(WsContext ctx) {
		String room = ctx.pathParam("room");
		User user = new User(room,ctx);
		usersConnected.put(ctx,user);
		refreshUserList(user);
	}
	
	public static void disconnect(WsContext ctx) {
		User userDisconnected = usersConnected.get(ctx);
		usersConnected.remove(ctx);
		refreshUserList(userDisconnected);
	}
	
	public static void message(WsMessageContext ctx) {
		User sender = usersConnected.get(ctx);
		String message = ctx.message();
		sendTextMessage(sender,message);
	}
	
	private static void refreshUserList(User sender) {
		
		List<String> usersList = usersConnected.values().stream()
				.filter(x -> sender.room.equals(x.room))
				.map(x -> x.username)
				.collect(Collectors.toList());
		
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("msgType", 1);
		obj.put("userList",usersList);
		
		usersConnected.values().stream()
			.filter(x -> sender.room.equals(x.room) && x.ctx.session.isOpen())
			.forEach(x -> x.ctx.send(obj));
		
	}
	
	private static void sendTextMessage(User sender, String message) {
		
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("msgType", 2);
		obj.put("userSender",sender.username);
		obj.put("userMessage", message);
		
		usersConnected.values().stream()
			.filter(x -> sender.room.equals(x.room) && x.ctx.session.isOpen())
			.forEach(x -> x.ctx.send(obj));
		
	}
	
}
