package logic;

import java.time.LocalTime;

public class Logic {

    private final static int LIGHT_TRESHOLD = 400;

    public String[] updateLogic(final String motion, final int luminosity) {
        int oraCorrente = (LocalTime.now()).getHour();
        String[] ArrayValues = {"null", "null"};
        if (motion.equals("ON")) {
            if(luminosity<LIGHT_TRESHOLD){
                //Luci accese
                ArrayValues[0] = "ON";
            }
            if(oraCorrente>=8 && oraCorrente<19){
                //Persiane tutte su
                ArrayValues[1] = "0";
            }
        }else{
            //Luci spente
            ArrayValues[0] = "OFF";
            if(oraCorrente>=19 || oraCorrente<8){
                //Persiane tutte giù
                ArrayValues[1] = "100";
            }
        }
        return ArrayValues;
    }
}
