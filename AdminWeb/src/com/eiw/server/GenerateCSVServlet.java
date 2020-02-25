package com.eiw.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.eiw.admin.ejb.EMSAdminPortal;
import com.eiw.client.dashboard.LoginDashboardModule;
import com.eiw.client.dto.ReportData;
import com.eiw.client.dto.VehicleIMEIDto;

@SuppressWarnings("serial")
public class GenerateCSVServlet extends HttpServlet {

	/**
	 * Description of CSV File Generation
	 * 
	 * @author Ibad urRahman Description ----------- The GenerateCSVServlet
	 *         identifies the name of the class by analyzing the request
	 *         received by this servlet.If a match is found,this class handles
	 *         the request.
	 */

	ServletOutputStream out = null;
	int columns;

	String[] headers = null;
	SortedMap<String, String> hashMapRunDur = new TreeMap<String, String>();
	SortedMap<String, String> hashMapStopDur = new TreeMap<String, String>();
	@EJB
	EMSAdminPortal adminPortalImpl;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("application/csv");
		String name = request.getParameter("name");
		String compName = request.getParameter("compName");
		String branchName = request.getParameter("brnchName");
		String suffix = request.getParameter("suffix");
		String vehicle = null;
		out = response.getOutputStream();
		String path = null;
		if (OSValidator.isWindows()) {
			path = "C:\\Windows\\Temp\\";
		} else if (OSValidator.isUnix()) {
			path = "/home/ubuntu/imagesdirectory/";
		}
		File temp = null;
		try {
			if (name.equalsIgnoreCase("CompanyBasedVehiclesDeviceIMEIReport")) {
				headers = new String[] { "Plate No", "Device IMEI",
						"Fleet User" };
				response.setHeader("Content-disposition",
						"attachment; filename=VehicleDeviceIMEIDetails.csv");
				List<VehicleIMEIDto> datas = new ArrayList<VehicleIMEIDto>();
				ReportData reportData = new ReportData();
				reportData.setCompanyId(compName);
				reportData.setBranchId(branchName);
				reportData.setVin(vehicle);
				datas = adminPortalImpl.getVehilceIMEI(suffix);
				temp = new File(path + "VehicleDeviceIMEIDetails.csv");
				PrintWriter writer = new PrintWriter(temp, "UTF-8");
				try {
					writer.append("Company Name" + ',');
					writer.append(compName + ',');
					writer.append('\n');
					writer.append(name + ',');
					writer.append('\n');
					String companyId = null, preCompanyId = null;

					// set datas in respective columns
					for (int i = 0; i < datas.size(); i++) {
						if (i == 0) {
							companyId = datas.get(i).getCompanyId();
							writer.append('\n');
							writer.append("CompanyID" + ',');
							writer.append(datas.get(i).getCompanyId() + ',');
							writer.append('\n');
							writer.append("Plate No." + ',');
							writer.append("IMEI No." + ',');
							writer.append("Fleet User" + ',');
							writer.append('\n');

							writer.append(strip(datas.get(i).getPlateNo()) + ',');
							if (datas.get(i).getImeiNo() != null) {
								writer.append(datas.get(i).getImeiNo() + ",");
							} else {
								writer.append(",");
							}
							if (datas.get(i).getFleetUser() != null) {
								writer.append(datas.get(i).getFleetUser() + ",");
							} else {
								writer.append(",");
							}
							writer.append('\n');
						} else {
							preCompanyId = companyId;
							companyId = datas.get(i).getCompanyId();
							if (preCompanyId.equals(companyId)) {
								writer.append(strip(datas.get(i).getPlateNo()) + ',');
								if (datas.get(i).getImeiNo() != null) {
									writer.append(datas.get(i).getImeiNo()
											+ ",");
								} else {
									writer.append(",");
								}
								if (datas.get(i).getFleetUser() != null) {
									writer.append(datas.get(i).getFleetUser()
											+ ",");
								} else {
									writer.append(",");
								}
								writer.append('\n');
							} else {
								writer.append('\n');
								writer.append("CompanyID" + ',');
								writer.append(datas.get(i).getCompanyId() + ',');
								writer.append('\n');
								writer.append("Plate No." + ',');
								writer.append("IMEI No." + ',');
								writer.append("Fleet User" + ',');
								writer.append('\n');
								writer.append(strip(datas.get(i).getPlateNo()) + ',');
								if (datas.get(i).getImeiNo() != null) {
									writer.append(datas.get(i).getImeiNo()
											+ ",");
								} else {
									writer.append(",");
								}
								if (datas.get(i).getFleetUser() != null) {
									writer.append(datas.get(i).getFleetUser()
											+ ",");
								} else {
									writer.append(",");
								}
								writer.append('\n');
							}
						}
					}
				} finally {
					writer.flush();
					writer.close();
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try (BufferedReader br = new BufferedReader(new FileReader(temp))) {
			String isCurrentLine;
			// Sending temp file content as response to client machine
			while ((isCurrentLine = br.readLine()) != null) {
				byte[] b = isCurrentLine.getBytes();
				response.getOutputStream().write(b);
				response.getOutputStream().write("\n".getBytes());
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// Deleting temp file to free memory.
			temp.delete();
		}
	}

	public String strip(String s) {
		String st = " abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789,.";
		String result = "";
		for (int i = 0; i < s.length(); i++) {
			if (st.indexOf(s.charAt(i)) >= 0)
				result += s.charAt(i);
		}
		return result;
	}

}