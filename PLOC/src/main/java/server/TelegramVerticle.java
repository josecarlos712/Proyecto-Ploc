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
	private boolean resolvingRequest = false;
	
	@Override
	public void start(Promise<Void> future) {
		TelegramOptions options = new TelegramOptions().setBotName("PlocBot").setBotToken("token");
		
		bot = TelegramBot.create(vertx, options).receiver(new LongPollingReceiver().onUpdate(handler -> {
			
			//-------------------------------------------------COMANDO HOLA----------------------------------------------------
			if(handler.getMessage().getText().toLowerCase().contains("hola")) {
				System.out.println("Mensaje recibido con exito :D");
				bot.sendMessage(new SendMessage().setText("Hola " + handler.getMessage().getFrom().getFirstName() + " bienvenido, soy el bot que ha creado Placix5 :D\n\n"
						+ "Se me ha encomendado la tarea de informar sobre el estado del proyecto PLOC hasta que se encuentre funcional, "
						+ "cuando eso ocurra podré ofrecer información sobre los drones, las rutas y los sensores :D\n\n"
						+ "De momento, puedes preguntar sobre el estado de las siguientes cosas:\n\n"
						+ " - GitHub del proyecto\n"
						+ " - Prototipo hardware\n"
						+ " - Base de datos\n"
						+ " - API REST\n"
						+ " - MQTT").setChatId(handler.getMessage().getChatId()));
			}else 
			
				//-------------------------------------------------COMANDO TIEMPO----------------------------------------------------	
				
			if (handler.getMessage().getText().toLowerCase().contains("github")){			
				
				bot.sendMessage(new SendMessage().setText("El enlace a GitHub de nuestro proyecto es el siguiente: \n\n"
						+ "https://github.com/josecarlos712/Proyecto-Ploc\n\n"
						+ "Esperamos que disfrutes viendo el código :D").setChatId(handler.getMessage().getChatId()));
				
			}
			
			if (handler.getMessage().getText().toLowerCase().contains("prototipo")){			
				
				bot.sendMessage(new SendMessage().setText("Nuestro prototipo hardware aún está en desarrollo, aunque ahora mismo el "
						+ "desarrollo se encuentra paralizado porque no disponemos de los materiales necesarios :(\n\n"
						+ "Pero pronto retomaremos el desarrollo :D").setChatId(handler.getMessage().getChatId()));
				
			}
			
			if (handler.getMessage().getText().toLowerCase().contains("base")){			
				
				bot.sendMessage(new SendMessage().setText("Nuestra base de datos está finalizada y es funcional, aunque estamos abiertos"
						+ " a cualquier posible ampliación que sea necesaria puesto que según vayamos avanzando en el proyecto puede que "
						+ "surjan imprevistos que no hayamos contemplado antes y haya que buscarles una solución :D\n\n"
						+ "En el estado actual, la base de datos cuenta con las siguientes tablas:\n"
						+ " - Drones --> En esta tabla se introduce la información correspondiente con cada uno de los drones :D\n"
						+ " - Rutas --> En esta tabla podemos encontrar las diferentes rutas que tenemos disponibles para que sigan los drones :D\n"
						+ " - Sensores --> En esta tabla encontramos los diferentes sensores registrados en el sistema :D\n"
						+ " - Valores --> En esta tabla podemos encontrar los valores que van recogiendo cada uno de los diferentes sensores que hay en la tabla sensores :D\n")
						.setChatId(handler.getMessage().getChatId()));
				
			}
			
			if (handler.getMessage().getText().toLowerCase().contains("api")){			
				
				bot.sendMessage(new SendMessage().setText("Nuestra API Rest es completamente funcional, si quieres probarla no tienes más "
						+ "que escribir el comando /request seguido de la tabla de la que quieres conocer la información, por ejemplo: "
						+ "/request sensores\n\n"
						+ "Esta petición será una petición de tipo GET ya que no podemos dejar que nos borres o nos cambies cosas de "
						+ "nuestra querida base de datos :D\n\n"
						+ "Te recordamos las diferentes tablas de las que puedes tener información:\n"
						+ " - Drones --> En esta tabla se introduce la información correspondiente con cada uno de los drones :D\n"
						+ " - Rutas --> En esta tabla podemos encontrar las diferentes rutas que tenemos disponibles para que sigan los drones :D\n"
						+ " - Sensores --> En esta tabla encontramos los diferentes sensores registrados en el sistema :D\n"
						+ " - Valores --> En esta tabla podemos encontrar los valores que van recogiendo cada uno de los diferentes sensores que hay en la tabla sensores :D\n")
						.setChatId(handler.getMessage().getChatId()));
				
			}
			
			if(handler.getMessage().getText().toLowerCase().contains("/request") || resolvingRequest) {
				
				String req = handler.getMessage().getText().toLowerCase();
				String s[] = req.split(" ");
				
				WebClient client = WebClient.create(vertx);
				client.get("10.202.89.250", "/api/" + s[1].toLowerCase()).send(ar -> {
						
					if(ar.succeeded()) {							
						HttpResponse<Buffer> response = ar.result();
						JsonArray body = response.bodyAsJsonArray();
						
						System.out.println("***************************************************************************");
						System.out.println(body);
						System.out.println("***************************************************************************");
						
						switch(s[1]) {
						case "sensores":
							
							bot.sendMessage(new SendMessage().setText("La información con respecto a los sensores que tenemos es: \n\n").setChatId(handler.getMessage().getChatId()));
							
							for(int i = 0; i<body.size(); i++) {
								JsonObject s1 = body.getJsonObject(i);
								bot.sendMessage(new SendMessage().setText("------------------------Sensor " + (i+1) + "------------------------\n"
										+ " - IDSensor: " + s1.getInteger("idSensor") + "\n"
										+ " - Tipo: " + s1.getString("tipo") + "\n"
										+ " - TiempoAct: " + s1.getInteger("tiempoAct") + "\n"
										+ "--------------------------------------------------------\n\n").setChatId(handler.getMessage().getChatId()));
								
							}							
							
							break;
						case "drones":
							
							bot.sendMessage(new SendMessage().setText("La información con respecto a los drones que tenemos es: \n\n").setChatId(handler.getMessage().getChatId()));
							
							for(int i = 0; i<body.size(); i++) {
								JsonObject s1 = body.getJsonObject(i);
								bot.sendMessage(new SendMessage().setText("------------------------Dron " + (i+1) + "------------------------\n"
										+ " - IDDron: " + s1.getInteger("idDron") + "\n"
										+ " - Estado: " + s1.getString("estado") + "\n"
										+ " - Peso soportado: " + s1.getInteger("pesoSoportado") + "\n"
										+ " - Bateria: " + s1.getInteger("bateria") + "\n"
										+ " - Parking Path: " + s1.getString("parkingPath") + "\n"
										+ " - IDSensor: " + s1.getInteger("idSensor") + "\n"
										+ " - IDRuta" + s1.getInteger("idRuta") + "\n"
										+ "------------------------------------------------------\n\n").setChatId(handler.getMessage().getChatId()));
								
							}	
							
							break;
						case "valores":
							
							bot.sendMessage(new SendMessage().setText("La información con respecto a los valores que tenemos es: \n\n").setChatId(handler.getMessage().getChatId()));
							
							for(int i = 0; i<body.size(); i++) {
								JsonObject s1 = body.getJsonObject(i);
								bot.sendMessage(new SendMessage().setText("------------------------Valor " + (i+1) + "------------------------\n"
										+ " - IDValor: " + s1.getInteger("idValor") + "\n"
										+ " - TimeStamp: " + s1.getLong("timestamp") + "\n"
										+ " - Obstaculo: " + s1.getBoolean("obs") + "\n"
										+ " - IDSensor: " + s1.getInteger("idSensor") + "\n"
										+ "-------------------------------------------------------\n\n").setChatId(handler.getMessage().getChatId()));
								
							}	
							
							break;
						case "rutas":
							
							bot.sendMessage(new SendMessage().setText("La información con respecto a las rutas que tenemos es: \n\n").setChatId(handler.getMessage().getChatId()));
							
							for(int i = 0; i<body.size(); i++) {
								JsonObject s1 = body.getJsonObject(i);
								bot.sendMessage(new SendMessage().setText("------------------------Rutas " + (i+1) + "------------------------\n"
										+ " - IDRuta: " + s1.getInteger("idRuta") + "\n"
										+ " - Tiempo Ruta: " + s1.getInteger("tiempoRuta") + "\n"
										+ " - Path: " + s1.getString("path") + "\n"
										+ "-------------------------------------------------------\n\n").setChatId(handler.getMessage().getChatId()));
								
							}	
							
							break;
						default:
							bot.sendMessage(new SendMessage().setText("No has introducido un valor valido :(").setChatId(handler.getMessage().getChatId()));
							
						}
							
					}else {
						bot.sendMessage(new SendMessage().setText("Vaya, algo ha salido mal :(").setChatId(handler.getMessage().getChatId()));
						System.out.println("***********************************************************************************************************");
						System.out.println(ar.cause());
						System.out.println("***********************************************************************************************************");
					}
				
				});
					
			}
			
			if (handler.getMessage().getText().toLowerCase().contains("mqtt")){			
				
				bot.sendMessage(new SendMessage().setText("Nuestro servidor mqtt está aún en desarrollo, en cuanto esté listo "
						+ "informaremos de los diferentes canales que tenemos disponibles y podrás subscribirte a ellos si "
						+ "así lo deseas :D").setChatId(handler.getMessage().getChatId()));
				
			}
		}));
		
		bot.start();
	}
	
}
