#ifndef __MSGSERVICE__
#define __MSGSERVICE__

#include <Arduino.h>
#include "SoftwareSerial.h"

#define MAX_SIZE 10

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

class MsgFifoList {
private:
  Msg* list[MAX_SIZE];
  int head = 0;
  int tail = 0;

public:
  void enqueue(Msg* msg);
  Msg* dequeue();
  bool isEmpty();
  void print_list();
  
};

class MsgServiceSerial {
    
public: 
  
  MsgFifoList list;
  String content;
  
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
