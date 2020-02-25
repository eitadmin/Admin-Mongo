package com.eiw.server;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DeviceReadServlet extends HttpServlet {

	String path = "";

	protected synchronized void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		BufferedReader read = null;
		if (OSValidator.isWindows()) {
			path = "C:\\Windows\\Temp\\DeviceTest.txt";
		} else if (OSValidator.isUnix()) {
			path = "/home/ubuntu/imagesdirectory/DeviceTest.txt";
		}
		try {
			String sreadline;
			read = new BufferedReader(new FileReader(path));
			while ((sreadline = read.readLine()) != null) {
				PrintWriter writer = response.getWriter();
				writer.println(sreadline);
			}
			read.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			read.close();
		}
	}

}
