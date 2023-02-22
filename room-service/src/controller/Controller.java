package controller;

import communication.RoomControllerComm;

public class Controller {
	
	private final RoomControllerComm roomContrComm;

	public Controller(final RoomControllerComm roomContrComm) {
		this.roomContrComm = roomContrComm;
	}
	
	public void start() throws InterruptedException { //cancellare throw
		while(true) {
			Thread.sleep(2000);
			roomContrComm.setLight(true);
			Thread.sleep(100);
			roomContrComm.setRollerBlinds(180);
			Thread.sleep(2000);
			roomContrComm.setLight(false);
			Thread.sleep(100);
			roomContrComm.setRollerBlinds(0);
		}
	}
	
}
