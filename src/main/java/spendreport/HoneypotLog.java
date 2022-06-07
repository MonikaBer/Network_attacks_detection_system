package spendreport;

import java.sql.Timestamp;
import java.util.Objects;


/** A simple log from honeypot */
@SuppressWarnings("unused")
public final class HoneypotLog
{

    private long honeypotId;

    private long timestamp;

    private String content;

    public HoneypotLog() {}

    public HoneypotLog(long honeypotId, String content)
    {
        this.honeypotId = honeypotId;
        this.timestamp = new Timestamp(System.currentTimeMillis()).getTime();
        this.content = content;
    }

    public HoneypotLog(long honeypotId, long timestamp, String content)
    {
        this.honeypotId = honeypotId;
        this.timestamp = timestamp;
        this.content = content;
    }

    public long getHoneypotId() {
        return honeypotId;
    }

    public void setHoneypotId(long honeypotId) {
        this.honeypotId = honeypotId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o == null || getClass() != o.getClass()) {
            return false;
        }
        HoneypotLog that = (HoneypotLog) o;
        return honeypotId == that.honeypotId
                && timestamp == that.timestamp
                && that.content.equals(content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(honeypotId, timestamp, content);
    }

    @Override
    public String toString() {
        return "HoneypotLog{"
                + "honeypotId="
                + honeypotId
                + ", timestamp="
                + timestamp
                + ", content="
                + content
                + '}';
    }
}
