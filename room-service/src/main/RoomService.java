package main;

import communication.HTTPServer;
import communication.MQTTAgent;
import controller.Controller;
import io.vertx.core.Vertx;
import json.JsonManager;
import logic.Logic;

public class RoomService {
	
	/**
	 * baud rate of the communication
	 */
	private static final int BAUD = 9600;
	
	/**
	 * Launch program.
	 * 
	 * @param args one parameter, name of the port for the serial communication 
	 * @throws Exception if port incorrect
	 */
	public static void main(String[] args) throws Exception {
		final String port = args[0];
		JsonManager jm = new JsonManager();
		Logic logic = new Logic();
		Vertx vertx = Vertx.vertx();
		Controller controller = new Controller(port, BAUD, logic, jm);
		MQTTAgent agent = new MQTTAgent(controller);
		vertx.deployVerticle(agent);
		System.out.println("MQTT Service started");
		new HTTPServer(controller);
		System.out.println("HTTP Server started");
		System.out.println("Room Service started");
	}

}

