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

//set position from 0 to 180
void ServoMotorImpl::setPosition(int angle){
  float coeff = (2250.0-750.0)/180;
  this->angle = angle < 0 ? 0 : angle > 180 ? 180 : angle;
  /*debug
  Serial.print("angle: ");
  Serial.println(this->angle);
  */
  motor.write(750 + this->angle*coeff);              
}

int ServoMotorImpl::getPosition(){
  return angle;
}



