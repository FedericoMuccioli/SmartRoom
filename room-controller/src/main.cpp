#include <Arduino.h>
#include "config.h"
#include "kernel/MsgService.h"
#include "kernel/Scheduler.h"
#include "task/SmartLightingTask.h"

Scheduler sched;

void setup() 
{
  sched.init(SCHEDULER_PERIOD);
  MsgService.init(BOUND);
  Task* smartLightingTask = new SmartLightingTask();
  smartLightingTask->init(SMART_LIGHTING_PERIOD);
  sched.addTask(smartLightingTask);
}

void loop() 
{
  sched.schedule();
}
