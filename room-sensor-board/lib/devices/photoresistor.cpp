#include "photoresistor.h"
#include "Arduino.h"

Photoresistor::Photoresistor(int pin){
  this->pin = pin;
  pinMode(pin, INPUT);
}

int Photoresistor::getLuminosity(){
  return analogRead(this->pin);
}