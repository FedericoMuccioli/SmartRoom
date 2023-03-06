package communication;

import controller.Controller;
import jssc.SerialPortEvent;

public class RoomCommChannel extends SerialCommChannel {
	
	private final static String LIGHT = "l";
	private final static String ROLLER_BLINDS = "d";
	private final static String REGEX = "&";
	
	private Controller controller;
	private int light;
	private int rollerBlinds;
	
	public RoomCommChannel(final String port, final int rate, final Controller controller) throws Exception {
		super(port, rate);
		this.controller = controller;
	}
	
	public void setLight(final boolean light) {
		sendMsg(LIGHT + (light == true ? "1" : "0"));
	}
	
	public void setRollerBlinds(final int degree) {
		sendMsg(ROLLER_BLINDS + String.valueOf(degree));
	}
	
	public int getLight() {
		return light;
	}

	public int getRollerBlinds() {
		return rollerBlinds;
	}
	
	public void serialEvent(SerialPortEvent event) {
		super.serialEvent(event);
		String state = null;
		try {
			while (isMsgAvailable()) {
				state = receiveMsg();
			}
			if(state != null) {
				var split = state.split(REGEX);
				for (String s : split) {
					String typeValue = Character.toString(s.charAt(0));
					final int value = Integer.parseInt(s.substring(1));
					if (typeValue.equals(LIGHT)) {
						light = value;
					} else if (typeValue.equals(ROLLER_BLINDS)) {
						rollerBlinds = value;
					}
				}
				controller.notifyChangeRoom(light, rollerBlinds);
			}
		} catch (Exception e) {
			System.out.println(state);
			return;
		}
	}

}
