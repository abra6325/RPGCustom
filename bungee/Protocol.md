# Relizc Network Protocol Information
This part will just simply introduce the basic annotations and data structure of different packets sent by the network managing system. This system uses a TCP socket that each server can communicate with each other by the management system

---
### 0x01 Handshake
Handshaking is the first ever packet a server should sent to the managing system, and for registering properly. It could also be used to change a server's status from `LAUNCHING` to `RUNNING`.
| Field | Name | Data Type | Description | Example |
| --- | ----- | ------------ | ------------ | ------------ |
| 0 | Packet ID | Byte | The packed ID that every packet should have. | 0x01 |
| 1 | RAM Code | Byte Enum | The RAM Code of the server.<br><br>**0x00** `T` Tiny Server<br>**0x01** `S` Small Server<br>**0x02** `M` Medium Server<br>**0x03** `L` Large Server<br>**0x04** `G` Gigantic Server | 0x03 |
| 2 | Server Registery ID | String | This ID will be used to identify a server in the future | np3s |
| 3 | Server Name | String | The name of this server. **If the server is already initlized in the control panel, the new name will overlap its old name** | standard-1.8.8_Dungeon_Main_Lobby_snp3s_public |
| 4 | Server Type | String Enun | The server's type. Used to specify categories of avaliable servers | LOCAL_DUNGEON_SCENE_VIEWER |
