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
            return value;
        }
    }

    private long honeypotId;
    private String timestamp;
    private String attacker;
    private String command;
    private Level level;

    public Alert(long id, String tstamp, String attacker, String cmd, Level level)
    {
        this.honeypotId = id;
        this.timestamp = tstamp;
        this.attacker = attacker;
        this.command = cmd;
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

    public String getTimestamp()
    {
        return timestamp;
    }

    public void setTimestamp(String timestamp)
    {
        this.timestamp = timestamp;
    }

    public String getAttacker()
    {
        return attacker;
    }

    public void setAttacker(String attacker)
    {
        this.attacker = attacker;
    }

    public String getCommand()
    {
        return command;
    }

    public void setCommand(String command)
    {
        this.command = command;
    }

    public Level getLevel()
    {
        return level;
    }

    public int getLevelValue()
    {
        return level.getValue();
    }

    public String getLevelString()
    {
        return level.name();
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
                && timestamp.equals(that.timestamp)
                && attacker.equals(that.attacker)
                && command.equals(that.command)
                && level == that.level;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(honeypotId, timestamp, attacker, command, level);
    }

    @Override
    public String toString()
    {
        return "Alert{"
                + "honeypotId=" + honeypotId
                + ", timestamp=" + timestamp
                + ", attacker=" + attacker
                + ", command=" + command
                + ", level=" + level
                + '}';
    }
}
