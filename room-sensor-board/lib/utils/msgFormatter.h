#ifndef __MSGFORMATTER__
#define __MSGFORMATTER__

#include "config.h"
#include <Arduino.h>

class MSGFormatter {
public:
    static String formatMsg(State s, int luminosity);
};

#endif