# Network_attacks_detection_system
System for alerts generation when network attacks are detected.

## Requirements
- 1 VM with Ubuntu
- 1 VM with Kali Linux

## Documentation
[final_doc](https://demo.hedgedoc.org/wWOoHAO3StmwlTl-1lvZyw)

## Configuration
- Ubuntu VM address: `3.72.193.9`
- Kali VM address: `3.73.103.223`

## System building steps
1. Create 2 instances (one for Ubuntu and one for Kali) in AWS and assign them public IPs.
2. Connect to VMs (at the level of .pem files):
```
ssh -i <key-name2>.pem ubuntu@3.72.193.9
ssh -i <key-name1>.pem kali@3.73.103.223
```
3.
