package server;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.mysqlclient.MySQLConnectOptions;
import io.vertx.mysqlclient.MySQLPool;
import io.vertx.sqlclient.PoolOptions;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import types.Dron;
import types.Ruta;
import types.Sensor;
import types.Valor;

public class DataBaseVerticle extends AbstractVerticle{
	
	private MySQLPool dataBase;
	
	@Override
	public void start(Promise<Void> startPromise) {
		
		MySQLConnectOptions connectOption = new MySQLConnectOptions().setPort(3306).setHost("localhost")
				.setDatabase("proyecto_ploc").setUser("root").setPassword("toor");
		PoolOptions poolOption = new PoolOptions().setMaxSize(5);
		
		dataBase = MySQLPool.pool(vertx, connectOption, poolOption); 
		
		Router router = Router.router(vertx);
		vertx.createHttpServer().requestHandler(router::handle).listen(80, result -> {
			if(result.succeeded()) {
				startPromise.complete();
			}else {
				startPromise.fail(result.cause());
			}
		});
		
		//-----------------------------------------------------------------TABLA DRONES------------------------------------------------------------
		
		router.route("/api/drones").handler(BodyHandler.create());
		router.route("/api/drones/*").handler(BodyHandler.create());
		
		router.get("/api/drones").handler(this::getAllDrones);
		router.get("/api/drones/:idDron").handler(this::getDroneByDroneID);
		router.post("/api/drones").handler(this::postDrone);
		router.put("/api/drones/:idDron").handler(this::putDrone);
		router.delete("/api/drones/:idDron").handler(this::deleteDroneByDroneID);
		
		//-----------------------------------------------------------------TABLA SENSORES------------------------------------------------------------
		
		router.route("/api/sensores").handler(BodyHandler.create());
		router.route("/api/sensores/*").handler(BodyHandler.create());
		
		router.get("/api/sensores").handler(this::getAllSensors);
		router.get("/api/sensores/:idSensor").handler(this::getSensorBySensorID);
		router.post("/api/sensores").handler(this::postSensor);
		router.put("/api/sensores/:idSensor").handler(this::putSensor);
		router.delete("/api/sensores/:idSensor").handler(this::deleteSensorBySensorID);
		
		//-----------------------------------------------------------------TABLA VALORES------------------------------------------------------------
		
		router.route("/api/valores").handler(BodyHandler.create());
		router.route("/api/valores/*").handler(BodyHandler.create());
		
		router.get("/api/valores").handler(this::getAllValues);
		router.get("/api/valores/:idSensor").handler(this::getValuesBySensorID);
		router.post("/api/valores").handler(this::postValue);
		router.delete("/api/valores/:idSensor").handler(this::deleteValuesBySensorID);
		
		//-----------------------------------------------------------------TABLA RUTAS------------------------------------------------------------
				
		router.route("/api/rutas").handler(BodyHandler.create());
		router.route("/api/rutas/*").handler(BodyHandler.create());
		
		router.get("/api/rutas").handler(this::getAllPaths);
		router.get("/api/rutas/:idRuta").handler(this::getPathByPathID);
		router.post("/api/rutas").handler(this::postPath);
		router.put("/api/rutas/:idRuta").handler(this::putPath);
		router.delete("/api/rutas/:idRuta").handler(this::deletePathByPathID);
	}
	
	//-----------------------------------------------------------------TABLA DRONES------------------------------------------------------------
	
	private void getAllDrones(RoutingContext context) {
		
		dataBase.query("SELECT * FROM proyecto_ploc.drones", res -> {
			if(res.succeeded()) {
				RowSet<Row> resultSet = res.result();
				System.out.println("El n�mero de elementos obtenidos es: " + resultSet.size());
				
				JsonArray result = new JsonArray();
				for(Row r : resultSet) {
					result.add(JsonObject.mapFrom(new Dron(r.getInteger("ID_DRON"),r.getInteger("PESO_SOPORTADO"),r.getInteger("BATERIA"),
							r.getString("ESTADO"),r.getString("PARKING_PATH"),r.getInteger("ID_SENSOR"),r.getInteger("ID_RUTA"))));
				}
				context.response().setStatusCode(200).putHeader("content-type", "application/json").end(result.encodePrettily());
			}else {
				System.out.println("Algo salio mal :(\n");
				context.response().setStatusCode(401).putHeader("content-type", "application/json").end(JsonObject.mapFrom(res.cause()).encodePrettily());
			}
		});
		
	}
	
