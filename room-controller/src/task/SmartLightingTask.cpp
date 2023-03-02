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
  state = ON;
}

void SmartLightingTask::tick(){
  switch (state){
    case ON:
      if(MsgSerial.isMsgAvailable()){
         Msg *msg = MsgSerial.receiveMsg();
         doCommand(msg);
      } else if(msgBT->isMsgAvailable()){
        Msg *msg = msgBT->receiveMsg();
        doCommand(msg);
      }
      break;
  }
}

void SmartLightingTask::doCommand(Msg *msg){
  String string = msg->getContent();
  Serial.println(string);//CANCELLARE
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
      motor->setPosition(map(value, 0, 100, 0, 180));
      break;
  }
}

//#define MSG(state) (state == ON ? String("ON") : state == OFF ? String("OFF") : String("DISABLE"))
//MsgService.sendMsg(SMART_LIGHTING_STATE_MSG + MSG(state));

