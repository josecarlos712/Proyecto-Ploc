package server;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.mysqlclient.MySQLConnectOptions;
import io.vertx.mysqlclient.MySQLPool;
import io.vertx.sqlclient.PoolOptions;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import types.SensorProximidad;

public class DataBaseVerticle extends AbstractVerticle{
	
	private MySQLPool dataBase;
	
	@Override
	public void start(Promise<Void> startPromise) {
		
		MySQLConnectOptions connectOption = new MySQLConnectOptions().setPort(3306).setHost("localhost")
				.setDatabase("proyecto_ploc").setUser("root").setPassword("contraseña");
		PoolOptions poolOption = new PoolOptions().setMaxSize(5);
		
		dataBase = MySQLPool.pool(vertx, connectOption, poolOption); 
		
		Router router = Router.router(vertx);
		vertx.createHttpServer().requestHandler(router::handle).listen(8085, result -> {
			if(result.succeeded()) {
				startPromise.complete();
			}else {
				startPromise.fail(result.cause());
			}
		});
		
		//router.put("/api/sensores").handler(this::putValue);
		router.get("/api/sensores/:idSensor").handler(this::getSensorBySensorID);
		//router.delete("/api/sensores/:idSensor").handler(this::deleteAllBySensorID);
	}
	
	private void getSensorBySensorID (RoutingContext context) {
		
		dataBase.query("SELECT * FROM proyecto_ploc.sensores_proximidad WHERE id_sensor_prox = " + context.request().getParam("idSensor"), res -> {
			if(res.succeeded()) {
				RowSet<Row> resultSet = res.result();
				System.out.println("El número de elementos obtenidos es: " + resultSet.size());
				
				JsonArray result = new JsonArray();
				for(Row r : resultSet) {
					result.add(JsonObject.mapFrom(new SensorProximidad(r.getInteger("ID_SENSOR_PROX"),r.getLong("TIMESTAMP"),r.getBoolean("OBSTACULO"))));
				}
				context.response().setStatusCode(200).putHeader("content-type", "application/json").end(result.encodePrettily());
			}else {
				System.out.println("Algo salio mal :(");
				context.response().setStatusCode(401).putHeader("content-type", "application/json").end(JsonObject.mapFrom(res.cause()).encodePrettily());
			}
		});
		
	}

}
