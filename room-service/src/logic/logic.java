package logic;

import java.time.LocalTime;

import communication.RoomControllerComm;
import Json.JsonManager;

public class logic {

    int LIGHT_TRESHOLD = 400;
    RoomControllerComm Controller;
    JsonManager jm;

    public logic(RoomControllerComm Controller, JsonManager jm){
        this.Controller = Controller;
        this.jm = jm;
    }

    public void UpdateLogic(String msg){
        String[] msgArray = msg.split("&");
        String motionValue = msgArray[0].split(":")[1];
        int luminosityValue = Integer.parseInt(msgArray[1].split(": ")[1]);
        int oraCorrente = (LocalTime.now()).getHour();

        if(motionValue=="ON"){
            if(luminosityValue<LIGHT_TRESHOLD){
                Controller.setLight(true);
                //jm.updateLights("ON");
            }
            if(oraCorrente>=8 && oraCorrente<=19){
                //Persiane tutte su
                Controller.setRollerBlinds(0);
                //jm.updateBlinds(0);
            }
        }else{
            Controller.setLight(false);
            //jm.updateLights("OFF");
            if(oraCorrente>=19){
                //Persiane tutte giù
                Controller.setRollerBlinds(180);
                //jm.updateBlinds(100);
            }
        }
    }
}
