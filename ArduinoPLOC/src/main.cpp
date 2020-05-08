#define BLYNK_PRINT Serial

#include <Arduino.h>
#include "ArduinoJson.h"
#include <ESP8266WiFi.h>
#include <ESP8266HTTPClient.h>
#include <SoftwareSerial.h>
#include <stdlib.h>
#include <BlynkSimpleEsp8266.h>

const int RL = D0;
const int FL = D1;
const int RR = D2;
const int FR = D3;

const int pinecho = D7;
const int pintrigger = D8;

int tiempo = 0;
int distancia = 0;

int idDron = 2; //Asignamos la ID del propio dron
int idRuta = -1;//Como no sabe la ruta que tiene asignada, debe pedirla
String ruta = "";//Ruta que seguirá nuestro pequeño

char respondeBuffer[300];
WiFiClient client;

char auth[] = "token";
char ssid[] = "PUERTOSURFIBRA869A";
char pass[] = "contraseña";

String SSID = "PUERTOSURFIBRA869A";
String PASS = "contraseña";

String SERVER_IP = "192.168.100.115";
int SERVER_PORT = 80;

int manual = 0;

int calculaDistancia();

void obtenerInfo(); //Obtiene información relacionada con el propio dron
void obtenerRuta(); //Obtiene toda la información relacionada con la ruta

void avanzar();
void retroceder();
void giroIzquierda();
void giroDerecha();
void pausa();

void trazaRuta();

BLYNK_WRITE(V5)
{
  manual = param.asInt(); // assigning incoming value from pin V1 to a variable
  // You can also use:
  // String i = param.asStr();
  // double d = param.asDouble();
}

void setup() {

  pinMode(RL,OUTPUT);
  pinMode(FL,OUTPUT);
  pinMode(RR,OUTPUT);
  pinMode(FR,OUTPUT);
  pinMode(D5,OUTPUT);
  pinMode(D4,INPUT);

  pinMode(pinecho, INPUT);
  pinMode(pintrigger, OUTPUT);

  // put your setup code here, to run once:
  Serial.begin(9600);
  WiFi.begin(SSID,PASS);

  Blynk.begin(auth,ssid,pass);

  Serial.println("Connecting...");
  while(WiFi.status() != WL_CONNECTED){
    delay(500);
    Serial.println(".");
  }
  Serial.println("Connected succesfuly");
  Serial.print("IP: ");
  Serial.println(WiFi.localIP());
}

void loop() {
  Blynk.run();

  if(manual != 1){

    pausa();
    //Solucitud de información adicional
    obtenerInfo();
  
    if(idRuta != -1){
      //Pide la ruta correspondiente
      obtenerRuta();
      //Tratamiento de la ruta
      trazaRuta();
    }

  }

  delay(10000);
}

void obtenerInfo(){
  if(WiFi.status() == WL_CONNECTED){
    HTTPClient http;
    char m[15];
    sprintf(m,"/api/drones/%d",idDron);

    Serial.print("La dirección a la que se va a pedir la información es: ");
    Serial.println(m);

    http.begin(client, SERVER_IP, SERVER_PORT, m, true);
    int httpCode = http.GET();

    Serial.println("Response code: " + httpCode);

    String payload = http.getString();

    const size_t capacity = JSON_OBJECT_SIZE(9) + JSON_ARRAY_SIZE(2) + 60;
    DynamicJsonDocument doc(capacity);

    DeserializationError error = deserializeJson(doc, payload);
    if(error){
        Serial.print("Deserialize Error: ");
        Serial.println(error.c_str());
        return;
    }

    Serial.println(F("Response:"));
    int pesoSoportado = doc[0]["pesoSoportado"].as<int>();
    int bateria = doc[0]["bateria"].as<int>();
    String parkingPath = doc[0]["parkingPath"].as<char*>();
    int idSensor = doc[0]["idSensor"].as<int>();
    idRuta = doc[0]["idRuta"].as<int>();
    String estado = doc[0]["estado"].as<char*>();        

    Serial.println(idDron);
    Serial.println(pesoSoportado);
    Serial.println(bateria);
    Serial.println(parkingPath);
    Serial.println(idSensor);
    Serial.println(idRuta);
    Serial.println(estado);
    
  }

}

