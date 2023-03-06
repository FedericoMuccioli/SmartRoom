#ifndef __PIR__
#define __PIR__

class Pir {

public:
  Pir(int pin);
  int checkDifference();
private:
  int pin;
};

#endif