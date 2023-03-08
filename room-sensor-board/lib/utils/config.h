#ifndef __CONFIG__
#define __CONFIG__

typedef enum{
  OFF,
  ON
} State;

#define BOUND 115200

#define WIFI_SSID "ssid"
#define WIFI_PASSWORD "psw"
#define MQTT_SERVER_ADDRESS "broker.hivemq.com"
#define MQTT_TOPIC "ESP32-IoT-2023"

#define LED_PIN 12
#define PIR_PIN 11
#define PHOTORESISTOR_PIN 8

#endif