	private void getDroneByDroneID (RoutingContext context) {
		
		dataBase.query("SELECT * FROM proyecto_ploc.drones WHERE id_dron = " + context.request().getParam("idDron"), res -> {
			if(res.succeeded()) {
				RowSet<Row> resultSet = res.result();
				System.out.println("El n�mero de elementos obtenidos es: " + resultSet.size());
				
				JsonArray result = new JsonArray();
				for(Row r : resultSet) {
					result.add(JsonObject.mapFrom(new Dron(r.getInteger("ID_DRON"),r.getInteger("PESO_SOPORTADO"),r.getInteger("BATERIA"),
							r.getString("ESTADO"),r.getString("PARKING_PATH"),r.getInteger("ID_SENSOR"),r.getInteger("ID_RUTA"))));
				}
				context.response().setStatusCode(200).putHeader("content-type", "application/json").end(result.encodePrettily());
			}else {
				System.out.println("Algo salio mal :(\n");
				context.response().setStatusCode(401).putHeader("content-type", "application/json").end(JsonObject.mapFrom(res.cause()).encodePrettily());
			}
		});
		
	}
	
	private void postDrone(RoutingContext context) {		
		JsonObject body = context.getBodyAsJson();
		
		dataBase.query("INSERT INTO proyecto_ploc.drones (ID_DRON,PESO_SOPORTADO,ESTADO,PARKING_PATH,ID_SENSOR,ID_RUTA) values "
				+ "(" + body.getInteger("idDron") + "," + body.getInteger("pesoSoportado") + ",'" + body.getString("estado") + "','" 
		+ body.getString("parkingPath") + "'," + body.getInteger("idSensor") + "," + body.getInteger("idRuta") + ")", res -> {
			
			if(res.succeeded()) {
				System.out.println("Se ha a�adido la l�nea con �xito :D");
				context.response().setStatusCode(201).putHeader("content-type", "application/json").end(body.encodePrettily());
			}else {
				System.out.println("Algo ha salido mal :(");
				context.response().setStatusCode(401).putHeader("content-type", "application/json").end(JsonObject.mapFrom(res.cause()).encodePrettily());
			}
			
		});
		
	}
	
	private void putDrone(RoutingContext context) {
		
		JsonObject body = context.getBodyAsJson();
		
		dataBase.query("UPDATE proyecto_ploc.drones SET ID_DRON = " + body.getInteger("idDron") + ", PESO_SOPORTADO = " + body.getInteger("pesoSoportado") + 
				", BATERIA = " + body.getInteger("bateria") + ", ESTADO = '" + body.getString("estado") + "', PARKING_PATH = '" + body.getString("parkingPath") + 
				"', ID_SENSOR = " + body.getInteger("idSensor") + ", ID_RUTA = " + body.getInteger("idRuta") + " WHERE ID_DRON = " + context.request().getParam("idDron")
				, res -> {
			
			if(res.succeeded()) {
				System.out.println("Se ha actualizado con exito :D\n");
				context.response().setStatusCode(201).putHeader("content-type", "application/json").end(body.encodePrettily());
			}else {
				System.out.println("Algo ha salido mal :(");
				context.response().setStatusCode(401).putHeader("content-type", "application/json").end(JsonObject.mapFrom(res.cause()).encodePrettily());
			}
			
		});
		
	}
	
	private void deleteDroneByDroneID(RoutingContext context) {
		
		dataBase.query("DELETE FROM proyecto_ploc.drones WHERE id_dron = " + context.request().getParam("idDron"), res -> {
			if(res.succeeded()) {
				System.out.println("Entrada borrada con �xito :D");
				context.response().setStatusCode(201).putHeader("content-type", "application/json").end();
			}else {
				System.out.println("Algo salio mal :(\n");
				context.response().setStatusCode(401).putHeader("content-type", "application/json").end(JsonObject.mapFrom(res.cause()).encodePrettily());
			}
		});
	}

	
	//-----------------------------------------------------------------TABLA SENSORES------------------------------------------------------------
	
