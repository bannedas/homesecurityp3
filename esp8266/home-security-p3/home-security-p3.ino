#include "FirebaseESP8266.h"
#include <ESP8266WiFi.h>

#define WIFI_SSID "wifi"
#define WIFI_PASSWORD "password"
#define FIREBASE_HOST "home-security-p3.firebaseio.com"
#define FIREBASE_AUTH "9kCJL1uJ2h8avxoD5uJniT7yb4ANp9ttM4uX0Xj8"

FirebaseData firebaseData;

int sensorPin = 16;
int pirState = LOW;
int sensorValue = 0;
int ledPin = 5;

void setup() {
  Serial.begin(9600);
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  
  pinMode(sensorPin, INPUT); // set input on pin 16
  pinMode(ledPin, OUTPUT); // set output on pin 5
  
  Serial.print("Connecting to Wi-Fi");
  while (WiFi.status() != WL_CONNECTED)
  {
    Serial.print(".");
    delay(300);
  }
  Serial.println();
  Serial.print("Connected with IP: ");
  Serial.println(WiFi.localIP());
  Serial.println();

  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);
  Firebase.reconnectWiFi(true);

  //Set database read timeout to 1 minute (max 15 minutes)
  Firebase.setReadTimeout(firebaseData, 1000 * 60);
  //tiny, small, medium, large and unlimited.
  //Size and its write timeout e.g. tiny (1s), small (10s), medium (30s) and large (60s).
  Firebase.setwriteSizeLimit(firebaseData, "tiny");
}

void loop(){
  sensorValue = digitalRead(sensorPin);  // read sensor value

  String path = "/motion-sensor"; // database folder

  // function below writes sensor data to firebase
  // and returns true if successful 
  // false if failed
  // we print failed via serial for debugging purposes if failed to write
  if(!Firebase.setBool(firebaseData, path, sensorValue)) {
    Serial.println("FAILED");
  }
  if (sensorValue == HIGH) {  // check if the input is HIGH            
    if (pirState == LOW) {
      digitalWrite(ledPin, HIGH);  // turn LED ON
      Serial.println("Motion detected!"); // print on output change
      pirState = HIGH;
    }
  } else {
    if (pirState == HIGH) {
      digitalWrite(ledPin, LOW); // turn LED OFF
      Serial.println("Motion ended!");  // print on output change
      pirState = LOW;
    }
  }

  delay(60*1000); // sleep for 60sec
}
