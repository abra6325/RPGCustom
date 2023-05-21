# Relizc Network Protocol Information
This part will just simply introduce the basic annotations and data structure of different packets sent by the network managing system. This system uses a TCP socket that each server can communicate with each other by the management system

## Data Types
Here are some common data types that will be used in the protocol. Every data is written or encoded in [Little-Endian](https://en.wikipedia.org/wiki/Endianness).
| Type | Bytes | Description | Example | Type Byte |
| ---------- | --- | ---------------------------- | ----- | ----- |
| Byte | 1 | The most basic component of the packet data. Only takes 1 byte, and the values range from 0 ~ 255. | 0xf5 | 0x00
| Byte Enum | 1 | Basically a byte, but used to determine a enum in the protocol. **To read this data, use the same function as reading a byte `readByte()`** | 0x01 | 0x00 |
| Boolean | 1 | True, or false. Basically just a simple boolean value represented in bytes. | 0x00 → **false** | 0x08 |
| (Unsigned) Short | 2 | A number that ranges from 0 ~ 65,535. The maximum writable short is 32,767, but the maximum readable short is 65,536.<br><br>**Reading a Short**:<br>To read a short, start by converting each byte to an integer. `ex. "\x08" to 8` Since every data is encoded in [Little-Endian](https://en.wikipedia.org/wiki/Endianness), read the 2 bytes by multiplying the first bit by 1 (16<sup>0</sup>) then the next byte by 16 (16<sup>1</sup>) | 0x05 0x00 → **5** | 0x01 |
| Signed Short | 2 | A number that ranges from -32,768 ~ 32,767. This is just basically decreasing a unsigned short by 32768. | 0x05 0x00 → **-32763** | 0x02 |
| (Unsigned) Integer | 4 | A number that ranges from 0 ~ 4,294,967,296. It is basically just 4 bytes.<br><br>**Reading an Integer**:<br>The same as reading a short, except that you have to read 4 bytes instead of 2. | 0xff 0xff 0xff 0x7f → **4,294,967,296** | 0x03 |
| Signed Integer | 4 | A integer that is basically decreasing an unsigned integer by 2,147,483,648. | 0xff 0xff 0xff 0x7f → **2,147,483,647** | 0x04 |
| String | 2 ~ 65537 | A short specifying the length of the string, and the rest are the data of the string | 0x03 0x00 A B C → **"ABC"** | 0x05 |
| Type Array | 3 ~ (65535 * n) + 3 `where n is the type size` | An array that contains a specific type of data. The first byte specifies the type, and the next 2 bytes describes a short, which is the length of the array. | 0x01 0x02 0x00 0x01 0x00 0x01 0x00 → SHORT, SIZE 2, 1, 1 → **Short[1, 1]** | 0x06 |
| Mixed Array | 3 ~ ...Basically Infinite | An array that has different elements inside. The first 2 bytes specifies a short, which is the length of the array. After the 2 bytes, is the most complex data. The components of a data item are:<br><br>**Component 1**: `Byte` The data type.<br>**Component 2**: `Any` The data of that specific type.<br>**Component 3 (actually 0)**: The next data item.<br><br>This is only the content of 1 item in a mixed array. | 0x02 0x00 0x01 0x03 0x00 0x05 0x03 0x00 A B C → `Size:2, Items[{ByteSize:4, Short(3)}, {ByteSize:6, String("ABC")}]` → **`[3, "ABC"]`** | 0x07 |


## Client Packets
Packets that are send by the client.


### `0x01` Handshake
Handshaking is the first ever packet a server should sent to the managing system, and for registering properly. It could also be used to change a server's status from `LAUNCHING` to `RUNNING`.
| Field | Name | Data Type | Description | Example |
| --- | ----- | ------------ | ------------ | ------------ |
| 0 | Packet ID | Byte | The packet ID that every packet should have. | 0x01 |
| 1 | RAM Code | Byte Enum | The RAM Code of the server.<br><br>**0x00** `T` Tiny Server<br>**0x01** `S` Small Server<br>**0x02** `M` Medium Server<br>**0x03** `B` Big Server<br>**0x04** `G` Gigantic Server | 0x03 |
| 2 | Server Version | String | The server version, basicailly the name of the server's template. | standard-1.8.8 |
| 3 | Server Registery ID | String | This ID will be used to identify a server in the future | np3s |
| 4 | Server Name | String | The name of this server. **If the server is already initlized in the control panel, the new name will overlap its old name** | standard-1.8.8_Dungeon_Main_Lobby_snp3s_public |
| 5 | Server Type | Nullable String Enum | The server's type. Used to specify categories of avaliable servers | LOCAL_DUNGEON_SCENE_VIEWER |
| 6 | Port | Short | Server Port. Used to verify packet integrity. | 11451 |



**Example Python Packet Data**:
b"\x01\x02\x0e\x00standard-1.8.8\x04\x00np3s\x00\x2estandard-1.8.8_Dungeon_Main_Lobby_snp3s_public\x00\x1aLOCAL_DUNGEON_SCENE_VIEWER\xbb\x2c"

### `0xf0` Ping
This ping packet should be sent whenever as possible. This packet should also be put into a thread, since the managing system will pong back at most 1 seconds after this packet is sent. However, the managing system will send back the packet as soon as possible if there is an operation needed. All packets sent back by the managing system will be considered as [Server Packets](https://github.com/abra6325/RPGCustom/blob/master/bungee/Protocol.md#server-packets). The ping packet should also include update information of the server.
| Field | Name | Data Type | Description | Example |
| --- | ----- | ------------ | ------------ | ------------ |
| 0 | Packet ID | Byte | The packet ID that every packet should have. | 0xf0 |
| 1 | RAM Code | Byte Enum | The RAM Code of the server. Used to identify the server. | 0x03 |
| 2 | Server Registery ID | String | The server ID. Also used to identify the server. | np3s |
| 3 | Server Name | Nullable String | The name of this server. Any new name that is sent will replace the old server name, so if the server does not want to update its name, sent a null string. | standard-1.8.8_Dungeon_Main_Lobby_snp3s_public |
| 4 | Server TPS | Double | The current TPS of the server. | 114.514 |
| 5 | Server RAM Usage | Long | The amount of RAM used by the server in MB | 114514 |
| 6 | Server Players | Type Array of Mixed Array | An array of player data.<br><table>    <thead>        <tr>            <th>Data Type</th>            <th>Field</th>            <th>Name</th>            <th>Data Type</th>            <th>Description</th>        </tr>    </thead>    <tr>        <td rowspan="4">Mixed Array</td>        <td>0</td>        <td>Player Name</td>        <td>String</td>        <td>The player\'s IGN. All lowercase.</td>    </tr>    <tr>        <td>1</td>        <td>Has UUID</td>        <td>Boolean</td>        <td>Whether the player has an UUID or not. If this is true, the procotol will not skip the next field.</td>    </tr>    <tr>        <td>2</td>        <td>Player UUID</td>        <td>String</td>        <td>The player\'s UUID. This field will be present if field 1 is true. </td>    </tr>    <tr>        <td>3</td>        <td>Is Moderator</td>        <td>Boolean</td>        <td>Whether this player is a moderator or not. </td>    </tr></table>  |


### `0x83` Player Info
Request for a player's info. Their real name, their ip address, and others.
This is useful when fetching a player's real UUID, since uuids are random in offline mode servers.
| Field | Name | Data Type | Description | Example |
| --- | ----- | ------------ | ------------ | ------------ |
| 0 | Packet ID | Byte | The packet ID that every packet should have. | 0x01 |
| 1 | Player Name | String | The player's name | Relizc |

### `0xe0` BungeeCord Ping
It is the same ping packet as the previous one, except for this one is sent by the bungeecord plugin.
| Field | Name | Data Type | Description | Example |
| --- | ----- | ------------ | ------------ | ------------ |
| 0 | Packet ID | Byte | The packet ID that every packet should have. | 0xe0 |
| 1 | Player Amount | Short | The number of players | 11451 |

### `0xe1` BungeeCord Login Sucess
Yay bungeecord is ready.
| Field | Name | Data Type | Description | Example |
| --- | ----- | ------------ | ------------ | ------------ |
| 0 | Packet ID | Byte | The packet ID that every packet should have. | 0xe0 |

### `0xe2` Request Getting BungeeCord Servers
This packet is sent by the client when it needs to fetch all the server data. It will return a packet with all servers
| Field | Name | Data Type | Description | Example |
| --- | ----- | ------------ | ------------ | ------------ |
| 0 | Packet ID | Byte | The packet ID that every packet should have. | 0xe0 |

### `0xe9` Dynamic Server Request Connection
Send when sent. Sent when sending a player to another server.

| Field | Name | Data Type | Description | Example |
| --- | ----- | ------------ | ------------ | ------------ |
| 0 | Packet ID | Byte | The packet ID that every packet should have. | 0xe0 |
| 1 | RAM Code | Byte Enum | The RAM Code of the server. | 0x03 |
| 2 | Server Registery ID | String | The ID Code of the server | np3s |
| 3 | Target Server RAM Code | Byte Enum | The RAM Code of the target server. | 0x03 |
| 4 | Target Server Registery ID | String | The ID of the target server. | np3s |
| 5 | Target Player Name | String | The player's name | Relizc |

### `0xe8` Dynamic Server Request Connection by Category
Almost the same as the packet above, but this sent when a player start matching a game and the server is going to send it to a random server with the following category.

| Field | Name | Data Type | Description | Example |
| --- | ----- | ------------ | ------------ | ------------ |
| 0 | Packet ID | Byte | The packet ID that every packet should have. | 0xe0 |
| 1 | RAM Code | Byte Enum | The RAM Code of the server. | 0x03 |
| 2 | Server Registery ID | String | The ID Code of the server | np3s |
| 3 | Target Server Category Type | Byte Enum | The target server type | 0x03 |
| 4 | Target Server Sub-Category Type | Short Enum | The target server's sub category| np3s |
| 5 | Target Player Name | String | The player's name | Relizc |

### `0xa0` Alert Message
Make the protocol alert a message.
| Field | Name | Data Type | Description | Example |
| --- | ----- | ------------ | ------------ | ------------ |
| 0 | Packet ID | Byte | The packet ID that every packet should have. | 0xf0 |
| 1 | RAM Code | Byte Enum | The RAM Code of the server. Used to identify the server. | 0x03 |
| 2 | Server Registery ID | String | The server ID. Also used to identify the server. | np3s |
| 3 | Message Type | Byte Enum | The message type.<br><br>`0x00` Info<br>`0x01` Warning<br>`0x02` Error | 0x02 |
| 4 | Message | String | The message that you want to send. | Whats going on??? HEYYYA |

### `0xa1` Commit Log
Sends whenever a server log is made.
| Field | Name | Data Type | Description | Example |
| --- | ----- | ------------ | ------------ | ------------ |
| 0 | Packet ID | Byte | The packet ID that every packet should have. | 0xf0 |
| 1 | RAM Code | Byte Enum | The RAM Code of the server. Used to identify the server. | 0x03 |
| 2 | Server Registery ID | String | The server ID. Also used to identify the server. | np3s |
| 3 | Log Type | Byte Enum | The log type.<br><br>`0x00` Info<br>`0x01` Warning<br>`0x02` Error | 0x02 |
| 4 | Log | String | The log that you want to send. | Whats going on??? HEYYYA |


## Server Packets
Packets that are send by the managing system **only after a ping packet is sent by the client.**

### `0x83` Player Info
Request for a player's info. Their real name, their ip address, and others.
This is useful when fetching a player's real UUID, since uuids are random in offline mode servers.
| Field | Name | Data Type | Description | Example |
| --- | ----- | ------------ | ------------ | ------------ |
| 0 | Packet ID | Byte | The packet ID that every packet should have. | 0x01 |
| 1 | Player Name | String | The player's name | Relizc |
| 2 | Real UUID | String | The player's name | c364b276-34f5-4270-9d80-1780425d62c6 |

### `0xb0` Kick Player
Make the server kick a player from it (kicking the player out of the proxy)
| Field | Name | Data Type | Description | Example |
| --- | ----- | ------------ | ------------ | ------------ |
| 0 | Packet ID | Byte | The packet ID that every packet should have. | 0xf0 |
| 1 | Player Name | String | The player's name | Relizc |
| 2 | Reason | String | The reason that the player is kicked | Too Dumb |

### `0xc0` OK
Basically making the protocol say "Ok you smartie your packet is correct."
| Field | Name | Data Type | Description | Example |
| --- | ----- | ------------ | ------------ | ------------ |
| 0 | Packet ID | Byte | The packet ID that every packet should have. | 0xf0 |

### `0xe2` BungeeCord Create Server
Adds a server into the avaliable server lists.
| Field | Name | Data Type | Description | Example |
| --- | ----- | ------------ | ------------ | ------------ |
| 0 | Packet ID | Byte | The packet ID that every packet should have. | 0xf0 |
| 1 | RAM ID | Byte Enum | The RAM ID | 0x00 |
| 2 | Server ID | String | The server ID | abcd |
| 3 | Port | Short | Port of the server | 11451 |
| 4 | Category | Byte Enum | The Category of the server | 0x01 |
| 5 | Sub Category | Short | Type of subcat | 32 |

### `0xe3` BungeeCord Remove Server
Yikes this server closed.
| Field | Name | Data Type | Description | Example |
| --- | ----- | ------------ | ------------ | ------------ |
| 0 | Packet ID | Byte | The packet ID that every packet should have. | 0xf0 |
| 1 | RAM ID | Byte Enum | The RAM ID | 0x00 |
| 2 | Server ID | String | The server ID | abcd |

### `0xe4` Listing BungeeCord Servers
Yikes this server closed.
| Field | Name | Data Type | Description | Example |
| --- | ----- | ------------ | ------------ | ------------ |
| 0 | Packet ID | Byte | The packet ID that every packet should have. | 0xf0 |
| 1 | Servers | Type Array of Mixed Array | Server Details<br> | 0x00 |

### `0xe8` Dynamic Server Request Connection by Category Complete
When the client packet is sent, the server will respond with a list of 1 or more servers. This is used to prevent connection errors because the delay might just cause the server to fill up.

| Field | Name | Data Type | Description | Example |
| --- | ----- | ------------ | ------------ | ------------ |
| 0 | Packet ID | Byte | The packet ID that every packet should have. | 0xe0 |
| 1 | Target Server RAM ID | Byte Enum | The target server RAM ID | 0x01 |
| 2 | Target Server ID | String | The target server's ID | np3s |

**Here are current avaliable server categories `ID:SubCat`**:
`0x00` Verification
`0x01` Lobby
`0x02` SMP Lobby
`0x03` DeathSwap Lobby
`0x04` Dungeon Lobby
`0xa0:0` Dungeon Large Base 
`0xa0:1` Dungeon Small Base
`0xaf` Dungeon Scene Animation

### `0xc1` Down
Send by the server when it is too fucking busy. (Not used)
| Field | Name | Data Type | Description | Example |
| --- | ----- | ------------ | ------------ | ------------ |
| 0 | Packet ID | Byte | The packet ID that every packet should have. | 0xf0 |
| 1 | Message | String | HEYYAHEYYAYA? Whats going on? (The error message) | NeverGonna Give YouUp |

### `0xc5` Server Error
Send by the server when some request is breaking the server.
| Field | Name | Data Type | Description | Example |
| --- | ----- | ------------ | ------------ | ------------ |
| 0 | Packet ID | Byte | The packet ID that every packet should have. | 0xf0 |
| 1 | Message | String | HEYYAHEYYAYA? Whats going on? (The error message) | NeverGonna Give YouUp |

### `0xc4` Not Found
Send by the server when some request's parameters is not found.
| Field | Name | Data Type | Description | Example |
| --- | ----- | ------------ | ------------ | ------------ |
| 0 | Packet ID | Byte | The packet ID that every packet should have. | 0xf0 |
| 1 | Message | String | HEYYAHEYYAYA? Whats going on? (The error message) | NeverGonna Give YouUp |

### `0xc4` Not Found
Send by the server when some request's parameters is not found.
| Field | Name | Data Type | Description | Example |
| --- | ----- | ------------ | ------------ | ------------ |
| 0 | Packet ID | Byte | The packet ID that every packet should have. | 0xf0 |
| 1 | Message | String | HEYYAHEYYAYA? Whats going on? (The error message) | NeverGonna Give YouUp |
