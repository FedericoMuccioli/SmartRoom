package controller;

import Json.JsonManager;
import communication.RoomControllerComm;
import logic.logic;

public class Controller {

	private final RoomControllerComm roomContrComm;
	private final logic logic;
	private final JsonManager jm;

	public Controller(final RoomControllerComm roomContrComm, final logic logic, final JsonManager jm) {
		this.roomContrComm = roomContrComm;
		this.logic = logic;
		this.jm = jm;
	}

	public void updateRoom(String movement, int luminosity) {
		String[] values = logic.UpdateLogic(movement, luminosity);
		if(values[0] != "null") {
			roomContrComm.setLight(values[0] == "ON" ? true : false);
		}
		if(values[1] != "null") {
			roomContrComm.setRollerBlinds(Integer.parseInt(values[1]));
		}
	}

	public void notifyChangeRoom(final int light, final int rollerBlinds) {
        jm.UpdateJSON(light, rollerBlinds);
    }

}