	private void getAllSensors(RoutingContext context) {
		
		dataBase.query("SELECT * FROM proyecto_ploc.sensores", res -> {
			if(res.succeeded()) {
				RowSet<Row> resultSet = res.result();
				System.out.println("El n�mero de elementos obtenidos es: " + resultSet.size());
				
				JsonArray result = new JsonArray();
				for(Row r : resultSet) {
					result.add(JsonObject.mapFrom(new Sensor(r.getInteger("ID_SENSOR"),r.getString("TIPO"),r.getInteger("TIEMPO_ACT"))));
				}
				context.response().setStatusCode(200).putHeader("content-type", "application/json").end(result.encodePrettily());
			}else {
				System.out.println("Algo salio mal :(\n");
				context.response().setStatusCode(401).putHeader("content-type", "application/json").end(JsonObject.mapFrom(res.cause()).encodePrettily());
			}
		});
		
	}
	
	private void getSensorBySensorID (RoutingContext context) {
		
		dataBase.query("SELECT * FROM proyecto_ploc.sensores WHERE id_sensor = " + context.request().getParam("idSensor"), res -> {
			if(res.succeeded()) {
				RowSet<Row> resultSet = res.result();
				System.out.println("El n�mero de elementos obtenidos es: " + resultSet.size());
				
				JsonArray result = new JsonArray();
				for(Row r : resultSet) {
					result.add(JsonObject.mapFrom(new Sensor(r.getInteger("ID_SENSOR"),r.getString("TIPO"),r.getInteger("TIEMPO_ACT"))));
				}
				context.response().setStatusCode(200).putHeader("content-type", "application/json").end(result.encodePrettily());
			}else {
				System.out.println("Algo salio mal :(\n");
				context.response().setStatusCode(401).putHeader("content-type", "application/json").end(JsonObject.mapFrom(res.cause()).encodePrettily());
			}
		});
		
	}
	
	private void postSensor(RoutingContext context) {		
		JsonObject body = context.getBodyAsJson();
		
		dataBase.query("INSERT INTO proyecto_ploc.sensores (ID_SENSOR,TIPO,TIEMPO_ACT) values "
				+ "(" + body.getInteger("idSensor") + ",'" + body.getString("tipo") + "'," + body.getInteger("tiempoAct") + ")", res -> {
			
			if(res.succeeded()) {
				System.out.println("Se ha a�adido la l�nea con �xito :D");
				context.response().setStatusCode(201).putHeader("content-type", "application/json").end(body.encodePrettily());
			}else {
				System.out.println("Algo ha salido mal :(");
				context.response().setStatusCode(401).putHeader("content-type", "application/json").end(JsonObject.mapFrom(res.cause()).encodePrettily());
			}
			
		});
		
	}
	
	private void putSensor(RoutingContext context) {
		
		JsonObject body = context.getBodyAsJson();
		
		dataBase.query("UPDATE proyecto_ploc.sensores SET ID_SENSOR = " + body.getInteger("idSensor") + ", TIPO = '" + body.getString("tipo") + 
				"', TIEMPO_ACT = " + body.getInteger("tiempoAct") + " WHERE ID_SENSOR = " + context.request().getParam("idSensor")
				, res -> {
			
			if(res.succeeded()) {
				System.out.println("Se ha actualizado con exito :D\n");
				context.response().setStatusCode(201).putHeader("content-type", "application/json").end(body.encodePrettily());
			}else {
				System.out.println("Algo ha salido mal :(");
				context.response().setStatusCode(401).putHeader("content-type", "application/json").end(JsonObject.mapFrom(res.cause()).encodePrettily());
			}
			
		});
		
	}
	
