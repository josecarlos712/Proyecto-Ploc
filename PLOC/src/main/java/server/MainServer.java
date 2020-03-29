package server;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;

public class MainServer extends AbstractVerticle{
	
	@Override
	public void start(Promise<Void> startPromise) {
			
//		vertx.createHttpServer().requestHandler(r -> {
//			
////			r.response().sendFile("prueba.html");
//			r.response().end("<h1>Bienvenido al servidor de pruebas</h1> Si esta viendo esto, es porque pertenece al proyecto PLOC, en caso"
//					+ "contrario, vuelvase por donde ha venido :D");
//			
//		}).listen(8085, result -> {
//			if(result.succeeded()) {
//				startPromise.complete();
//			}else {
//				startPromise.fail(result.cause());
//			}
//		});
		
		vertx.deployVerticle(DataBaseVerticle.class.getName());
		
	}

}
