package net.itsrelizc.networking;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.function.Function;

import org.bukkit.Bukkit;

import net.itsrelizc.networking.executors.KickPlayer;
import net.itsrelizc.networking.executors.UpdateWarpTemplate;

public class Communication {
	
	private int id;
	private String message;
	private DataOutputStream dOut;
	private List<Byte> array;
	private Map<CommunicationType, Runnable> executor;

	public Communication() {
		this.id = new Random().nextInt(2147483647);
		this.message = null;
		this.array = new ArrayList<Byte>();
		this.executor = new HashMap<CommunicationType, Runnable>();
	}
	
	public Communication(CommunicationType type) {
		this();
		this.writeByte(type.data);
	}
	
	public void writeByte(byte b) {
		this.array.add(b);
	}
	
	public void writeInt(int i) {
		if (i > 2147483647) {
			throw new NumberFormatException("Max integer writable is 2147483647.");
		}
		for (int x = 0; x < 4; x ++) {
			int r = i % 256;
			i /= 256;
			
			this.array.add((byte) r);
		}
	}
	
	public void writeShort(short s) {
		if (s > 65535) {
			throw new NumberFormatException("Max short writable is 65535.");
		}
		for (int x = 0; x < 2; x ++) {
			int r = s % 256;
			s /= 256;
			
			this.array.add((byte) r);
		}
	}
	
	public void writeLong(long l) {
		if (l > Long.MAX_VALUE) {
			throw new NumberFormatException("Max integer writable is " + Long.MAX_VALUE);
		}
		for (int x = 0; x < 8; x ++) {
			long r = l % 256;
			l /= 256;
			
			this.array.add((byte) r);
		}
	}
	
	public void writeChar(char c) {
		this.writeByte((byte) c);
	}
	
	public void writeString(String s) {
		if (s == null) {
			this.writeShort((short) 0);
			return;
		}
		if (s.length() > 65535) {
			throw new NumberFormatException("String too large! Max length is 65535.");
		}
		this.writeShort((short) s.length());
		for (int x = 0; x < s.length(); x ++) {
			this.writeChar(s.charAt(x));
		}
	}
	
	public void writeBoolean(boolean b) {
		if (b) {
			this.writeByte((byte) 0x01);
		} else {
			this.writeByte((byte) 0x00);
		}
	}
	
	public void writeTypeArray(List<?> l) {
		if (l.size() == 0) {
			this.writeByte((byte) 0x00);
			this.writeShort((short) 0);
			return;
		}
		boolean first = true;
		for (Object o : l) {
			if (first) {
				this.writeTypeByObjectClass(o);
				this.writeShort((short) l.size());
				first = false;
			}
			this.writeByObjectClass(o);
		}
	}
	
	public void writeMixedArray(List<Object> l) {
		this.writeShort((short) l.size());
		if (l.size() == 0) return;
		for (Object o : l) {
			this.writeTypeByObjectClass(o);
			this.writeByObjectClass(o);
		}
	}
	
	public void writeByObjectClass(Object o) {
		if (o instanceof Byte) {
			this.writeByte((byte) o);;
		} else if (o instanceof Integer) {
			if ((Integer) o >= 0) {
				this.writeInt((int) o);;
			} else {
				this.writeInt((int) o + 2147483647);;
			}
		} else if (o instanceof Short) {
			if ((Short) o >= 0) {
				this.writeShort((short) o);;
			} else {
				this.writeShort((short) ((short) o + 65536));;
			}
		} else if (o instanceof Character) {
			this.writeChar((char) o);;
		} else if (o instanceof String) {
			this.writeString((String) o);;
		} else if (o instanceof List<?>) {
			this.writeMixedArray((List<Object>) o);
		} else if (o instanceof Boolean) {
			this.writeBoolean((boolean) o);
		}
	}
	
	public void writeTypeByObjectClass(Object o) {
		if (o instanceof Byte) {
			this.writeByte((byte) 0x00);
		} else if (o instanceof Integer) {
			if ((Integer) o >= 0) {
				this.writeByte((byte) 0x03);
			} else {
				this.writeByte((byte) 0x04);
			}
		} else if (o instanceof Short) {
			if ((Short) o >= 0) {
				this.writeByte((byte) 0x01);
			} else {
				this.writeByte((byte) 0x02);
			}
		} else if (o instanceof Character) {
			this.writeByte((byte) 0x00);;
		} else if (o instanceof String) {
			this.writeByte((byte) 0x05);;
		} else if (o instanceof List<?>) {
			this.writeByte((byte) 0x07);
		} else if (o instanceof Boolean) {
			this.writeByte((byte) 0x08);
		}
	}
	
	public void attachExecutor(CommunicationType type, Runnable runnable) {
		this.executor.put(type, runnable);
	}
	
	public void sendMessage() throws IOException {
		Socket socket = new Socket("127.0.0.1", 127);
		this.dOut = new DataOutputStream(socket.getOutputStream());
		
		byte[] data = new byte[this.array.size()];
		for (int i = 0; i < this.array.size(); i ++) {
			data[i] = this.array.get(i);
		}
		
		final Communication self = this;
	    
		dOut.write(data);
		
		//System.out.print("@ PacketID: " + this.id + " -OUT-> ");
		
		Runnable run = new Runnable() {

			@Override
			public void run() {
				try {
					dOut.flush();
					DataInputStream inputStream = new DataInputStream(new BufferedInputStream(socket.getInputStream()));  
					CommunicationInputGroup inp = new CommunicationInputGroup(inputStream);
					for (CommunicationInput input : inp.getAllPackets()) {
						//System.out.print("@ PacketID: " + id + " -in-> " + input.getType());
						
						self.attachExecutor(CommunicationType.KICK_PLAYER_FROM_PROXY, new KickPlayer(input));
						self.attachExecutor(CommunicationType.INCOMING_BUNGEE_LIST_SERVERS, new UpdateWarpTemplate(input));
						self.attachExecutor(CommunicationType.ACTIVE_SHUT_DOWN, new Runnable() {

							@Override
							public void run() {
								Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("RSBundler"), new Runnable() {

									@Override
									public void run() {
										Properties prop = new Properties();
										FileInputStream fis = null;
										try {
											fis = new FileInputStream("server.properties");
										} catch (FileNotFoundException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										try {
											prop.load(fis);
										} catch (IOException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										
										Communication ok = new Communication(CommunicationType.CLOSING_SHUTDOWN_OK);
										byte b = 0x00;
										if (prop.getProperty("rid").equalsIgnoreCase("t")) {
											b = 0x00;
										} else if (prop.getProperty("rid").equalsIgnoreCase("s")) {
											b = 0x01;
										} else if (prop.getProperty("rid").equalsIgnoreCase("m")) {
											b = 0x02;
										} else if (prop.getProperty("rid").equalsIgnoreCase("b")) {
											b = 0x03;
										} else if (prop.getProperty("rid").equalsIgnoreCase("g")) {
											b = 0x04;
										}
										ok.writeByte(b);
										ok.writeString(prop.getProperty("sid"));
										try {
											ok.sendMessage();
										} catch (IOException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										Bukkit.getServer().shutdown();
									}
									
								});
							}
							
						});
						
						if (self.executor.keySet().contains(input.getType())) self.executor.get(input.getType()).run();
					}
					
					dOut.close();
					socket.close();
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		};
		
		run.run();
	}
	
}
