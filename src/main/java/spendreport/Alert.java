package spendreport;

import java.util.Objects;


/** Simple alert event */
@SuppressWarnings("unused")
public final class Alert
{
    public enum Level
    {
        LOW(0), MEDIUM(1), HIGH(2), CRITICAL(3);

        private final int value;

        private Level(int value)
        {
            this.value = value;
        }

        public int getValue()
        {
            return this.value;
        }
    }

    private long honeypotId;
    private long timestamp;
    private String content;
    private Level level;

    public Alert(HoneypotLog honeypotLog, Level level)
    {
        this.honeypotId = honeypotLog.getHoneypotId();
        this.timestamp = honeypotLog.getTimestamp();
        this.content = honeypotLog.getContent();
        this.level = level;
    }

    public long getHoneypotId()
    {
        return honeypotId;
    }

    public void setHoneypotId(long honeypotId)
    {
        this.honeypotId = honeypotId;
    }

    public long getTimestamp()
    {
        return timestamp;
    }

    public void setTimestamp(long timestamp)
    {
        this.timestamp = timestamp;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public Level getLevel()
    {
        return level;
    }

    public int getLevelValue()
    {
        return level.getValue();
    }

    public void setLevel(Level level)
    {
        this.level = level;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) {
            return true;
        } else if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Alert that = (Alert) o;
        return honeypotId == that.honeypotId
                && timestamp == that.timestamp
                && content.equals(that.content)
                && level == that.level;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(honeypotId, timestamp, content, level);
    }

    @Override
    public String toString()
    {
        return "Alert{"
                + "honeypotId=" + honeypotId
                + ", timestamp=" + timestamp
                + ", content=" + content
                + ", level=" + level
                + '}';
    }
}