	private void deleteSensorBySensorID(RoutingContext context) {

		
		dataBase.query("DELETE FROM proyecto_ploc.sensores WHERE id_sensor = " + context.request().getParam("idSensor"), res -> {
			if(res.succeeded()) {
				System.out.println("Entrada borrada con �xito :D");
				context.response().setStatusCode(201).putHeader("content-type", "application/json").end();
			}else {
				System.out.println("Algo salio mal :(\n");
				context.response().setStatusCode(401).putHeader("content-type", "application/json").end(JsonObject.mapFrom(res.cause()).encodePrettily());
			}
		});
	}

	
	//-----------------------------------------------------------------TABLA VALORES------------------------------------------------------------

	private void getAllValues(RoutingContext context) {
		
		dataBase.query("SELECT * FROM proyecto_ploc.valores", res -> {
			if(res.succeeded()) {
				RowSet<Row> resultSet = res.result();
				System.out.println("El n�mero de elementos obtenidos es: " + resultSet.size());
				
				JsonArray result = new JsonArray();
				for(Row r : resultSet) {
					result.add(JsonObject.mapFrom(new Valor(r.getInteger("ID_VALOR"),r.getLong("TIMESTAMP"),r.getBoolean("OBS"),r.getInteger("ID_SENSOR"))));
				}
				context.response().setStatusCode(200).putHeader("content-type", "application/json").end(result.encodePrettily());
			}else {
				System.out.println("Algo salio mal :(\n");
				context.response().setStatusCode(401).putHeader("content-type", "application/json").end(JsonObject.mapFrom(res.cause()).encodePrettily());
			}
		});
		
	}

	private void getValuesBySensorID (RoutingContext context) {
		
		dataBase.query("SELECT * FROM proyecto_ploc.valores WHERE id_sensor = " + context.request().getParam("idSensor"), res -> {
			if(res.succeeded()) {
				RowSet<Row> resultSet = res.result();
				System.out.println("El n�mero de elementos obtenidos es: " + resultSet.size());
				
				JsonArray result = new JsonArray();
				for(Row r : resultSet) {
					result.add(JsonObject.mapFrom(new Valor(r.getInteger("ID_VALOR"),r.getLong("TIMESTAMP"),r.getBoolean("OBS"),r.getInteger("ID_SENSOR"))));
				}
				context.response().setStatusCode(200).putHeader("content-type", "application/json").end(result.encodePrettily());
			}else {
				System.out.println("Algo salio mal :(\n");
				context.response().setStatusCode(401).putHeader("content-type", "application/json").end(JsonObject.mapFrom(res.cause()).encodePrettily());
			}
		});
		
	}
	
	private void postValue(RoutingContext context) {		
		JsonObject body = context.getBodyAsJson();
		
		dataBase.query("INSERT INTO proyecto_ploc.valores (ID_VALOR,TIMESTAMP,OBS,ID_SENSOR) values "
				+ "(" + body.getInteger("idValor") + "," + body.getLong("timestamp") + "," + body.getBoolean("obs") + "," + body.getInteger("idSensor") + ")", res -> {
			
			if(res.succeeded()) {
				System.out.println("Se ha a�adido la l�nea con �xito :D");
				context.response().setStatusCode(201).putHeader("content-type", "application/json").end(body.encodePrettily());
			}else {
				System.out.println("Algo ha salido mal :(");
				context.response().setStatusCode(401).putHeader("content-type", "application/json").end(JsonObject.mapFrom(res.cause()).encodePrettily());
			}
			
		});
		
	}
	
	private void deleteValuesBySensorID(RoutingContext context) {
		
		dataBase.query("DELETE FROM proyecto_ploc.valores WHERE id_sensor = " + context.request().getParam("idSensor"), res -> {
			if(res.succeeded()) {
				System.out.println("Entrada borrada con �xito :D");
				context.response().setStatusCode(201).putHeader("content-type", "application/json").end();
			}else {
				System.out.println("Algo salio mal :(\n");
				context.response().setStatusCode(401).putHeader("content-type", "application/json").end(JsonObject.mapFrom(res.cause()).encodePrettily());
			}
		});
	}
	
	//-----------------------------------------------------------------TABLA RUTAS------------------------------------------------------------
	
