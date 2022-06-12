package spendreport;

public final class IDSEngine
{
    private long honeypotId;
    private String log;
    private String timestamp;
    private String attacker;
    private String cmd;
    private Alert.Level level;

    public IDSEngine(long honeypotId, String log)
    {
        this.honeypotId = honeypotId;
        this.log = log;
        this.timestamp = null;
        this.attacker = null;
        this.cmd = null;
        this.level = null;
    }

    public Alert processLog()
    {
        if (this.log.indexOf("] Command found: ") != -1)
            processCmd();
        else if (this.log.indexOf("] New connection: ") != -1)
            processSSHConnect();
        else if (this.log.indexOf("] login attempt [b'root'/b'kali'] succeeded") != -1)
            processRootLogin();
        else if (this.log.indexOf("] Connection lost after") != -1)
            processSSHDisconnect();
        else
            return null;

        if (this.timestamp == null || this.attacker == null || this.cmd == null || this.level == null)
            return null;

		Alert alert = new Alert(this.honeypotId, this.timestamp, this.attacker, this.cmd, this.level);
        return alert;
    }

    private void processCmd()
    {
        // "2022-06-12T16:09:46+0000 [HoneyPotSSHTransport,9,172.18.0.5] Command found: ls"

        String pattern1 = new String("] Command found: ");
        int idx1 = this.log.indexOf(pattern1);
        this.cmd = this.log.substring(idx1 + pattern1.length());

        int idx2 = findAttacker(idx1, " [HoneyPotSSHTransport,");
        if (idx2 == -1)
            return;

        findTimestamp(idx2);

        this.level = Alert.Level.LOW;
        //set level
    }

    private void processSSHConnect()
    {
        // "2022-06-12T16:09:44+0000 [cowrie.ssh.factory.CowrieSSHFactory] New connection: 172.18.0.5:43516 (172.21.0.2:2222) [session: 1576158385b1]"

        String pattern1 = new String("] New connection: ");
        String pattern2 = new String(") [session:");
        int idx1 = this.log.indexOf(pattern1);
        int idx2 = this.log.indexOf(pattern2);
        if (idx2 < idx1)
            return;

        String substr = this.log.substring(idx1 + pattern1.length(), idx2);  //172.18.0.5:43516 (172.21.0.2:2222
        pattern2 = new String(" (");
        idx2 = substr.indexOf(pattern2);
        String targetHost = substr.substring(idx2 + pattern2.length());
        this.cmd = "SSH connect to " + targetHost;
        this.attacker = substr.substring(0, idx2);

        pattern2 = new String(" [cowrie.ssh.factory.CowrieSSHFactory]");
        idx2 = this.log.indexOf(pattern2);

        findTimestamp(idx2);

        this.level = Alert.Level.HIGH;
    }

    private void processRootLogin()
    {
        // 2022-06-12T16:09:45+0000 [HoneyPotSSHTransport,9,172.18.0.5] login attempt [b'root'/b'kali'] succeeded

        String pattern1 = new String("] login attempt [b'root'/b'kali'] succeeded");
        int idx1 = this.log.indexOf(pattern1);
        this.cmd = "login attempt ['root'/'kali'] succeeded";

        int idx2 = findAttacker(idx1, " [HoneyPotSSHTransport,");
        if (idx2 == -1)
            return;

        findTimestamp(idx2);

        this.level = Alert.Level.CRITICAL;
    }

    private void processSSHDisconnect()
    {
        // "2022-06-12T16:12:45+0000 [HoneyPotSSHTransport,8,172.18.0.5] Connection lost after 181 seconds"

        String pattern1 = new String("] Connection lost after");
        int idx1 = this.log.indexOf(pattern1);
        this.cmd = "SSH connection lost";

        int idx2 = findAttacker(idx1, " [HoneyPotSSHTransport,");
        if (idx2 == -1)
            return;

        findTimestamp(idx2);

        this.level = Alert.Level.LOW;
    }

    private void findTimestamp(int idx)
    {
        this.timestamp = this.log.substring(0, idx);
    }

    private int findAttacker(int idx1, String pattern2)
    {
        int idx2 = this.log.indexOf(pattern2);
        if (idx1 == -1 || idx2 == -1 || idx2 > idx1)
            return -1;

        String substr = this.log.substring(idx2 + pattern2.length(), idx1);
        this.attacker = substr.substring(substr.indexOf(",") + 1);
        return idx2;
    }
}
