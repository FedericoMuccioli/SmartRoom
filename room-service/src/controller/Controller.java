package controller;

import Json.JsonManager;
import communication.RoomCommChannel;
import logic.logic;

public class Controller {

	private final RoomCommChannel roomContrComm;
	private final logic logic;
	private final JsonManager jm;

	public Controller(final String port, final int rate, final logic logic, final JsonManager jm) throws Exception {
		this.roomContrComm = new RoomCommChannel(port, rate, this);
		this.logic = logic;
		this.jm = jm;
	}

	public void updateRoom(String movement, int luminosity) {
		String[] values = logic.UpdateLogic(movement, luminosity);
		if(values[0] != "null") {
			roomContrComm.setLight(values[0] == "ON");
		}
		if(values[1] != "null") {
			roomContrComm.setRollerBlinds(Integer.parseInt(values[1]));
		}
	}
	
	public void notifyChangeRoom(final int light, final int rollerBlinds) {
		//DA CANCELLARE
		System.out.println("light" + light);
		System.out.println("rollerBlinds" + rollerBlinds);
		jm.UpdateJSON(light, rollerBlinds);
	}


}
