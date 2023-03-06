#include <Arduino.h>
#include "SmartLightingTask.h"
#include "config.h"

SmartLightingTask::SmartLightingTask(MsgServiceBT* msgBT){
  led = new Led(LED_PIN);
  motor = new ServoMotorImpl(MOTOR_PIN);
  this->msgBT = msgBT;
}

void SmartLightingTask::init(int period){
  Task::init(period);
  led->switchOff();
  motor->on();
  motor->setPosition(0);
}

void SmartLightingTask::tick(){
  if(MsgSerial.isMsgAvailable()){
    updateRoom(MsgSerial.receiveMsg());
  } else if(msgBT->isMsgAvailable()){
    updateRoom(msgBT->receiveMsg());
  }
}

void SmartLightingTask::updateRoom(Msg *msg){
  String string = msg->getContent();
  delete msg;
  char code = string.charAt(0);
  int value = string.substring(1).toInt();
  switch(code){
    case 'l':
      switch (value){
        case 0:
          led->switchOff();
          break;
        case 1:
          led->switchOn();
          break;
        case 2:
          led->switchLight();
          break;
        }
      break;
    case 'd':
      motor->setPosition(value);
      break;
  }
  notifyServer();
}

void SmartLightingTask::notifyServer(){
  String lightStateMsg =  String('l' + String(led->isOn() ? '1' : '0'));
  String rollerBlindsStateMsg = String('d' + String(motor->getPosition()));
  MsgSerial.sendMsg(lightStateMsg + '&' + rollerBlindsStateMsg);
}
