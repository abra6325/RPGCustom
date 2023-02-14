# Relizc Network Protocol Information
This part will just simply introduce the basic annotations and data structure of different packets sent by the network managing system. This system uses a TCP socket that each server can communicate with each other by the management system

## Data Types
Here are some common data types that will be used in the protocol. Every data is written or encoded in [Little-Endian](https://en.wikipedia.org/wiki/Endianness).
| Type | Bytes | Description |
| ---------- | ------- | ---------------------------- |
| Byte | 1 | The most basic component of the packet data. Only takes 1 byte, and the values range from 0 ~ 255. |
| Byte Enum | 1 | Basically a byte, but used to determine a enum in the protocol. **To read this data, use the same function as reading a byte `readByte()`** |
| Short | 2 | A number that ranges from 0 ~ 65535. The maximum writable short is 32767, but the maximum readable short is 65536.<br><br>**Reading a Short**:<br>To read a short, start by converting each byte to an integer. `ex. "\x08" to 8` Since every data is encoded in [Little-Endian](https://en.wikipedia.org/wiki/Endianness), read the 2 bytes by multiplying the first bit by 1 (16<sup>0</sup>) then the next byte by 16 (16<sup>1</sup>) |

## Packets
### `0x01` Handshake
Handshaking is the first ever packet a server should sent to the managing system, and for registering properly. It could also be used to change a server's status from `LAUNCHING` to `RUNNING`.
| Field | Name | Data Type | Description | Example |
| --- | ----- | ------------ | ------------ | ------------ |
| 0 | Packet ID | Byte | The packed ID that every packet should have. | 0x01 |
| 1 | RAM Code | Byte Enum | The RAM Code of the server.<br><br>**0x00** `T` Tiny Server<br>**0x01** `S` Small Server<br>**0x02** `M` Medium Server<br>**0x03** `B` Big Server<br>**0x04** `G` Gigantic Server | 0x03 |
| 2 | Server Version | String | The server version, basicailly the name of the server's template. | standard-1.8.8 |
| 3 | Server Registery ID | String | This ID will be used to identify a server in the future | np3s |
| 4 | Server Name | String | The name of this server. **If the server is already initlized in the control panel, the new name will overlap its old name** | standard-1.8.8_Dungeon_Main_Lobby_snp3s_public |
| 5 | Server Type | Nullable String Enum | The server's type. Used to specify categories of avaliable servers | LOCAL_DUNGEON_SCENE_VIEWER |
| 6 | Port | Short | Server Port. Used to verify packet integrity. | 11451 |

**Example Python Packet Data**:
b"\x01\x02\x0e\x00standard-1.8.8\x04\x00np3s\x00\x2estandard-1.8.8_Dungeon_Main_Lobby_snp3s_public\x00\x1aLOCAL_DUNGEON_SCENE_VIEWER\xbb\x2c"
