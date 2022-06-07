package spendreport;

import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;
import org.apache.flink.walkthrough.common.entity.Alert;


public class AttackDetector extends KeyedProcessFunction<Long, HoneypotLog, Alert>
{
	private static final long serialVersionUID = 1L;

	@Override
	public void processElement(HoneypotLog honeypotLog, Context context, Collector<Alert> collector) throws Exception
	{
		Alert alert = new Alert();
		alert.setId(honeypotLog.getHoneypotId());

		collector.collect(alert);
	}
}
