#include "Pir.h"
#include "Arduino.h"

Pir::Pir(int pin){
  this->pin = pin;
  pinMode(pin, INPUT);
}

int Pir::checkDifference(){
  return digitalRead(this->pin);
}
