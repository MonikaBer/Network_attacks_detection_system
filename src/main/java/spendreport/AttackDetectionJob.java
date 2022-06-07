package spendreport;

import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.walkthrough.common.sink.AlertSink;
import org.apache.flink.walkthrough.common.entity.Alert;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.util.serialization.SimpleStringSchema;
import java.util.Properties;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoDatabase;


public class AttackDetectionJob
{
	final static String topic = "cowrie";
    final static String kafkaAddress = "localhost:9092";
	final static String consumerGroup = "abc";

	public static void main(String[] args) throws Exception
	{
		StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

		FlinkKafkaConsumer<String> kafkaConsumer = createStringConsumerForTopic(topic, kafkaAddress, consumerGroup);

		DataStream<HoneypotLog> cowrieLogs = env
			.addSource(kafkaConsumer)
			.map(x -> new HoneypotLog(1, x))
			.name("cowrie-logs");

		DataStream<Alert> alerts = cowrieLogs
			.keyBy(HoneypotLog::getHoneypotId)
			.process(new AttackDetector())
			.name("attack-detector");

		MongoClient dbClient = new MongoClient();

		// create Mongo credentials
		MongoCredential credential;
		credential = MongoCredential.createCredential("mongoUser", "alertsDB", "password".toCharArray());
		System.out.println("Connected to DB successfully");

		// access DB
		MongoDatabase db = dbClient.getDatabase("alertsDB");
		System.out.println("Credentials ::" + credential);

		try {
			// create collection with alerts if not exists
			db.createCollection("alerts");
			System.out.println("Collection 'alerts' created successfully");
		}
		catch(Exception e) {
			System.out.println("Collection 'alerts' already exists");
		}


		alerts
			.addSink(new AlertSink())
			.name("send-alerts");

		env.execute("Attacks Detection");
	}

	public static FlinkKafkaConsumer<String> createStringConsumerForTopic(String topic, String kafkaAddress, String kafkaGroup)
	{
		Properties props = new Properties();
		props.setProperty("bootstrap.servers", kafkaAddress);
		FlinkKafkaConsumer<String> consumer = new FlinkKafkaConsumer<>(topic, new SimpleStringSchema(), props);

		return consumer;
	}
}
