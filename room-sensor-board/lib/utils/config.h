#ifndef __CONFIG__
#define __CONFIG__

typedef enum{
  OFF,
  ON
} State;

#define BOUND 115200
#define SCHEDULER_PERIOD 100

#define WIFI_SSID "TIM-47108471"
#define WIFI_PASSWORD "EZu3DG9eSqhd3dubP4CdN5dQ"
#define MQTT_SERVER_ADDRESS "broker.hivemq.com"
#define MQTT_TOPIC "ESP32-IoT-2023"

#define MSG_BUFFER_SIZE  50

#define LED_PIN 12
#define PIR_PIN 11
#define PHOTORESISTOR_PIN 8

#endif
