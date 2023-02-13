# Relizc Network Protocol Information
This part will just simply introduce the basic annotations and data structure of different packets sent by the network managing system. This system uses a TCP socket that each server can communicate with each other by the management system
<br>  
### 0x01 Handshake
Handshaking is the first ever packet a server should sent to the managing system, and for registering properly. It could also be used to change a server's status from `LAUNCHING` to `RUNNING`.
|Field|Name|Data Type|Description|Example|
|---|-----|------------|------------|
|0|Packet ID|Byte|The packed ID that every packet should have|0x01|
