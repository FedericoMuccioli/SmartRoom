# IoT - SmartRoom 🏠 ![StatusBadge](https://badgen.net/badge/Status/Completed/green)

___
## PROJECT DESCRIPTION:

**This is a university project for the "Embedded Systems and Internet of Things" Exam.**

*This project is the third and final assignment.*

**SmartRoom is an IoT-based project** that allows users to remotely control and automate a room's lighting and window blinds.

The project includes 5 sub-services:

- **Room-App:** An Android application that allows users to remotely control the lights and window blinds from their phone.
- **Room-Controller:** A microcontroller that receives information from the Room-Service and Room-App and controls the lights and window blinds accordingly. It reports back to the Room-Service once the changes have been made.
- **Room-Dashboard:** A website that users can use to view the room's history and remotely control the lights and window blinds.
- **Room-Sensor-Board:** A microcontroller that detects when someone enters or exits the room and sends an MQTT message to the Room-Service when a change in state occurs.
- **Room-Service:** The main service of the project that handles all interactions between the sub-services. It receives information from the Room-Sensor-Board and sends commands to the Room-Controller. It also receives information from the Room-Dashboard and sends commands to the Room-Controller. Finally, it is updated by the Room-Controller when changes are made to the room. It writes JSON files every time the room is modified, which are then sent to the Room-Dashboard when requested.

___
## FEATURES:

- Automatic lighting control based on the current light level and time of day.
- Automatic control of window blinds based on the current light level and time of day.
- Ability to manually control the lighting and window blinds via Android APP.
- Ability to manually control the lighting and window blinds via Web APP.
- MQTT protocol for wireless communication between the Room Sensor Board and the Room Controller.
- Room status history using JSONs

___
## COMPATIBILITY:
The **Room Controller is based on an Arduino board**, and the **Room Sensor Board is based on an ESP32 board**.
The **Mobile application** can be used only on **Android devices**.
The **Web app** is designed to be *mobile-friendly* and can be accessed from **any device of your choice**.

___
## DOCUMENTATION
*There is a folder containing all the documentation:*

- The complete report of the project.
- All Wiring Diagrams.
- Screenshots of the Android application.
- Screenshots of the Web application.
- Room Controller State Scheme.
- Room Sensor Board State Scheme.

___
## REPORT
*There is a PDF report which explains in detail the organization, structure and operation of the entire project.*
