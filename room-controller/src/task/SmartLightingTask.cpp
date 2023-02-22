#include <Arduino.h>
#include "SmartLightingTask.h"
#include "kernel/MsgService.h"
#include "config.h"

//#define MSG(state) (state == ON ? String("ON") : state == OFF ? String("OFF") : String("DISABLE"))

SmartLightingTask::SmartLightingTask(){
  led = new Led(LED_PIN);
  motor = new ServoMotorImpl(MOTOR_PIN);
}
  
void SmartLightingTask::init(int period){
  Task::init(period);
  led->switchOff();
  motor->on();
  motor->setPosition(0);
  state = OFF;
  //MsgService.sendMsg(SMART_LIGHTING_STATE_MSG + MSG(state));
}
  
void SmartLightingTask::tick(){
  switch (state){
    case OFF:
      led->switchLight();
      motor->setPosition(random(0,180));

      break;
    /*
      if (smartLighting->isSomeoneDetected()){
        smartLighting->turnLightOn();
        state = ON;
        MsgService.sendMsg(SMART_LIGHTING_STATE_MSG + MSG(state));
      }
      break;
    case ON:
      if (!(smartLighting->isSomeoneDetected())){
        smartLighting->turnLightOff();
        state = OFF;
        MsgService.sendMsg(SMART_LIGHTING_STATE_MSG + MSG(state));
      }
      break;
      */
  }
}
