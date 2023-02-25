#include <Arduino.h>
#include "config.h"
#include "kernel/MsgService.h"
#include "kernel/Scheduler.h"
#include "task/SmartLightingTask.h"

Scheduler sched;
MsgServiceBT* msgBT;
Task* smartLightingTask;

void setup() 
{
  sched.init(SCHEDULER_PERIOD);
  msgBT = new MsgServiceBT(RX_PIN, TX_PIN);
  msgBT->init(BOUND);
  MsgSerial.init(BOUND);
  smartLightingTask = new SmartLightingTask(msgBT);
  smartLightingTask->init(SMART_LIGHTING_PERIOD);
  sched.addTask(smartLightingTask);
}

void loop() 
{
  sched.schedule();
}
