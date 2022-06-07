# Network_attacks_detection_system
System for alerts generation when network attacks are detected.

## Requirements
- 2 VMs with Ubuntu
- 1 VM with Kali Linux
- Java 11
- Maven 3.6.3
- Flink 1.15.0
- Kafka
- Honeypot Cowrie

## Documentation
[doc](https://demo.hedgedoc.org/wWOoHAO3StmwlTl-1lvZyw)

## Configuration

## Flink Application
Build application in Maven:
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

Stop Flink cluster:
```
./flink-1.15.0/bin/stop-cluster.sh
```
