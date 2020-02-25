package com.eiw.device.android;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Date;

import org.json.JSONObject;

public class AndroidDecoder {
	private DataInputStream dis;
	private DataOutputStream dos;
	private String rawData;
	private long imeiNo;
	private Date eventTimeStamp;
	private double latitude;
	private double longitude;
	private int speed;
	private String status;
	private long battery;

	public AndroidDecoder(String data) {
		this.rawData = data;
	}

	public AndroidDecoder(DataInputStream clientSocketDis,
			DataOutputStream clientSocketDos) {
		this.dis = clientSocketDis;
		this.dos = clientSocketDos;
	}

	public Position decode() {
		try {
			StringBuilder sb = new StringBuilder();
			while (true) {
				int count = this.dis.available();
				if (count > 0) {
					sb.append((char) this.dis.readByte());
					if (sb.toString().contains(";")) {
						sb.delete(sb.length() - 1, sb.length());
						break;
					}
				}
			}
			this.rawData = sb.toString();
		} catch (Exception e) {
			System.out.println("DeCode value " + this.rawData);
		}
		System.out.println("DeCode value " + this.rawData);
		return getPosition(null);
	}

	public Position getPosition(String data) {
		Position position = new Position();
		try {
			JSONObject obj = new JSONObject(this.rawData);
			position.setDeviceId(obj.getString("imei"));
			position.setBattery(obj.getLong("battery"));
			position.setValid(true);
			position.setTime(new Date(obj.getLong("timestamp")));
			position.setLatitude(obj.getDouble("latitude"));
			position.setLongitude(obj.getDouble("longitude"));
			position.setSpeed(obj.getInt("speed"));
			position.setEngine(obj.getBoolean("engine"));
			JSONObject ioJson = new JSONObject(obj.getString("io_data"));
			if (ioJson.has("vin")) {
				ioJson.remove("vin");
			}
			position.setIoData(ioJson.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return position;
	}

}
