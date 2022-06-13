package spendreport;

import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.util.serialization.SimpleStringSchema;
import java.util.Properties;
import java.util.HashMap;
import java.lang.Integer;
import java.util.stream.IntStream;


public class AttackDetectionJob
{
	final static String[] topics = { "cowrie_1", "cowrie_2", "cowrie_3" };
	final static String[] kafkaAddresses = { "172.21.0.1", "172.20.0.1", "172.19.0.1" };
	final static String[] consumerGroups = { "1", "2", "3" };

	public static void main(String[] args) throws Exception
	{
		StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

		HashMap<Integer, FlinkKafkaConsumer<String>> consumers = new HashMap<Integer, FlinkKafkaConsumer<String>>();
		IntStream.range(0, 3).forEachOrdered(x -> {
			consumers.put(x + 1, createStringConsumerForTopic(topics[x], kafkaAddresses[x], consumerGroups[x]));
		});

		for (Integer nr : consumers.keySet()) {
			addStream(env, consumers.get(nr), nr);
		}

		env.execute("Attacks Detection");
	}

	public static FlinkKafkaConsumer<String> createStringConsumerForTopic(String topic, String kafkaAddress, String kafkaGroup)
	{
		Properties props = new Properties();
		props.setProperty("group.id", kafkaGroup);
		props.setProperty("bootstrap.servers", kafkaAddress + ":9092");

		FlinkKafkaConsumer<String> consumer = new FlinkKafkaConsumer<>(topic, new SimpleStringSchema(), props);
		return consumer;
	}

	public static void addStream(StreamExecutionEnvironment env, FlinkKafkaConsumer<String> kafkaConsumer, Integer nr)
	{
		DataStream<HoneypotLog> cowrieLogs = env
			.addSource(kafkaConsumer)
			.map(x -> new HoneypotLog(nr, x))
			.name("cowrie-logs-" + nr.toString());

		DataStream<Alert> alerts = cowrieLogs
			.keyBy(HoneypotLog::getHoneypotId)
			.process(new AttackDetector())
			.name("attack-detector-" + nr.toString());

		alerts
			.addSink(new AlertSink())
			.name("send-alerts-" + nr.toString());
	}
}
