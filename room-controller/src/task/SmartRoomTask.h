#ifndef __SMARTROOMTASK__
#define __SMARTROOMTASK__

#include "kernel/Task.h"
#include "devices/Led.h"
#include "devices/ServoMotorImpl.h"
#include "kernel/MsgService.h"

#define SMART_ROOM_PERIOD 1000

class SmartRoomTask: public Task {

private:
  Light* led;
  ServoMotor* motor;
  MsgServiceBT* msgBT;
  void updateRoom(Msg *msg);
  void notifyServer();

public:
  SmartRoomTask(MsgServiceBT* msgBT);  
  void init(int period);
  void tick();
};

#endif
