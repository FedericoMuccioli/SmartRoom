package communication;

public class RoomControllerComm {
	
	private final static String msgLight = "l";
	private final static String msgDegree = "d";
	
	private final CommChannel comm;
	
	public RoomControllerComm(final CommChannel commChannel) {
		this.comm = commChannel;
	}
	
	public void setLight(final boolean light) {
		comm.sendMsg(msgLight + (light == true ? "1" : "0"));
	}
	
	public void setRollerBlinds(final int degree) {
		comm.sendMsg(msgDegree + String.valueOf(degree));
	}

}
