#include <Arduino.h>
#include "MsgService.h"

String content;

MsgServiceSerial MsgSerial;

bool MsgServiceSerial::isMsgAvailable(){
  return msgAvailable;
}

Msg* MsgServiceSerial::receiveMsg(){
  if (msgAvailable){
    Msg* msg = currentMsg;
    msgAvailable = false;
    currentMsg = NULL;
    content = "";
    return msg;  
  } else {
    return NULL; 
  }
}

void MsgServiceSerial::init(int bound){
  Serial.begin(bound);
  content.reserve(256);
  content = "";
  currentMsg = NULL;
  msgAvailable = false;  
}

void MsgServiceSerial::sendMsg(const String& msg){
  Serial.println(msg);
}

MsgServiceBT::MsgServiceBT(int rxPin, int txPin){
  channel = new SoftwareSerial(rxPin, txPin);
}

void MsgServiceBT::init(int bound){
  content.reserve(256);
  channel->begin(9600);
  availableMsg = NULL;
}

bool MsgServiceBT::sendMsg(Msg msg){
  channel->println(msg.getContent());  
}

bool MsgServiceBT::isMsgAvailable(){
  while (channel->available()) {
    char ch = (char) channel->read();
    if (ch == '\n'){
      availableMsg = new Msg(content); 
      content = "";
      return true;    
    } else {
      content += ch;      
    }
  }
  return false;  
}

Msg* MsgServiceBT::receiveMsg(){
  if (availableMsg != NULL){
    Msg* msg = availableMsg;
    availableMsg = NULL;
    return msg;  
  } else {
    return NULL;
  }
}

void serialEvent() {
  /* reading the content */
  while (Serial.available()) {
    char ch = (char) Serial.read();
    if (ch == '\n'){
      MsgSerial.currentMsg = new Msg(content);
      MsgSerial.msgAvailable = true;      
    } else {
      content += ch;      
    }
  }
  
}

