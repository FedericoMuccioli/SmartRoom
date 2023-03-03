package communication;

import io.vertx.core.AbstractVerticle;
import io.vertx.mqtt.MqttClient;
import logic.logic;

/*
 * MQTT Agent
 */
public class MQTTAgent extends AbstractVerticle {

	private logic logic;

	public MQTTAgent(logic logic) {
		this.logic = logic;
	}
	

	@Override
	public void start() {
		MqttClient client = MqttClient.create(vertx);

		client.connect(1883, "broker.hivemq.com", c -> {
			log("connected");
			log("subscribing...");
			client.publishHandler(s -> {
				log(s.payload().toString());
				logic.UpdateLogic(s.payload().toString());
			})
			.subscribe("ESP32-IoT-2023", 2);
		});
	}

	private void log(String msg) {
		System.out.println("[MQTT AGENT] "+msg);
	}
}