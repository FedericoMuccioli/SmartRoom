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
    doCommand(MsgSerial.receiveMsg());
  } else if(msgBT->isMsgAvailable()){
    doCommand(msgBT->receiveMsg());
  }
}

void SmartLightingTask::doCommand(Msg *msg){
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

//#define MSG(state) (state == ON ? String("ON") : state == OFF ? String("OFF") : String("DISABLE"))
//MsgService.sendMsg(SMART_LIGHTING_STATE_MSG + MSG(state));

