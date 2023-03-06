#include <Arduino.h>
#include "Led.h"

Led::Led(int pin){
  this->pin = pin;
  pinMode(pin,OUTPUT);
  state = false;
}

void Led::switchOn(){
  digitalWrite(pin,HIGH);
  state = true;
}

void Led::switchOff(){
  digitalWrite(pin,LOW);
  state = false;
}

void Led::switchLight(){
  if (state){
    switchOff();
  } else {
    switchOn();
  }
}
