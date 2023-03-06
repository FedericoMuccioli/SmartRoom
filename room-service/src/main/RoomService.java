package main;
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
	//private static final String PORT = "/dev/tty.usbmodem11401";
	//private static final String PORT = "/dev/cu.usbmodem11401";
	
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
	}

}

