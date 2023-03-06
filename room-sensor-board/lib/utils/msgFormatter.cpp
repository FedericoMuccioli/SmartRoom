#include "msgFormatter.h"
#include <Arduino.h>

String MSGFormatter::formatMsg(State s, int luminosityValue){
    char luminosity[100];
    char Movement[100];
    char Event[200];
    sprintf(luminosity, "LUMINOSITY: %d", luminosityValue);
    sprintf(Movement, "MOVEMENT_DETECTED: %s", (s == ON ? "ON" : "OFF"));
    strcpy(Event, Movement);
    strcat(Event, " & ");
    strcat(Event, luminosity);
    return Event;
}