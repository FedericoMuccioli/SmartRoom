#ifndef __WIFIMANAGER__
#define __WIFIMANAGER__

#include <WiFi.h>

class WiFiManager {
public:
    static void establishConnection(const char* ssid, const char* password);
};

#endif