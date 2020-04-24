# INTRODUCCIÓN

La idea final de este proyecto es buscar desarrollar un sistema móvil (vehículo) que use una comunicación con un servidor para poder detectar obstáculos en su camino de un punto a otro y, si es posible, rodearlo o buscar otro camino para evitarlo. El servidor servirá como registro de los datos que el vehículo va reportando. Estos datos son analizados por el servidor y según estos, se le ordena a cada uno de ellos cómo y hacia dónde deben moverse para lograr alcanzar su objetivo. Esta tecnología es utilizada, por ejemplo, en los vehículos autómatas que se usan como mozos de almacén para recoger paquetes y reunirlos. Otro ejemplo que hemos visto en nuestra propia ciudad es la de un camarero autómata que se encarga de dar servicio entregando comida y bebida en una bandeja que porta. Estos vehículos siguen una cuadrícula precalculada que restringe el movimiento de estos. Nuestro cometido es diseñar vehículos de mantenimiento que se encarguen de detectar obstáculos en estas vías, informar a un servidor y. en última instancia, en caso de tener la funcionalidad adecuada implementada, retirar el objeto de la vía para dejarla libre.
Comenzaremos a desarrollar funcionalidades básicas, como: si, mientras que el vehículo es conducido, detecta algún obstáculo, este se detiene e informa al servidor de la incidencia. Se irá ampliando el proyecto a medida que vayamos completando requerimientos. De esta manera, el proyecto será entregable en varios momentos de su desarrollo (al final de cada iteración del desarrollo de sus funcionalidades) por si no se pudiese llegar a la idea final del proyecto.
En la primera iteración de nuestro proyecto (con fecha límite la fecha de la segunda iteración según el plan de la asignatura), ya que la “primera iteración” según la asignatura no es más que una presentación de ideas tenemos los siguientes requisitos en orden a cumplir:	

- Crear el servidor
- Se establece conexión con el servidor para recolectar o enviar datos
- El vehículo puede moverse (mediante comandos o algún control)
- El vehículo puede detectar obstáculos frente a sí mismo.

# Links

Chasis tanque - [Chasis de tanque Robot](https://es.aliexpress.com/item/4000449355642.html?spm=a2g0s.8937460.0.0.1b842e0enKKRRU)

Placas de desarrollo - [Yizhet NodeMCU Lua Lolin V3](https://www.amazon.es/Yizhet-NodeMCU-ESP8266-Desarrollo-Internet/dp/B07XJWK5F4/ref=sr_1_4?__mk_es_ES=ÅMÅŽÕÑ&keywords=esp8266&qid=1581681743&sr=8-4)

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

Este canal se usa para asignar a los drones una ruta a seguir, de forma que se les enviará un Json dirigido a un dron en específico con el siguiente contenido:

~~~json
{"idDron": Dron al que se le asigna la ruta, "idRuta": Identificador de la ruta que se encuentra en la base de datos y la cual se asignará al dron, "Path": Ruta asignada al robot}
~~~