	private void getAllPaths(RoutingContext context) {
		
		dataBase.query("SELECT * FROM proyecto_ploc.rutas", res -> {
			if(res.succeeded()) {
				RowSet<Row> resultSet = res.result();
				System.out.println("El n�mero de elementos obtenidos es: " + resultSet.size());
				
				JsonArray result = new JsonArray();
				for(Row r : resultSet) {
					result.add(JsonObject.mapFrom(new Ruta(r.getInteger("ID_RUTA"),r.getLong("TIEMPO_RUTA"),r.getString("PATH"))));
				}
				context.response().setStatusCode(200).putHeader("content-type", "application/json").end(result.encodePrettily());
			}else {
				System.out.println("Algo salio mal :(\n");
				context.response().setStatusCode(401).putHeader("content-type", "application/json").end(JsonObject.mapFrom(res.cause()).encodePrettily());
			}
		});
		
	}
	
	private void getPathByPathID (RoutingContext context) {
		
		dataBase.query("SELECT * FROM proyecto_ploc.rutas WHERE id_ruta = " + context.request().getParam("idRuta"), res -> {
			if(res.succeeded()) {
				RowSet<Row> resultSet = res.result();
				System.out.println("El n�mero de elementos obtenidos es: " + resultSet.size());
				
				JsonArray result = new JsonArray();
				for(Row r : resultSet) {
					result.add(JsonObject.mapFrom(new Ruta(r.getInteger("ID_RUTA"),r.getLong("TIEMPO_RUTA"),r.getString("PATH"))));
				}
				context.response().setStatusCode(200).putHeader("content-type", "application/json").end(result.encodePrettily());
			}else {
				System.out.println("Algo salio mal :(\n");
				context.response().setStatusCode(401).putHeader("content-type", "application/json").end(JsonObject.mapFrom(res.cause()).encodePrettily());
			}
		});
		
	}
	
	private void postPath(RoutingContext context) {		
		JsonObject body = context.getBodyAsJson();
		
		dataBase.query("INSERT INTO proyecto_ploc.rutas (ID_RUTA,PATH,TIEMPO_RUTA) values "
				+ "(" + body.getInteger("idRuta") + ",'" + body.getString("path") + "'," + body.getInteger("tiempoRuta") + ")", res -> {
			
			if(res.succeeded()) {
				System.out.println("Se ha a�adido la l�nea con �xito :D");
				context.response().setStatusCode(201).putHeader("content-type", "application/json").end(body.encodePrettily());
			}else {
				System.out.println("Algo ha salido mal :(");
				context.response().setStatusCode(401).putHeader("content-type", "application/json").end(JsonObject.mapFrom(res.cause()).encodePrettily());
			}
			
		});
		
	}
	
	private void putPath(RoutingContext context) {
		
		JsonObject body = context.getBodyAsJson();
		
		dataBase.query("UPDATE proyecto_ploc.rutas SET ID_RUTA = " + body.getInteger("idRuta") + ", PATH = '" + body.getString("path") + 
				"', TIEMPO_RUTA = " + body.getLong("tiempoRuta") + " WHERE ID_RUTA = " + context.request().getParam("idRuta")
				, res -> {
			
			if(res.succeeded()) {
				System.out.println("Se ha actualizado con exito :D\n");
				context.response().setStatusCode(201).putHeader("content-type", "application/json").end(body.encodePrettily());
			}else {
				System.out.println("Algo ha salido mal :(");
				context.response().setStatusCode(401).putHeader("content-type", "application/json").end(JsonObject.mapFrom(res.cause()).encodePrettily());
			}
			
		});
		
	}
	
	private void deletePathByPathID(RoutingContext context) {
		
		dataBase.query("DELETE FROM proyecto_ploc.rutas WHERE id_ruta = " + context.request().getParam("idRuta"), res -> {
			if(res.succeeded()) {
				System.out.println("Entrada borrada con �xito :D");
				context.response().setStatusCode(201).putHeader("content-type", "application/json").end();
			}else {
				System.out.println("Algo salio mal :(\n");
				context.response().setStatusCode(401).putHeader("content-type", "application/json").end(JsonObject.mapFrom(res.cause()).encodePrettily());
			}
		});
	}
}
