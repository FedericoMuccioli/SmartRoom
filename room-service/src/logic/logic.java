package logic;

import java.time.LocalTime;
import Json.JsonManager;

public class logic {

    int LIGHT_TRESHOLD = 400;
    JsonManager jm;

    public logic(JsonManager jm){
        this.jm = jm;
    }

    public String[] UpdateLogic(String motion, int luminosity){
        int oraCorrente = (LocalTime.now()).getHour();
        String[] ArrayValues = {"null", "null"};
        if(motion=="ON"){
            if(luminosity<LIGHT_TRESHOLD){
                //Luci accese
                ArrayValues[0] = "ON";
            }
            if(oraCorrente>=8 && oraCorrente<=19){
                //Persiane tutte su
                ArrayValues[1] = "0";
            }
        }else{
            //Luci spente
            ArrayValues[0] = "OFF";
            if(oraCorrente>=19){
                //Persiane tutte giù
                ArrayValues[1] = "100";
            }
        }
        return ArrayValues;
    }
}
