# INTRODUCCIÓN

La idea final de este proyecto es buscar desarrollar un sistema móvil (vehículo) que use una comunicación con un servidor para poder detectar obstáculos en su camino de un punto a otro y, si es posible, rodearlo o buscar otro camino para evitarlo. El servidor servirá como registro de los datos que el vehículo va reportando. Estos datos son analizados por el servidor y según estos, se le ordena a cada uno de ellos cómo y hacia dónde deben moverse para lograr alcanzar su objetivo. Esta tecnología es utilizada, por ejemplo, en los vehículos autómatas que se usan como mozos de almacén para recoger paquetes y reunirlos. Otro ejemplo que hemos visto en nuestra propia ciudad es la de un camarero autómata que se encarga de dar servicio entregando comida y bebida en una bandeja que porta. Estos vehículos siguen una cuadrícula precalculada que restringe el movimiento de estos. Nuestro cometido es diseñar vehículos de mantenimiento que se encarguen de detectar obstáculos en estas vías, informar a un servidor y. en última instancia, en caso de tener la funcionalidad adecuada implementada, retirar el objeto de la vía para dejarla libre.
Comenzaremos a desarrollar funcionalidades básicas, como: si, mientras que el vehículo es conducido, detecta algún obstáculo, este se detiene e informa al servidor de la incidencia. Se irá ampliando el proyecto a medida que vayamos completando requerimientos. De esta manera, el proyecto será entregable en varios momentos de su desarrollo (al final de cada iteración del desarrollo de sus funcionalidades) por si no se pudiese llegar a la idea final del proyecto.
En la primera iteración de nuestro proyecto (con fecha límite la fecha de la segunda iteración según el plan de la asignatura), ya que la “primera iteración” según la asignatura no es más que una presentación de ideas tenemos los siguientes requisitos en orden a cumplir:	

- Crear el servidor
- Se establece conexión con el servidor para recolectar o enviar datos
- El vehículo puede moverse (mediante comandos o algún control)
- El vehículo puede detectar obstáculos frente a sí mismo.

# Links

