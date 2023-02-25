package main;
import communication.CommChannel;
import communication.RoomControllerComm;
import communication.SerialCommChannel;
import controller.Controller;

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
		
		CommChannel comm = new SerialCommChannel(port, BAUD);
		System.out.println("Waiting Arduino for rebooting...");
		Thread.sleep(5000);
		System.out.println("Ready.");
		
		Controller controller = new Controller(new RoomControllerComm(comm));
		controller.start();
	}

}
