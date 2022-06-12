package spendreport;

import java.util.HashMap;

public class CommandsMap
{
    private HashMap<String, Alert.Level> cmdMap;

    public CommandsMap()
    {
        this.cmdMap = new HashMap<String, Alert.Level>();
        this.cmdMap.put("rm -rf / --no-preserve-root --one-file-system", Alert.Level.CRITICAL);
        this.cmdMap.put("rm -rf *", Alert.Level.CRITICAL);
        this.cmdMap.put("rm -R *", Alert.Level.CRITICAL);

        this.cmdMap.put("sudo su", Alert.Level.HIGH);
        this.cmdMap.put("su -", Alert.Level.HIGH);
        this.cmdMap.put("/etc/shadow", Alert.Level.HIGH);
        this.cmdMap.put("/etc/passwd", Alert.Level.HIGH);

        this.cmdMap.put("systemctl", Alert.Level.MEDIUM);
        this.cmdMap.put("netstat", Alert.Level.MEDIUM);
    }

    public Alert.Level getCmdLevel(String cmd)
    {
        for (String key : this.cmdMap.keySet()) {
            if (cmd.indexOf(key) != -1)
                return this.cmdMap.get(key);
        }
        return Alert.Level.LOW;
    }
}
