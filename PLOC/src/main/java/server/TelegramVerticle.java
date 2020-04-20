package server;

import org.schors.vertx.telegram.bot.LongPollingReceiver;
import org.schors.vertx.telegram.bot.TelegramBot;
import org.schors.vertx.telegram.bot.TelegramOptions;
import org.schors.vertx.telegram.bot.api.methods.SendMessage;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;

public class TelegramVerticle extends AbstractVerticle {

	private TelegramBot bot;
	private boolean resolvingWeather = false;
	private boolean resolvingFilm = false;
	
	@Override
	public void start(Promise<Void> future) {
		TelegramOptions options = new TelegramOptions().setBotName("PlocBot").setBotToken("token");
		
		bot = TelegramBot.create(vertx, options).receiver(new LongPollingReceiver().onUpdate(handler -> {
			
			//-------------------------------------------------COMANDO HOLA----------------------------------------------------
			if(handler.getMessage().getText().toLowerCase().contains("hola")) {
				System.out.println("Mensaje recibido con exito :D");
				bot.sendMessage(new SendMessage().setText("Hola " + handler.getMessage().getFrom().getFirstName() + " bienvenido, soy el bot que ha creado Placix5 :D\n\n"
						+ "Mi creador ha querido que me parezca a él, así que tengo que deciros: OSTIA LOCO QUE GUAAAAAPO\n\n" 
						+ "Acto seguido, me gustaría enunciar las funciones que tengo disponibles para que las probéis y recordad que no soy Placi, si queréis hablar "
						+ "con el tendréis que escribirle a él directamente.\nLas funciones son:\n\n- Tiempo\n- Pelis de Studio Ghibli\n\n :D").setChatId(handler.getMessage().getChatId()));
			}else 
			
				//-------------------------------------------------COMANDO TIEMPO----------------------------------------------------	
				
			if (handler.getMessage().getText().toLowerCase().contains("tiempo") || resolvingWeather){			
				
				if(resolvingWeather) {
					String city = handler.getMessage().getText();
					
					WebClient client = WebClient.create(vertx);
					client.get("api.openweathermap.org", "/data/2.5/weather?q="+city+"&APPID=77831608c4509c8256a22e8d43e0b54b&units=metric").send(ar ->{
					//	PROCESAMIENTO DE LA RESPUESTA
						if(ar.succeeded()) {
							HttpResponse<Buffer> response = ar.result();
							JsonObject body = response.bodyAsJsonObject();
						
							Float temp = body.getJsonObject("main").getFloat("temp");
							Float press = body.getJsonObject("main").getFloat("pressure");
							Float hum = body.getJsonObject("main").getFloat("humidity");
							
							String princ = body.getJsonArray("weather").getJsonObject(0).getString("main");
						
							bot.sendMessage(new SendMessage().setText("El tiempo en " + city + " esta principalmente " + princ +", aquí puedes ver otra información relevante:"
									+ "\n\n"
									+ "- Temperatura: " + temp + " ºC\n- Presión: " + press + " mmHg\n- Humedad: " + hum + " %")
									.setChatId(handler.getMessage().getChatId()));
						}else {
							bot.sendMessage(new SendMessage().setText("Vaya, algo ha salido mal :(").setChatId(handler.getMessage().getChatId()));
						}
					});
					
					resolvingWeather = false;
				}else {
					bot.sendMessage(new SendMessage().setText("Estupendo :D\nIntroduce el nombre de la ciudad en la que quieres conocer el tiempo")
							.setChatId(handler.getMessage().getChatId()));
					resolvingWeather = true;
				}
				
			}
			
			if(handler.getMessage().getText().toLowerCase().contains("peli") || resolvingFilm) {
				
				if(resolvingFilm) {
					String film = handler.getMessage().getText().toLowerCase();
					
					WebClient client = WebClient.create(vertx);
					client.get("ghibliapi.herokuapp.com", "/films").send(ar -> {
						
						if(ar.succeeded()) {							
							HttpResponse<Buffer> response = ar.result();
							JsonArray filmLs = response.bodyAsJsonArray();
						
							int pos = 0;
							for(int i = 0; i < filmLs.size(); i++) {
								
								JsonObject f = filmLs.getJsonObject(i);
								if(f.getString("title").toLowerCase().equals(film)) {
									pos = i;
									break;
								}
								
							}
							
							JsonObject finalFilm = filmLs.getJsonObject(pos);
							String title = finalFilm.getString("title");
							String description = finalFilm.getString("description");
							String director = finalFilm.getString("director");
							String producer = finalFilm.getString("producer");
							String releaseDate = finalFilm.getString("release_date");
							
							bot.sendMessage(new SendMessage().setText("Título: " + title + "\n"
									+ "Director: " + director + "\n"
									+ "Productor: " + producer + "\n"
									+ "Año de lanzamiento: " + releaseDate + "\n\n"
									+ "Descripción: " + description ).setChatId(handler.getMessage().getChatId()));
							
							
						}else {
							bot.sendMessage(new SendMessage().setText("Vaya, algo ha salido mal :(").setChatId(handler.getMessage().getChatId()));
						}
					
					});
					resolvingFilm = false;
				}else {
					bot.sendMessage(new SendMessage().setText("Estupendo :D\nIntroduce el nombre de la pelicula en ingles de la cual quieres obtener información")
							.setChatId(handler.getMessage().getChatId()));
					resolvingFilm = true;
				}
				
			}
		}));
		
		bot.start();
	}
	
}
