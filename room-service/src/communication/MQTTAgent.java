package communication;

import controller.Controller;
import io.vertx.core.AbstractVerticle;
import io.vertx.mqtt.MqttClient;

/*
 * MQTT Agent
 */
public class MQTTAgent extends AbstractVerticle {

	private Controller controller;

	public MQTTAgent(Controller controller) {
		this.controller = controller;
	}

	@Override
	public void start() {
		MqttClient client = MqttClient.create(vertx);

		client.connect(1883, "broker.hivemq.com", c -> {
			log("connected");
			log("subscribing...");
			client.publishHandler(s -> {
				log(s.payload().toString());
				String[] msgArray = s.payload().toString().split(" & ");
				String motionValue = msgArray[0].split(": ")[1];
				int luminosityValue = Integer.parseInt(msgArray[1].split(": ")[1]);
				controller.updateRoom(motionValue, luminosityValue);
			})
			.subscribe("ESP32-IoT-2023", 2);
		});
	}

	private void log(String msg) {
		System.out.println("[MQTT AGENT] "+msg);
	}
}