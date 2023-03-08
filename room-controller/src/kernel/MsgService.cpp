#include <Arduino.h>
#include "MsgService.h"

MsgServiceSerial MsgSerial;

void MsgFifoList::enqueue(Msg* msg) {
  if ((tail + 1) % MAX_SIZE == head) {
    Serial.println("The list is full");
    return;
  }

  list[tail] = msg;
  tail = (tail + 1) % MAX_SIZE;
}

Msg* MsgFifoList::dequeue() {
  Msg* msg = list[head];
  head = (head + 1) % MAX_SIZE;
  return msg;
}

bool MsgFifoList::isEmpty() {
  return head == tail;
}

void MsgFifoList::print_list() {
  Serial.print("List: ");
  int i = head;
  while (i != tail) {
    Serial.print(list[i]->getContent());
    Serial.print(" ");
    i = (i + 1) % MAX_SIZE;
  }
  Serial.println();
}

bool MsgServiceSerial::isMsgAvailable(){
  return !list.isEmpty();
}

Msg* MsgServiceSerial::receiveMsg(){
  if (list.isEmpty()){
    return NULL;
  }
  return list.dequeue();
}

void MsgServiceSerial::init(int bound){
  Serial.begin(bound);
  content.reserve(256);
  content = "";
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
      MsgSerial.list.enqueue(new Msg(MsgSerial.content));
      MsgSerial.content = "";     
    } else {
      MsgSerial.content += ch;      
    }
  }
  
}