void obtenerRuta(){

  if(WiFi.status() == WL_CONNECTED){
    HTTPClient http;

    char m[15];
    sprintf(m,"/api/rutas/%d",idRuta);

    http.begin(client, SERVER_IP, SERVER_PORT, m, true);
    int httpCode = http.GET();

    Serial.println("Response code: " + httpCode);

    String payload = http.getString();

    const size_t capacity = JSON_OBJECT_SIZE(3) + JSON_ARRAY_SIZE(2) + 60;
    DynamicJsonDocument doc(capacity);

    DeserializationError error = deserializeJson(doc, payload);
    if(error){
        Serial.print("Deserialize Error: ");
        Serial.println(error.c_str());
        return;
    }

    Serial.println(F("Response:"));
    ruta = doc[0]["path"].as<char*>();
    int idSensor = doc[0]["idSensor"].as<int>();
    int tiempoRuta = doc[0]["tiempoRuta"].as<int>();

    Serial.println(idSensor);
    Serial.println(tiempoRuta);
    Serial.println(ruta);
  }
}

  void trazaRuta(){

  String rutaProcesada = "";
  String aux = "";
  
  int i = 0;
  int j = 0;
  
  for(i = 0; i <= ruta.length(); i++){

    if(ruta[i] == '-' || ruta[i] == ',' || ruta[i] == '\0'){
       if(aux.length() > 1){
          for(j = 0; j < aux.toInt(); j++){
            rutaProcesada += aux[1];  
          }
        }else{
            rutaProcesada += aux[0];
        }
        aux = "";      
    }else{
      aux += ruta[i]; 
    }
    Serial.print("La ruta procesada es: ");
    Serial.println(rutaProcesada); 
    Serial.print("La ruta aux es: ");
    Serial.println(aux);
  }

  for(int i = 0; i < rutaProcesada.length(); i++){
    Serial.println(rutaProcesada);
    switch(rutaProcesada[i]){
      case 'F':
        avanzar();
        break;
      case 'B':
        retroceder();
        break;
      case 'R':
        giroDerecha();
        break;
      case 'L':
        giroIzquierda();
        break;
      case 'P':
        pausa();
        break;
    }
    if(calculaDistancia() < 20){

      rutaProcesada = "";
      ruta = "P";

    }
    //delay(1500);  
  }

  pausa();
  
  }

void avanzar(){
  //AVANZA
  digitalWrite(RL,LOW);
  digitalWrite(FL,HIGH);
  digitalWrite(RR,LOW);
  digitalWrite(FR,HIGH);
  delay(1000);
}

void retroceder(){
  //RETROCEDE
  digitalWrite(RL,HIGH);
  digitalWrite(FL,LOW);
  digitalWrite(RR,HIGH);
  digitalWrite(FR,LOW);
  delay(1000);
}

void giroIzquierda(){
  //GIRA A LA IZQUIERDA
  digitalWrite(RL,HIGH);
  digitalWrite(FL,LOW);
  digitalWrite(RR,LOW);
  digitalWrite(FR,HIGH);
  delay(1500);
}

void giroDerecha(){
  //GIRA A LA DERECHA
  digitalWrite(RL,LOW);
  digitalWrite(FL,HIGH);
  digitalWrite(RR,HIGH);
  digitalWrite(FR,LOW);
  delay(1500);
}

void pausa (){
  //PAUSADO
  digitalWrite(RL,LOW);
  digitalWrite(FL,LOW);
  digitalWrite(RR,LOW);
  digitalWrite(FR,LOW);
  delay(1000);
}

int calculaDistancia(){

  // ENVIAR PULSO DE DISPARO EN EL PIN "TRIGGER"
  digitalWrite(pintrigger, LOW);
  delayMicroseconds(2);
  digitalWrite(pintrigger, HIGH);
  // EL PULSO DURA AL MENOS 10 uS EN ESTADO ALTO
  delayMicroseconds(10);
  digitalWrite(pintrigger, LOW);

  // MEDIR EL TIEMPO EN ESTADO ALTO DEL PIN "ECHO" EL PULSO ES PROPORCIONAL A LA DISTANCIA MEDIDA
  tiempo = pulseIn(pinecho, HIGH);

  // LA VELOCIDAD DEL SONIDO ES DE 340 M/S O 29 MICROSEGUNDOS POR CENTIMETRO
  // DIVIDIMOS EL TIEMPO DEL PULSO ENTRE 58, TIEMPO QUE TARDA RECORRER IDA Y VUELTA UN CENTIMETRO LA ONDA SONORA
  distancia = tiempo / 58;

  // ENVIAR EL RESULTADO AL MONITOR SERIAL
  Serial.print(distancia);
  Serial.println(" cm");
  delay(100);

  return distancia;

}
