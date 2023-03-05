#include <Arduino.h>
#include <WiFi.h>
#include <PubSubClient.h>
#include "config.h"
#include "devices/Led.h"
#include "devices/Pir.h"
#include "devices/photoresistor.h"

typedef enum{
  OFF,
  ON
} State;

TaskHandle_t RoomStateTask;
State state;
Led* led;
Pir* pir;
Photoresistor* photoresistor;
/* MQTT client management */
WiFiClient espClient;
PubSubClient client(espClient);

void setup_wifi() {
  delay(10);
  Serial.println(String("Connecting to ") + WIFI_SSID);
  WiFi.mode(WIFI_STA);
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  Serial.println("");
  Serial.println("WiFi connected");
  Serial.println("IP address: ");
  Serial.println(WiFi.localIP());
  WiFi.setAutoReconnect(true);
}

/* MQTT subscribing callback */
void callback(char* topic, byte* payload, unsigned int length) {
  Serial.println(String("Message arrived on [") + topic + "] len: " + length );
}

void reconnect() {
  // Loop until we're reconnected
  while (!client.connected()) {
    Serial.print("Attempting MQTT connection...");
    // Attempt to connect with a random client ID
    if (client.connect((String("IoT-client-")+String(random(0xffff), HEX)).c_str())) {
      Serial.println("connected");
      // Once connected, resubscribe
      client.subscribe(MQTT_TOPIC);
    } else {
      Serial.print("failed, try again in 5 seconds");
      // Wait 5 seconds before retrying
      delay(5000);
    }
  }
}

void sendUpdate(State s){
  char luminosity[100];
  char Movement[100];
  char Event[200];

  sprintf(luminosity, "LUMINOSITY: %d", photoresistor->getLuminosity());
  sprintf(Movement, "MOVEMENT_DETECTED: %s", (state == ON ? "ON" : "OFF"));
  strcpy(Event, Movement);
  strcat(Event, " & ");
  strcat(Event, luminosity);
  client.publish(MQTT_TOPIC, Event);
  Serial.println(Event);
}

void checkRoomState( void * parameter ){
  while(true){
    switch (state){
      case ON:
        if(!pir->checkDifference()){
          led->switchOff();
          state = OFF;
          sendUpdate(state);
        }
        break;
      case OFF:
        if(pir->checkDifference()){
          led->switchOn();
          state = ON;
          sendUpdate(state);
        }
        break;
    }
    delay(1000);
  }
}

void setup() {
  Serial.begin(115200);
  setup_wifi();
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