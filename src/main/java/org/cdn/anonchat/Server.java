package org.cdn.anonchat;

import org.cdn.anonchat.controllers.ChatController;

import io.javalin.Javalin;

public class Server {

	public static int PORT = Integer.parseInt(System.getenv("PORT"),8000);
	
	public static void main(String[] args) {
		
		Javalin app = Javalin.create(config -> {
				config.addStaticFiles("/public");
			})
			.start(PORT);
		
		app.get("/", ChatController::home);
		
		app.post("/login", ChatController::login);
		
		app.get("/chat/:room", ChatController::chat);
		
		app.ws("/chatsocket/:room", ws ->{
			ws.onConnect(ChatController::connect);
			ws.onClose(ChatController::disconnect);
			ws.onMessage(ChatController::message);
		});
		
		app.get("/ping", ctx -> { 
			ctx.result("Pong!");
			ctx.status(200);
		});

	}

}
