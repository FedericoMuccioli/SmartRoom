#ifndef __LED__
#define __LED__

#include "Light.h"

class Led: public Light { 
public:
  Led(int pin);
  void switchOn();
  void switchOff();
  void switchLight();
  bool isOn();
private:
  int pin;  
  bool state;
};

#endif
