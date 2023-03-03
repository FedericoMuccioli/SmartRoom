#ifndef __SMARTLIGHTINGTASK__
#define __SMARTLIGHTINGTASK__

#include "kernel/Task.h"
#include "devices/Led.h"
#include "devices/ServoMotorImpl.h"
#include "kernel/MsgService.h"

#define SMART_LIGHTING_PERIOD 100

class SmartLightingTask: public Task {

private:
  Light* led;
  ServoMotor* motor;
  MsgServiceBT* msgBT;
  void doCommand(Msg *msg);
  void notifyServer();

public:
  SmartLightingTask(MsgServiceBT* msgBT);  
  void init(int period);
  void tick();
};

#endif
