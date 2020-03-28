package server;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;

public class MainServer extends AbstractVerticle{
	
	@SuppressWarnings("deprecation")
	@Override
	public void start(Future<Void> startFuture) {
			
		vertx.createHttpServer().requestHandler(r -> {
			
//			r.response().sendFile("prueba.html");
			r.response().end("<h1>Bienvenido al servidor de pruebas</h1> Si esta viendo esto, es porque pertenece al proyecto PLOC, en caso"
					+ "contrario, vuelvase por donde ha venido :D");
			
			//Segunda prueba
			
		}).listen(8085, result -> {
			if(result.succeeded()) {
				startFuture.complete();
			}else {
				startFuture.fail(result.cause());
			}
		});
		
	}
	
	//Esta es la linea de Placix5

}
