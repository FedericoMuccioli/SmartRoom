#ifndef __MSGSERVICE__
#define __MSGSERVICE__

#include <Arduino.h>
#include "SoftwareSerial.h"

class Msg {
  String content;

public:
  Msg(String content){
    this->content = content;
  }
  
  String getContent(){
    return content;
  }
};

class MsgServiceSerial {
    
public: 
  
  Msg* currentMsg;
  bool msgAvailable;

  void init(int bound);  

  bool isMsgAvailable();
  Msg* receiveMsg();
  
  void sendMsg(const String& msg);
};

class MsgServiceBT {
    
public: 
  MsgServiceBT(int rxPin, int txPin);  
  void init(int bound);  
  bool isMsgAvailable();
  Msg* receiveMsg();
  bool sendMsg(Msg msg);

private:
  String content;
  Msg* availableMsg;
  SoftwareSerial* channel;
  
};

extern MsgServiceSerial MsgSerial;

#endif
