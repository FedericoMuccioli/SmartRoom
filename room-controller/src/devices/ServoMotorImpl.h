#ifndef __SERVOMOTORIMPL__
#define __SERVOMOTORIMPL__

#include <Arduino.h>
#include <ServoTimer2.h>
#include "ServoMotor.h"

class ServoMotorImpl: public ServoMotor {

public:
  ServoMotorImpl(int pin);

  void on();
  void off();
  void setPosition(int angle);
  int getPosition();
    
private:
  int pin;
  int angle;
  ServoTimer2 motor;
};

#endif
