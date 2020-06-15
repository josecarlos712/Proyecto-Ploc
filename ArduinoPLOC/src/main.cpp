#define BLYNK_PRINT Serial

#include <Arduino.h>
#include "ArduinoJson.h"
#include <ESP8266WiFi.h>
#include <ESP8266HTTPClient.h>
#include <SoftwareSerial.h>
#include <stdlib.h>
#include <BlynkSimpleEsp8266.h>
#include <PubSubClient.h>

const int RL = D0;
const int FL = D1;
const int RR = D2;
const int FR = D3;

const int pinecho = D7;
const int pintrigger = D8;

long timestamp = 0;

int tiempo = 0;
int distancia = 0;

int idDron = 1; //Asignamos la ID del propio dron
int idRuta = -1;//Como no sabe la ruta que tiene asignada, debe pedirla
int idSensor = -1;//Como no sabe el sensor que tiene asignada, debe pedirla
String ruta = "";//Ruta que seguirá nuestro pequeño
String rutaProcesada = "";
String parkingPath = ""; //Ruta para salir del parking
String estadoDron = "INACTIVO"; //Estado en el que se encuentra el Dron

String estadoRuta = "NO ASIGNADA";

char respondeBuffer[300];
WiFiClient client;

char auth[] = "";
char ssid[] = "PUERTOSURFIBRA869A";
char pass[] = "";

String SSID = "PUERTOSURFIBRA869A";
String PASS = "";

String SERVER_IP = "192.168.100.115";
int SERVER_PORT = 80;

int manual = 0;
int needActInfo = 0;

//MQTT
String USERNAME = "Dron1";
char* SERVER_IP_MQTT  =  "192.168.100.115";
int MQTT_PORT = 1885;

PubSubClient clienteMQTT(client);

int calculaDistancia();

void obtenerInfo(); //Obtiene información relacionada con el propio dron
void obtenerRuta(); //Obtiene toda la información relacionada con la ruta

void enviarValores(); //Enviamos los valores de los sensores a la base de datos

void avanzar();
void retroceder();
void giroIzquierda();
void giroDerecha();
void pausa();

void trazaRuta(String r);

void callback(char* topic, byte* payload, unsigned int length); //Función en la que se hará el tratamiento del mensaje MQTT

BLYNK_WRITE(V5)
{
  manual = param.asInt(); // assigning incoming value from pin V5 to a variable
}

BLYNK_WRITE(V6)
{
  needActInfo = param.asInt(); // assigning incoming value from pin V6 to a variable
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

  //Conexión wifi
  Serial.println("Connecting...");
  while(WiFi.status() != WL_CONNECTED){
    delay(500);
    Serial.println(".");
  }
  Serial.println("Connected succesfuly");
  Serial.print("IP: ");
  Serial.println(WiFi.localIP());

  //MQTT
  clienteMQTT.setServer(SERVER_IP_MQTT,MQTT_PORT);
  clienteMQTT.setCallback(callback);
  
  while(!clienteMQTT.connect("Dron1","mqttbroker","mqttbrokerpass")){
    Serial.println("Coneccting to MQTT");
  }
  Serial.println("Conectado al servidor MQTT");

  clienteMQTT.publish("stop", "Dron conectado");
  clienteMQTT.subscribe("stop");

  //Solucitud de información adicional
  obtenerInfo();

}

void loop() {

  Blynk.run();
  clienteMQTT.loop();

  clienteMQTT.publish("stop", "Dron conectado");

  if(needActInfo){obtenerInfo(); estadoDron = "LISTO PARA ENRUTAR"; Serial.println("ACTUALIZADA INFORMACIÓN DE DRON"); } //Necesita actualizar la información

  if(manual != 1){

    digitalWrite(D5,LOW);
    pausa();
    
    if(idRuta != -1){

      if(estadoDron == "GARAJE"){
        trazaRuta(parkingPath);
        estadoDron = "LISTO PARA ENRUTAR";
      }

      if(estadoDron == "LISTO PARA ENRUTAR"){
        //Pide la ruta correspondiente
        obtenerRuta();
        estadoDron = "EN RUTA";
        //Tratamiento de la ruta
        trazaRuta(ruta);
        Serial.println("**********************************************************************************");
        Serial.print("ESTADO DE DRON: ");
        Serial.println(estadoDron);
        Serial.println("**********************************************************************************");
        Serial.print("RUTA ");
        Serial.println(estadoRuta);
        Serial.println("**********************************************************************************");
      }

    }
      delay(1000);
  }

  if(calculaDistancia() < 20 && !(RL == 1 && RR == 1 && FL == 0 && FR == 0)){
 
      ruta = "P";
      pausa();

  }

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
    parkingPath = doc[0]["parkingPath"].as<char*>();
    idSensor = doc[0]["idSensor"].as<int>();
    idRuta = doc[0]["idRuta"].as<int>();
    estadoDron = doc[0]["estado"].as<char*>();        

    Serial.println(idDron);
    Serial.println(pesoSoportado);
    Serial.println(bateria);
    Serial.println(parkingPath);
    Serial.println(idSensor);
    Serial.println(idRuta);
    Serial.println(estadoDron);
    
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
    //int idSensor = doc[0]["idSensor"].as<int>();
    int tiempoRuta = doc[0]["tiempoRuta"].as<int>();

    Serial.println(idSensor);
    Serial.println(tiempoRuta);
    Serial.println(ruta);
  }
}

