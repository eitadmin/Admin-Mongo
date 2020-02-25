package com.eiw.server;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.logging.Logger;

public class DeviceWriteServlet extends HttpServlet {

	private Logger logger = Logger.getLogger("admin");

	File temp = null;
	String path = "";

	protected synchronized void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String value = request.getParameter("value");

		logger.info("Received values from controller : " + value);

		if (OSValidator.isWindows()) {
			path = "C:\\Windows\\Temp\\";
		} else if (OSValidator.isUnix()) {
			path = "/home/ubuntu/imagesdirectory/";
		}

		temp = new File(path + "DeviceTest.txt");
		FileWriter writer = new FileWriter(temp);
		try {
			writer.append(value);
			writer.append('\n');
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			writer.flush();
			writer.close();
		}
	}

}
