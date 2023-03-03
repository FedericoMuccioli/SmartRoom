package communication;

public class RoomControllerComm {
	
	private final static String LIGHT = "l";
	private final static String ROLLER_BLINDS = "d";
	private final static String REGEX = "&";
	
	private final CommChannel comm;
	private int light;
	private int rollerBlinds;
	
	public RoomControllerComm(final CommChannel commChannel) {
		this.comm = commChannel;
	}
	
	public boolean isChangeState() {
		String state = null;
		while (comm.isMsgAvailable()) {
			try {
				state = comm.receiveMsg();
			} catch (InterruptedException e) {
				e.printStackTrace();
				return false;
			}
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
		}
		return state != null;
	}
	
	public void setLight(final boolean light) {
		comm.sendMsg(LIGHT + (light == true ? "1" : "0"));
	}
	
	public void setRollerBlinds(final int degree) {
		comm.sendMsg(ROLLER_BLINDS + String.valueOf(degree));
	}
	
	public int getLight() {
		return light;
	}

	public int getRollerBlinds() {
		return rollerBlinds;
	}

}
