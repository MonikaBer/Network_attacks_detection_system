package spendreport;

import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.walkthrough.common.sink.AlertSink;
import org.apache.flink.walkthrough.common.entity.Alert;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.util.serialization.SimpleStringSchema;
import java.util.Properties;
// import org.apache.flink.walkthrough.common.entity.Transaction;
// import org.apache.flink.walkthrough.common.source.TransactionSource;

public class FraudDetectionJob
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
			.process(new FraudDetector())
			.name("attack-detector");

		// DataStream<Transaction> transactions = env
		// 	.addSource(new TransactionSource())
		// 	.name("transactions");
		// DataStream<Alert> alerts = transactions
		// 	.keyBy(Transaction::getAccountId)
		// 	.process(new FraudDetector())
		// 	.name("fraud-detector");

		alerts
			.addSink(new AlertSink())
			.name("send-alerts");

		env.execute("Attacks Detection");
	}

	public static FlinkKafkaConsumer<String> createStringConsumerForTopic(String topic, String kafkaAddress, String kafkaGroup)
	{
		Properties props = new Properties();
		props.setProperty("bootstrap.servers", kafkaAddress);
		// props.setProperty("group.id", kafkaGroup);
		FlinkKafkaConsumer<String> consumer = new FlinkKafkaConsumer<>(topic, new SimpleStringSchema(), props);

		return consumer;
	}
}