Placas de desarrollo - [Yizhet NodeMCU Lua Lolin V3](https://www.amazon.es/Yizhet-NodeMCU-ESP8266-Desarrollo-Internet/dp/B07XJWK5F4/ref=sr_1_4?__mk_es_ES=ÅMÅŽÕÑ&keywords=esp8266&qid=1581681743&sr=8-4)

Hemos preparado un video de presentación del proyecto en el cual puede verse el correcto funcionamiento de un dron que hemos creado nosotros y de los diferentes servidores. Dicho video puede pedirse a nuestro Bot de Telegram  **@PlocBot** o haciendo click [aquí](https://youtu.be/Le7JS5TxZe4)

# API REST

## GET

El método get obtiene información de la base de datos.

### TABLA SENSORES

**getAllSensors** → Esta función devuelve todos los sensores de la base de datos. 

URL: /api/sensores

**getSensorBySensorID**→ Esta función devuelve la información de un sensor con una ID que se especifica como parámetro. Se espera, que la ID especificada, sea un número que pertenezca a alguna ID de los sensores existentes en la base de datos. 

URL: /api/sensores/:idSensor

### TABLA RUTA

**getAllPaths** → Esta función devuelve todas las rutas disponibles.

URL: /api/rutas

**getPathByPathID** → Esta función devuelve la información de una ruta con una ID que se especifica como parámetro.

URL: /api/rutas/:idRuta

### TABLA VALORES

**getAllValues** → Esta función devuelve todas las entradas **abigarradas** de la tabla de valores que tenga disponible hasta el momento.

URL: /api/valores

**getValuesBySensorID** → Esta función devuelve la historia de un sensor especificado mediante la id del sensor como parámetro.

URL: /api/valores/:idSensor

### TABLA DRONES

**getAllDrones** → Esta función devuelve todos los drones disponibles, junto con información de su estado, además de su ruta y sensor asignados.

URL: /api/drones

**getDroneByDroneID** → Esta función devuelve la información de una ruta con una ID que se especifica como parámetro.

URL: /api/drones/:idDron

## POST

Crea una nueva entrada en la base de datos, pasándole como cuerpo de la petición toda la información necesaria como para poder instanciar el objeto en el servidor.

### TABLA SENSORES

**postSensor** → Crea una nueva entrada en la tabla de sensores. En el cuerpo de la petición se requieren los siguientes parámetros: 

- Tipo. Ya sea ultrasonido, sonoro, lumínico, etc.
- Tiempo de actuación. Especifica cada cuanto tiempo envía una actualización de su estado actual. En cuanto detecta un obstáculo se produce una interrupción e inmediatamente envía una actualización al servidor sin esperar al próximo ciclo.
- El id es autoincremental.

URL: /api/sensores

El Json usado para probar el funcionamiento del método post de la tabla sensores es: 

~~~json
{  "idSensor": 5,  "tipo" : "Proximidad",  "tiempoAct" : 3}
~~~

### TABLA RUTA

**postPath** → Crea una nueva entrada en la tabla de rutas. En el cuerpo de la petición se requieren los siguientes parámetros:

- Path. La nueva ruta a añadir.
- El tiempo de ruta se calcula en el servidor.
- El id es autoincremental

URL: /api/rutas

El Json usado para probar el funcionamiento del método post de la tabla rutas es:

~~~json
{  "idRuta": 0,  "TiempoRuta" : 140,  "path" : "5F-R-4F-L-2F"}
~~~

### TABLA VALORES

**postValue** → Crea una nueva entrada en la tabla de valores. En el cuerpo de la petición se requieren los siguientes parámetros:

- Obstáculo. Valor booleano que indica si hay algún obstáculo frente al dron.
- ID del dron. Indica de qué dron proviene el dato.
- El timestamp lo asigna el servidor.
- El id es autoincremental.	

URL: /api/values	

El Json usado para probar el funcionamiento del método post de la tabla valores es:

~~~	json
{  "idValor": 0,  "timestamp" : 1,  "obs" : true,  "idSensor" : 1}
~~~

### TABLA DRONES

**postDrone** → Crea una nueva entrada en la tabla de drones. En el cuerpo de la petición se requieren los siguientes parámetros:

- Peso soportado. Una actualización del peso soportado en caso de modificación del vehículo.
- Ruta de aparcamiento. Actualización de la nueva ruta de aparcamiento.
- ID del sensor. Actualización del ID del nuevo sensor.	
- Tanto la batería, el estado y la ruta son asignadas una vez se establece la conexión con el dron.

URL: /api/drones

El Json usado para probar el funcionamiento del método post de la tabla drones es: 

~~~json
{  "idDron": 2,  "pesoSoportado": 50,  "bateria": 100,  "parkingPath": "4F-R-2F",  "idSensor": 1,  "idRuta": 1,  "estado": "GARAJE"}
~~~

## PUT

El método put actualiza una entrada de la base de datos.

### TABLA SENSORES

**putSensor** → Actualiza la información de un sensor con una ID que se especifica como parámetro. En el cuerpo de la petición se requieren los siguientes parámetros:

- Tipo. Ya sea ultrasonido, sonoro, lumínico, etc.
- Tiempo de actuación. Especifica cada cuanto tiempo envía una actualización de su estado actual. 

URL: /api/sensores/:idSensor

El Json que se ha utilizado para comprobar el correcto funcionamiento del método put para la tabla drones es:

~~~json
{  "idSensor": 3,  "tipo" : "Prox",  "tiempoAct" : 8}
~~~

### TABLA RUTA

**putPath** → Actualiza la información de una ruta con una ID que se especifica como parámetro. En el cuerpo de la petición se requieren los siguientes parámetros:

- Path. La nueva ruta que sustituirá a la existente.
- El tiempo de ruta se calcula en el servidor.

URL: /api/rutas/:idRuta
El Json que se ha utilizado para comprobar el correcto funcionamiento del método put para la tabla drones es:

~~~json
{  "idRuta": 2,  "tiempoRuta" : 160,  "path" : "5F-R-4F-L-2F"}
~~~

### TABLA VALORES

No tiene sentido que la tabla valores tenga un método put ya que es solo de creación y lectura, no se pueden modificar los datos existentes.

### TABLA DRONES

**putDrone** → Actualiza la información de un dron con una ID que se especifica como parámetro. En el cuerpo de la petición se requieren los siguientes parámetros:

- Peso soportado. Una actualización del peso soportado en caso de modificación del vehículo.
- Ruta de aparcamiento. Actualización de la nueva ruta de aparcamiento.
- ID del sensor. Actualización del ID del nuevo sensor.	
- Tanto la batería, el estado y la ruta no pueden ser modificadas ya que se actualizan a posteriori tras establecerse la conexión con el dron.

URL: /api/drones/:idDron
El Json que se ha utilizado para comprobar el correcto funcionamiento del método put para la tabla drones es:

~~~json
{  "idDron": 2,  "pesoSoportado": 1,  "bateria": 98,  "parkingPath": "4F,R,2F",  "idSensor": 1,  "idRuta": 1,  "estado": "GARAJE"}
~~~

## DELETE

Borra una entrada de la base de datos.

### TABLA SENSORES

**deleteSensorBySensorID** → Esta función devuelve la información de un sensor con una ID que se especifica como parámetro. Se espera, que la ID especificada, sea un número que pertenezca a alguna ID de los sensores existentes en la base de datos. 

URL: /api/sensores/:idSensor

### TABLA RUTA

**deletePathByPathID** → Esta función devuelve la información de una ruta con una ID que se especifica como parámetro.

URL: /api/rutas/:idRuta

### TABLA VALORES

**deleteValuesBySensorID** → Esta función devuelve la historia de un sensor especificado mediante la id del sensor como parámetro. No tiene sentido pedir la historia de todos los sensores abigarrados ya que no tiene utilidad.

URL: /api/valores/:idSensor

### TABLA DRONES

**deleteDroneByDroneID** → Esta función devuelve la información de una ruta con una ID que se especifica como parámetro.

URL: /api/drones/:idDron

# MQTT

Hemos implementado un servidor MQTT que posee dos canales característicos, de los cuales hablaremos un poco más adelante. Antes de nada queremos resaltar que todo esto es muy provisional, porque hasta que no nos enfrentemos al problema a la hora de implementarlo completamente no podremos conocer por completo los diferentes canales que vamos a necesitar implementar. Los canales que actualmente se encuentran implementados son:

## SENSORS

Este canal se usa para que los drones que poseen un sensor asociado y hayan encontrado un obstáculo, puedan anunciar en el canal el punto exacto en el que lo han encontrado siguiendo la ruta que se les asignó.
Los mensajes siempre los envían los drones, por lo que el servidor simplemente deberá escuchar.
El contenido del Json que se envía en el mensaje es el siguiente:

~~~json
{"idDron": Dron que informa del obtáculo, "idSensor": Sensor que ha usado para detectar el obstáculo,"Path": Pasos que ha seguido hasta encontrar el obstáculo}
~~~

## PATHS

Este canal se usa para poder dar la orden al dron de que renueve su información de ruta. Por defecto, el dron sale del garaje y se queda esperando instrucciones, en ese momento, debemos enviar un mensaje por este canal con el idDron que queremos que actualice su información de ruta. De la misma forma, cuando un dron termina de realizar su ruta, se queda esperando a que le demos una nueva orden, por lo que si queremos que vuelva a actualizar su información de ruta, debemos enviar un mensaje por este canal.

Los mensajes que se envían son con formato Json y su contenido es el **idDron** del cual queremos que actualice su información de ruta

~~~json
{"idDron": Dron al que se le asigna la ruta}
~~~

## STOP

Este canal se usa para detener un dron de una forma rápida. Cuando un dron queda detenido, se queda esperando ruta, por lo que podemos decirle que actualice la ruta o que simplemente continúe parado.

Los mensajes que se envían son con formato Json y su contenido es el **idDron** del cual queremos que se pare

~~~json
{"idDron": Dron al que se le asigna la ruta}
~~~

# PROTOTIPO HARDWARE

Hemos completado el prototipo hardware y además hemos conseguido realizar la conexión con la base de datos. Además de este sistema de conexión a la base de datos usando la API REST, hemos incluido un sistema de control usando una aplicación para móvil mediante la librería Blynk. El código del mismo puede encontrarse en esta misma carpeta, su nombre es ArduinoPloc.

## VARIABLES RELEVANTES

Dentro del mismo, se puede ver que contamos con varias variables globales que podemos usar durante todo el programa, como son las variables

- **idDron** la cual contiene el ID del dron, que debemos especificarlo nosotros manualmente. 
- **idRuta** la cual contiene el ID de la ruta que se le ha asignado al dron. Este ID deberá solicitarse al servidor mediante la función correspondiente, en este caso es la función **obtenerInfo()**, en la cual se obtiene la información del dron asociada a su ID mediante una petición GET a la API REST.
- **idSensor** la cual contiene el ID del sensor que se le asocia al dron. Al igual que en el caso anterior, debe solicitarse previamente al servidor mediante la función **obtenerInfo()**.
- **Ruta** la cual contiene el path de la ruta que se le ha asignado al dron. Para ello, una vez que disponemos del idRuta asignado a nuestro dron, se llama a la función **obtenerRuta()**, en la cual se obtiene la información del dron asociada a su idRuta mediante una petición GET a la API REST.
- **ParkingPath** la cual contiene el path necesario para sacar al dron del garaje. Esta ruta solo se ejecutará en caso de que el dron se encuentre en el garaje y la obtendremos al realizar la función **obtenerInfo()**.
- **EstadoDron** la cual informa del estado actual en el que se encuentra el dron, este estado puede abarcar desde en garaje hasta en ruta o colisionado. Se irá actualizando según cambie el estado del Dron.
- **EstadoRuta** la cual informa del estado actual en el que se encuentra la ruta. Los estados pueden ser **NO ASIGNADA**, **EN PROCESO**, **INTERRUMPIDA** y **COMPLETADA**.

## FUNCIONES

En cuanto a las funciones, disponemos de varias funciones las cuales se encargan de realizar diferentes tareas, desde controlar el movimiento del dron mediante los pines correspondientes hasta de realizar las peticiones a la API REST. Vamos a proceder a catalogar las funciones más relevantes dentro del proyecto en varias secciones:

### MOVIMIENTO

En el apartado del movimiento, hay que recordar que nuestros drones deben ser capaces de realizar movimientos, pero estos movimientos se traducen en activación y desactivación de los pines asociados al controlador del motos. Para facilitar esta tarea, hemos definido una serie de funciones que realizan toda la configuración de pines para que el dron se mueva en función de lo que nosotros queremos:

- **avanzar()**
- **retroceder()**
- **giroIzquierda()**
- **giroDerecha()**
- **pausa()**

Cada una de estas funciones se activan durante un tiempo, en concreto durante un segundo, de forma que cuando interpretamos las rutas, el hecho de que aparezca un comando de avanzar, el dron avanzará durante un segundo.

### CONEXIÓN CON LA BASE DE DATOS

En el apartado de la conexión con la base de datos, ya se ha adelantado anteriormente que disponemos de dos funciones principales para el tratamiento de las rutas y además, hemos incluido una función que nos permite informar a la base de datos en caso de colisión enviando el timestamp en el que se ha encontrado el obstáculo. Estas tres funciones son las siguientes:

- **obtenerInfo()**: Esta función obtiene la información del dron asociada a su ID mediante una petición GET a la API REST. Esta información es la entrada entera correspondiente de la base de datos para ese dron. En ella podemos encontrar el estado de la batería, el estado actual del dron, las IDs que necesitamos...
- **obtenerRuta()**: Esta función obtiene la información del dron asociada a su idRuta mediante una petición GET a la API REST. Esta información, al igual que en el caso anterior, contiene todas las columnas de la entrada correspondiente de dicha ruta en la base de datos. En ella podemos encontrar el path de la ruta y el tiempo que tarda en realizarse dicha ruta
- **enviarValores()**: Esta función envía información a la base de datos sobre las colisiones, para ello, envía un Json que contiene el idSensor que tiene asignado, el timestamp en el que se ha producido la detección y el valor de la variable obs, la cual denota si hay un obstáculo o no.

En las funciones de **obtenerInfo()** y **obtenerRuta()**, solamente almacenaremos la información que realmente vayamos a utilizar, aunque si tenemos la placa enchufada al PC, podemos llegar a ver toda la información que estamos obteniendo. 

### TRATAMIENTO DE LAS RUTAS

En el apartado del tratamiento de las rutas, podemos encontrar una función que se encarga de interpretar el path que está asociado a la ruta que se le ha asignado al dron y de ejecutarlo. Primero, pasa el path por un procesado, en el que se crea la cadena completa que posteriormente se interpretará. Una vez hecho esto, la **rutaProcesada** se pasa a un bucle for, el cual va ejecutando todos los pasos hasta el final. Cada vez que realiza una acción llamando a una de las funciones correspondientes al movimiento que dijimos antes comprueba si tiene algún objeto delante, en caso de que lo tenga se detiene e informa al servidor, en caso contrario, continúa ejecutando la ruta procesada.

Una vez que el dron haya realizado satisfactoriamente la ruta que se le ha asignado, se queda a la espera de que se le asigne una nueva ruta. En caso de que no cambie la ruta en la base de datos, volverá a recorrer la misma. Entre cada asignación de rutas dejamos pasar un tiempo.

La función que se encarga de realizar todas estas acciones se llama: **trazaRuta()** 

### COMPROBACIÓN DE OBSTÁCULOS

En el apartado de la comprobación de obstáculos hemos añadido una función que se encarga de medir la distancia usando el sensor de ultrasonidos que tiene incorporado el dron. Esta función ejecuta todos los pasos necesarios para poder medir la distancia que son los siguientes:

1. Enviar un pulso de disparo al pin **Trigger**
2. Medir el tiempo en estado alto del pin **Echo**
3. Calcular la distancia en función del tiempo de ida y vuelta de la señal sónica

Esta función devuelve la distancia en cm y nosotros decidiremos en cada caso cómo usarla. La función que se encarga de realizar todas estas acciones se llama: **calculaDistancia()**

### CONTROL MANUAL

Hemos incluido un sistema de control manual usando la aplicación y las librerías de Blynk. Para ello, simplemente se ejecuta una instrucción llamada **Blynk.run()**, la cual se encuentra en el **loop()** y evalúa los botones que hemos establecido en el entorno virtual de Blynk, es decir, la aplicación y realiza la correspondiente acción en función del que se haya pulsado. Es decir, si se ha pulsado el botón de avanzar, se ejecutará la instrucción de avanzar.

# TELEGRAM

Hemos creado un bot de Telegram el cual nos ayuda a gestionar información sobre los drones y nos permite usar los canales de comunicación MQTT de una forma muy sencilla. El nombre del bot es: **@PlocBot**

El bot incorpora varias funciones, como la de informar sobre el estado actual del proyecto, mostrando información sobre el prototipo hardware, la base de datos, la API REST... Pero además, podemos realizar peticiones a la API REST para comprobar su correcto funcionamiento y además nos permite controlar los drones usando los canales MQTT que hasta ahora tenemos implementados y se encuentran funcionando. Para poder hacer esto, debemos disponer del usuario y la contraseñas necesarios para poder acceder al servidor MQTT.

