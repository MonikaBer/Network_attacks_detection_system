package spendreport;

import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;


public class AttackDetector extends KeyedProcessFunction<Long, HoneypotLog, Alert>
{
	private static final long serialVersionUID = 1L;

	@Override
	public void processElement(HoneypotLog honeypotLog, Context context, Collector<Alert> collector) throws Exception
	{
		IDSEngine idsEngine = new IDSEngine(honeypotLog.getHoneypotId(), honeypotLog.getContent());
		Alert alert = idsEngine.processLog();
		if (alert == null)
			return;

		System.out.println(alert.toString());

		AlertsDB alertsDB = new AlertsDB();
		alertsDB.insertAlert(alert);

		collector.collect(alert);
	}
}
