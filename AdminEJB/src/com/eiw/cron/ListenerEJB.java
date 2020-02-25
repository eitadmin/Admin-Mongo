package com.eiw.cron;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

//@Singleton
//@Startup
public class ListenerEJB {

	@PersistenceContext(unitName = "ltmscompanyadminpu")
	private EntityManager em;

	@PostConstruct
	public void startJbossServer() {
		String license = "1578747EAR32LocalNo@a";
		try {
			InetAddress address = InetAddress.getLocalHost();
			Process p = Runtime.getRuntime().exec("getmac /fo csv /nh");
			BufferedReader in = new BufferedReader(
					new InputStreamReader(p.getInputStream()));
			String line;
			line = in.readLine();
			String[] result = line.split(",");
			String macAdd = result[0].replace('"', ' ').trim();
			String ipAdd = address.getHostAddress();
			Query query = em.createQuery(
					"SELECT a.ipaddress FROM Authenticate a WHERE a.ipaddress=:ipaddress "
							+ "AND a.macaddress=:macaddress "
							+ "AND a.licenseNo=:licenseNo");
			query.setParameter("ipaddress", ipAdd);
			query.setParameter("macaddress", macAdd);
			query.setParameter("licenseNo", license);
			String resutlList = (String) query.getSingleResult();
			if (resutlList != null) {
				System.out.println("SERVER STARTED SUCCESSFULLY");
			} else {
				throw new NullPointerException(
						"IP Address and MAC Address Does Not Match");
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (PersistenceException e) {
			throw new NullPointerException(
					"IP Address and MAC Address Does Not Match");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
