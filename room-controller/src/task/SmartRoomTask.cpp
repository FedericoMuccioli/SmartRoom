#include <Arduino.h>
#include "SmartRoomTask.h"
#include "config.h"

SmartRoomTask::SmartRoomTask(MsgServiceBT* msgBT){
  led = new Led(LED_PIN);
  motor = new ServoMotorImpl(MOTOR_PIN);
  this->msgBT = msgBT;
}

void SmartRoomTask::init(int period){
  Task::init(period);
  led->switchOff();
  motor->on();
  motor->setPosition(0);
}

void SmartRoomTask::tick(){
  if(MsgSerial.isMsgAvailable()){
    updateRoom(MsgSerial.receiveMsg());
  } else if(msgBT->isMsgAvailable()){
    updateRoom(msgBT->receiveMsg());
  }
}

void SmartRoomTask::updateRoom(Msg *msg){
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
    case 'r':
      motor->setPosition(value);
      break;
  }
  notifyServer();
}

void SmartRoomTask::notifyServer(){
  String lightStateMsg =  String('l' + String(led->isOn() ? '1' : '0'));
  String rollerBlindsStateMsg = String('r' + String(motor->getPosition()));
  MsgSerial.sendMsg(lightStateMsg + '&' + rollerBlindsStateMsg);
}
