#include <Arduino.h>
#include <WiFi.h>
#include <PubSubClient.h>
#include "wifiManager.h"
#include "config.h"
#include "msgFormatter.h"
#include "Led.h"
#include "Pir.h"
#include "photoresistor.h"

TaskHandle_t RoomStateTask;
State state;
Led* led;
Pir* pir;
Photoresistor* photoresistor;
WiFiClient espClient;
PubSubClient client(espClient);

void callback(char* topic, byte* payload, unsigned int length) {
  Serial.println(String("Message arrived on [") + topic + "] len: " + length );
}

void reconnect() {
  while (!client.connected()) {
    Serial.print("Attempting MQTT connection...");
    if (client.connect((String("IoT-client-")+String(random(0xffff), HEX)).c_str())) {
      Serial.println("Connected");
      client.subscribe(MQTT_TOPIC);
    } else {
      Serial.print("Failed, try again in 5 seconds");
      delay(5000);
    }
  }
}

void checkRoomState( void * parameter ){
  while(true){
    switch (state){
      case ON:
        if(!pir->checkDifference()){
          led->switchOff();
          state = OFF;
          client.publish(MQTT_TOPIC, MSGFormatter::formatMsg(state, photoresistor->getLuminosity()).c_str());
        }
        break;
      case OFF:
        if(pir->checkDifference()){
          led->switchOn();
          state = ON;
          client.publish(MQTT_TOPIC, MSGFormatter::formatMsg(state, photoresistor->getLuminosity()).c_str());
        }
        break;
    }
    delay(1000);
  }
}

void setup() {
  Serial.begin(115200);
  WiFiManager::establishConnection(WIFI_SSID, WIFI_PASSWORD);
  randomSeed(micros());
  client.setServer(MQTT_SERVER_ADDRESS, 1883);
  client.setCallback(callback);
  led = new Led(LED_PIN);
  pir = new Pir(PIR_PIN);
  photoresistor = new Photoresistor(PHOTORESISTOR_PIN);
  led->switchOff();
  state = OFF;
  xTaskCreatePinnedToCore(checkRoomState,"checkRoomStateTask",10000,NULL,1,&RoomStateTask,0);
  delay(500);
}

void loop() {
  if (!client.connected()) {
    reconnect();
  }
  client.loop();
}