void enviarValores(bool obs){
  if(WiFi.status() == WL_CONNECTED){
    HTTPClient http;
    char payload[50];

    http.begin(client, SERVER_IP, SERVER_PORT, "/api/valores", false);

    //String payload = "{\"idValor\":0,\"timestamp\":" + timestamp + ",\"obs\":" + obs + ",\"idSensor\":" + idSensor + "}";
    http.addHeader("content-type", "application/json");

    if(obs){
      sprintf(payload, "{\"idValor\":0,\"timestamp\":%d,\"obs\":true,\"idSensor\":%d}", timestamp, idSensor);
    }else{
      sprintf(payload, "{\"idValor\":0,\"timestamp\":%d,\"obs\":false,\"idSensor\":%d}", timestamp, idSensor);
    }

    Serial.print("Json posteado: ");
    //Serial.println(payload);

    int responseCode = http.POST(payload);
    String response = http.getString();
    Serial.println(response);
    http.end();
    
  }

}

  void trazaRuta(String r){

  rutaProcesada = "";
  String aux = "";
  
  int i = 0;
  int j = 0;
  
  for(i = 0; i <= r.length(); i++){

    if(r[i] == '-' || r[i] == ',' || r[i] == '\0'){
       if(aux.length() > 1){
          for(j = 0; j < aux.toInt(); j++){
            rutaProcesada += aux[1];  
          }
        }else{
            rutaProcesada += aux[0];
        }
        aux = "";      
    }else{
      aux += r[i]; 
    }
    Serial.print("La ruta procesada es: ");
    Serial.println(rutaProcesada); 
    //Serial.print("La ruta aux es: ");
    //Serial.println(aux);
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

      pausa();
      enviarValores(true);
      rutaProcesada = "";
      ruta = "P";
      estadoRuta = "INTERRUMPIDA POR COLISION";
      estadoDron = "COLISIONADO :(";

      return;

    }
    //enviarValores(false);
    timestamp++;
    Blynk.run();
    clienteMQTT.loop();
    if(manual){ 
      pausa(); 
      ruta = "P"; 
      rutaProcesada = "";

      estadoRuta = "INTERRUMPIDA POR USUARIO";
      estadoDron = "CONTROL MANUAL ACTIVADO";   
    }
    //delay(1500);  
  }

  pausa();
  estadoRuta = "COMPLETADA CON EXITO";
  estadoDron = "ESPERANDO INSTRUCCIONES";

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

void callback(char* topic, byte* payload, unsigned int length) {

  String body = "";

  for (int i = 0; i < length; i++) {
    body += (char)payload[i];
  }
  
  const size_t capacity = JSON_OBJECT_SIZE(3) + JSON_ARRAY_SIZE(2) + 60;
  DynamicJsonDocument doc(capacity);

  DeserializationError error = deserializeJson(doc, body);
  if(error){
      Serial.print("Deserialize Error: ");
      Serial.println(error.c_str());
      return;
  }

  Serial.print("Mensaje recibido desde el canal: ");
  Serial.println(topic);

  if(topic == "stop"){

    Serial.println(F("Response:"));
    String nuevaRuta = doc[0]["path"].as<char*>(); //Se envía una nueva ruta a seguir por el Dron
    int dron = doc["idDron"].as<int>();

    Serial.println(nuevaRuta);
    Serial.println(dron);

    int dron = 1;

    if(dron == idDron){ 
      pausa(); 
      ruta = "P";
      rutaProcesada = "P"; 

      estadoRuta = "INTERRUMPIDA POR USUARIO";
      estadoDron = "CABREADO PORQUE LE HAN PARADO LA RUTA AL POBRE";  
    }

  }

}