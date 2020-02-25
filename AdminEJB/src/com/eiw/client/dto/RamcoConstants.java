package com.eiw.client.dto;

public class RamcoConstants {

	public static String driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	public static String serverName = "103.230.84.89";
	public static String portNumber = "1433";
	public static String mydatabase = serverName + ":" + portNumber
			+ ";databaseName=GPSDB";
	public static String url = "jdbc:sqlserver://" + mydatabase
			+ ";user=GPSUSER;password=mclrcl12*"; // a JDBC url

}
