package com.eiw.device.cantrack;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.jboss.logging.Logger;

import com.eiw.device.handler.CantrackDeviceHandler;

public class CantrackByteWapper {
	
	private static final Logger LOGGER = Logger.getLogger("CantrackByteWapper");
	private static final String MSG_LOGIN = "1";
	public static final String MSG_GPS = "2";
	public static final String MSG_ALARM = "4";
	public static final String MSG_HBD = "7";
	private static final String MSG_START ="6767";
	private String type = null;
	private String imeiNo =null;
	private int lengthOfPacket=0;
	private int informationSerialNo=0;
	private String language = null;
	private CantrackGpsData cantrackGpsData = new CantrackGpsData();
	
	
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getImeiNo() {
		return imeiNo;
	}

	public void setImeiNo(String imeiNo) {
		this.imeiNo = imeiNo;
	}

	public int getLengthOfPacket() {
		return lengthOfPacket;
	}

	public void setLengthOfPacket(int lengthOfPacket) {
		this.lengthOfPacket = lengthOfPacket;
	}

	public int getInformationSerialNo() {
		return informationSerialNo;
	}

	public void setInformationSerialNo(int informationSerialNo) {
		this.informationSerialNo = informationSerialNo;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public CantrackGpsData getCantrackGpsData() {
		return cantrackGpsData;
	}

	public void setCantrackGpsData(CantrackGpsData cantrackGpsData) {
		this.cantrackGpsData = cantrackGpsData;
	}

	public void unwrapDataFromStream(DataInputStream clientSocketDis,
			DataOutputStream clientSocketDos,
			CantrackDeviceHandler cantrackDeviceHandler) throws Exception {
		String start = null;
		try {
			if (clientSocketDis.available() != -1) {
				int StartPacket = clientSocketDis.readShort();
				start = Integer.toHexString(StartPacket);
				if (start.equalsIgnoreCase(MSG_START)) {
					this.type = Integer.toHexString(clientSocketDis.readByte());
					this.lengthOfPacket = clientSocketDis.readShort();
					this.informationSerialNo = clientSocketDis.readShort();
					if (type.equalsIgnoreCase(MSG_LOGIN)) {
						LOGGER.info("!.........Login Packet............!");
						long content = clientSocketDis.readLong();
						this.language = Integer.toHexString(clientSocketDis.readByte());
						this.imeiNo = Long.toHexString(content);
					} else if (type.equalsIgnoreCase(MSG_GPS)) {
						LOGGER.info("!.........Gps Packet............!");
						this.cantrackGpsData.read(clientSocketDis,type);
					}else if(type.equalsIgnoreCase(MSG_ALARM)){
						LOGGER.info("!.........Alarm Packet............!");
						this.cantrackGpsData.read(clientSocketDis,type);
					} else if (type.equalsIgnoreCase(MSG_HBD)) {
						LOGGER.info("!.........HeartBeat Packet............!");
						this.cantrackGpsData.readHbd(clientSocketDis,type);
					}
				}
			}

		} catch (IOException j) {
			throw new IOException("CantrackByteWrapper I/O Exce " + j);
		} finally {

		}
	}

}