#include "ServoMotorImpl.h"

ServoMotorImpl::ServoMotorImpl(int pin){
  this->pin = pin;
  this->angle = 0;
} 

void ServoMotorImpl::on(){
  motor.attach(pin);
}

void ServoMotorImpl::off(){
  motor.detach();    
}

void ServoMotorImpl::setPosition(int percentage){
  int angle = map(percentage, 0, 100, 0, 180);
  float coeff = (2250.0-750.0)/180;
  this->angle = angle < 0 ? 0 : angle > 180 ? 180 : angle;
  motor.write(750 + this->angle*coeff);              
}

int ServoMotorImpl::getPosition(){
  return map(angle, 0, 180, 0, 100);;
}



