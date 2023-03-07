package controller;

import java.util.concurrent.TimeUnit;

import communication.RoomCommChannel;
import json.JsonManager;
import logic.Logic;

public class Controller {

	private final RoomCommChannel roomCommChannel;
	private final Logic logic;
	private final JsonManager jm;

	public Controller(final String port, final int rate, final Logic logic, final JsonManager jm) throws Exception {
		this.roomCommChannel = new RoomCommChannel(port, rate, this);
		this.logic = logic;
		this.jm = jm;
	}

	public void updateRoom(String movement, int luminosity) {
		String[] values = logic.updateLogic(movement, luminosity);
		if(values[0] != "null") {
			updateLights(values[0] == "ON");
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if(values[1] != "null") {
			updateRollerBlinds(Integer.parseInt(values[1]));
		}
	}

	public void updateLights(boolean lights) {
		roomCommChannel.setLight(lights);
		System.out.println("Lights: " + lights);
	}

	public void updateRollerBlinds(int rollerBlinds) {
		roomCommChannel.setRollerBlinds(rollerBlinds);
		System.out.println("Roller blinds: " + rollerBlinds);
	}
	
	public void notifyChangeRoom(final int light, final int rollerBlinds) {
		jm.updateJSON(light, rollerBlinds);
	}

}
