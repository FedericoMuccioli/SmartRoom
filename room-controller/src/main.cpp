#include <Arduino.h>
#include "config.h"
#include "kernel/MsgService.h"
#include "kernel/Scheduler.h"
#include "task/SmartRoomTask.h"

Scheduler sched;
MsgServiceBT* msgBT;
Task* smartRoomTask;

void setup() 
{
  sched.init(SCHEDULER_PERIOD);
  msgBT = new MsgServiceBT(RX_PIN, TX_PIN);
  msgBT->init(BOUND);
  MsgSerial.init(BOUND);
  smartRoomTask = new SmartRoomTask(msgBT);
  smartRoomTask->init(SMART_ROOM_PERIOD);
  sched.addTask(smartRoomTask);
}

void loop() 
{
  sched.schedule();
}
