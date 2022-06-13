# Network_attacks_detection_system
System for alerts generation when network attacks are detected.

## Requirements
- 3 VMs with Ubuntu (one with Flink application)
- 1 VM with Kali Linux
- Java 11
- Maven 3.6.3
- Flink 1.15.0
- Kafka
- Honeypot Cowrie
- MongoDB 4.4

## Documentation
[doc](https://demo.hedgedoc.org/wWOoHAO3StmwlTl-1lvZyw)

## Configuration
1. Install MongoDB
```
sudo apt-get install gnupg
wget -qO - https://www.mongodb.org/static/pgp/server-4.4.asc | sudo apt-key add -
echo "deb [ arch=amd64,arm64 ] https://repo.mongodb.org/apt/ubuntu focal/mongodb-org/4.4 multiverse" | sudo tee /etc/apt/sources.list.d/mongodb-org-4.4.list
sudo apt-get update
sudo apt-get install -y mongodb-org
```

When the last command returned error related to _libssl1.0_:
```
echo "deb http://security.ubuntu.com/ubuntu impish-security main" | sudo tee /etc/apt/sources.list.d/impish-security.list
sudo apt-get update
sudo apt-get install libssl1.0
sudo apt-get install -y mongodb-org
```

Start MongoDB daemon and client:
```
sudo systemctl start mongod
mongo
```

Within Mongo client:
```
show dbs
```

To show all alerts in Mongo:
```
use alertsDB
db.alerts.find()
```

## Flink Application
Build application:
```
mvn package
```

Start Flink cluster:
```
./flink-1.15.0/bin/start-cluster.sh
```

Execute application in Flink:
```
./flink-1.15.0/bin/flink run Network_attacks_detection_system/target/attackdetection-0.1.jar
```

Read Flink logs (and attack using Kali VM):
```
tail -f flink-1.15.0/log/flink-psd-taskexecutor-0-psd-VirtualBox.log
```

Stop Flink cluster:
```
./flink-1.15.0/bin/stop-cluster.sh
```
