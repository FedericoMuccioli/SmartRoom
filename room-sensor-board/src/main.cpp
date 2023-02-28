#include <Arduino.h>
#include <WiFi.h>
#include <PubSubClient.h>
#include "config.h"
#include "devices/Led.h"
#include "devices/Pir.h"
#include "devices/photoresistor.h"

TaskHandle_t Task1;
enum {OFF, ON} state;
Led* led;
Pir* pir;
Photoresistor* photoresistor;

const char* ssid = WIFI_SSID;
const char* password = WIFI_PASSWORD;
/* MQTT server address */
const char* mqtt_server = MQTT_SERVER_ADDRESS;
/* MQTT topic */
const char* topic = MQTT_TOPIC;
/* MQTT client management */
WiFiClient espClient;
PubSubClient client(espClient);

void setup_wifi() {
  delay(10);
  Serial.println(String("Connecting to ") + ssid);
  WiFi.mode(WIFI_STA);
  WiFi.begin(ssid, password);
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
    // Create a random client ID
    // String clientId = String("esiot-2122-client-")+String(random(0xffff), HEX);
    // Attempt to connect
    if (client.connect("esiot-2122-client1")) {
      Serial.println("connected");
      // Once connected, publish an announcement...
      // client.publish("outTopic", "hello world");
      // ... and resubscribe
      client.subscribe(topic);
    } else {
      Serial.print("failed, rc=");
      Serial.print(client.state());
      Serial.println(" try again in 5 seconds");
      // Wait 5 seconds before retrying
      delay(5000);
    }
  }
}

void checkRoomState( void * parameter ){
  while(true){
    switch (state){
      case ON:
        if(!pir->checkDifference()){
          client.publish(MQTT_TOPIC, "MOVEMENT_DETECTED: OFF");
          client.publish(MQTT_TOPIC, "LUMINOSITY: ");
          led->switchOff();
          state = OFF;
        }
        break;
      case OFF:
        if(pir->checkDifference()){
          client.publish(MQTT_TOPIC, "MOVEMENT_DETECTED: ON");
          client.publish(MQTT_TOPIC, "LUMINOSITY:");
          led->switchOn();
          state = ON;
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
  client.setServer(mqtt_server, 1883);
  client.setCallback(callback);
  led = new Led(LED_PIN);
  pir = new Pir(PIR_PIN);

  Photoresistor* photoresistor = new Photoresistor(PHOTORESISTOR_PIN);
  led->switchOff();
  state = OFF;
  xTaskCreatePinnedToCore(checkRoomState,"Task1",10000,NULL,1,&Task1,0);
  delay(500);
}

void loop() {
  if (!client.connected()) {
    reconnect();
  }
  client.loop();
}