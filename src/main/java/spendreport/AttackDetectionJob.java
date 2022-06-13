package spendreport;

import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.util.serialization.SimpleStringSchema;
import java.util.Properties;

public class AttackDetectionJob
{
	final static String topic_1 = "cowrie_1";
	final static String topic_2 = "cowrie_2";
	final static String topic_3 = "cowrie_3";
    final static String kafkaAddress_1 = "172.21.0.1";
	final static String kafkaAddress_2 = "172.20.0.1";
	final static String kafkaAddress_3 = "172.19.0.1";
	final static String consumerGroup_1 = "1";
	final static String consumerGroup_2 = "2";
	final static String consumerGroup_3 = "3";

	public static void main(String[] args) throws Exception
	{
		StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

		FlinkKafkaConsumer<String> kafkaConsumer_1 = createStringConsumerForTopic(topic_1, kafkaAddress_1, consumerGroup_1);
		FlinkKafkaConsumer<String> kafkaConsumer_2 = createStringConsumerForTopic(topic_2, kafkaAddress_2, consumerGroup_2);
		FlinkKafkaConsumer<String> kafkaConsumer_3 = createStringConsumerForTopic(topic_3, kafkaAddress_3, consumerGroup_3);

		DataStream<HoneypotLog> cowrieLogs_1 = env
			.addSource(kafkaConsumer_1)
			.map(x -> new HoneypotLog(1, x))
			.name("cowrie-logs-1");

		DataStream<HoneypotLog> cowrieLogs_2 = env
			.addSource(kafkaConsumer_2)
			.map(x -> new HoneypotLog(2, x))
			.name("cowrie-logs-2");

		DataStream<HoneypotLog> cowrieLogs_3 = env
			.addSource(kafkaConsumer_3)
			.map(x -> new HoneypotLog(3, x))
			.name("cowrie-logs-3");

		DataStream<Alert> alerts_1 = cowrieLogs_1
			.keyBy(HoneypotLog::getHoneypotId)
			.process(new AttackDetector())
			.name("attack-detector-1");

		DataStream<Alert> alerts_2 = cowrieLogs_2
			.keyBy(HoneypotLog::getHoneypotId)
			.process(new AttackDetector())
			.name("attack-detector-2");

		DataStream<Alert> alerts_3 = cowrieLogs_3
			.keyBy(HoneypotLog::getHoneypotId)
			.process(new AttackDetector())
			.name("attack-detector-3");

		alerts_1
			.addSink(new AlertSink())
			.name("send-alerts-1");

		alerts_2
			.addSink(new AlertSink())
			.name("send-alerts-2");

		alerts_3
			.addSink(new AlertSink())
			.name("send-alerts-3");

		env.execute("Attacks Detection");
	}

	public static FlinkKafkaConsumer<String> createStringConsumerForTopic(String topic, String kafkaAddress, String kafkaGroup)
	{
		Properties props = new Properties();
		//props.setProperty(Constants.TOPIC, topic);
		//props.setProperty("zookeeper.connect", kafkaAddress + ":2181");
		props.setProperty("group.id", kafkaGroup);
		props.setProperty("bootstrap.servers", kafkaAddress + ":9092");

		FlinkKafkaConsumer<String> consumer = new FlinkKafkaConsumer<>(topic, new SimpleStringSchema(), props);
		return consumer;
	}
}
