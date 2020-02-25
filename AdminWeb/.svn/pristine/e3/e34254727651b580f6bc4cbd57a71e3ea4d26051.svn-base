package com.eiw.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.eiw.admin.ejb.EMSAdminPortal;
import com.eiw.client.dto.ReportData;
import com.eiw.client.dto.ReportPdfData;
import com.eiw.client.dto.VehicleIMEIDto;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

@SuppressWarnings("serial")
public class GeneratePDFServlet extends HttpServlet {

	ServletOutputStream out = null;
	int columns;

	String[] headers = null;

	PdfPTable table;

	PdfPTable dateTable = null;
	String companyGroup = null;
	Image setLogo = null, setLogistic = null;
	Document document;
	String groupBy;
	Font fontStyleTitle = FontFactory.getFont(FontFactory.HELVETICA, "UTF-8",
			9, Font.NORMAL);
	Font fontStyleHeader = FontFactory.getFont(FontFactory.HELVETICA, "UTF-8",
			8, Font.BOLD);
	Font fontStyleTest = FontFactory.getFont(FontFactory.HELVETICA, "UTF-8",
			20, Font.NORMAL);
	Font fontStyleGroupBy = FontFactory.getFont(FontFactory.HELVETICA, "UTF-8",
			8, Font.NORMAL);
	Font fontStyleContent1 = FontFactory.getFont(FontFactory.HELVETICA,
			"UTF-8", 8, Font.NORMAL);
	BaseFont bf = null;
	Font fontStyleContent = null;

	@EJB
	EMSAdminPortal adminPortalImpl;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			if (OSValidator.isWindows()) {
				bf = BaseFont.createFont("C:\\windows\\fonts\\KacstOffice.ttf",
						BaseFont.IDENTITY_H, true);
			} else if (OSValidator.isUnix()) {
				bf = BaseFont.createFont("/home/ubuntu/.fonts/KACSTOFFICE.TTF",
						BaseFont.IDENTITY_H, true);
			}
			fontStyleContent = new Font(bf, 9);
		} catch (DocumentException doce) {
			System.out.println("DocumentException" + doce);
		}
		document = new Document();
		String name = request.getParameter("name");
		String compName = request.getParameter("compName");
		String branchName = request.getParameter("brnchName");
		String companyLogo = request.getParameter("companyLogo");
		String ourlogo = request.getParameter("ourlogo");
		String suffix = request.getParameter("suffix");
		String bucketName = request.getParameter("bucketName");
		// System.out.println("bucketName" + bucketName);

		out = response.getOutputStream();

		out.print(name);

		try {

			PdfWriter writer = PdfWriter.getInstance(document,
					response.getOutputStream());
			document.open();
			PdfPCell titleCell = new PdfPCell();
			// titleCell.setGrayFill(0.5f);
			PdfPCell headerCell1 = new PdfPCell();
			headerCell1.setBackgroundColor(BaseColor.WHITE);

			PdfPCell dateCellComp = new PdfPCell();
			PdfPCell dateCellBrch = new PdfPCell();

			try {

				String companyurl = "http://" + bucketName
						+ ".s3.amazonaws.com/Company/";
				if (ourlogo != null) {
					setLogistic = Image.getInstance(companyurl + ourlogo);
				}
				setLogistic.setAlignment(Image.LEFT);
				if (companyLogo == null || companyLogo.equalsIgnoreCase("")
						|| companyLogo.equalsIgnoreCase("null")) {
					setLogo = Image.getInstance(companyurl + "transparent.gif");
				} else
					setLogo = Image.getInstance(companyurl + companyLogo);

				setLogo.setAlignment(Image.RIGHT);
				setLogo.scaleAbsolute(130, 40);

			} catch (Exception e) {
				System.out.println("error = " + e);
			}

			if (name.equalsIgnoreCase("CompanyBasedVehiclesDeviceIMEIReport")) {
				headers = new String[] { "Plate No", "Device IMEI",
						"Fleet User" };
				response.setHeader("Content-disposition",
						"attachment; filename=VehicleDeviceIMEIDetails.pdf");
				PdfPTable titleTable = new PdfPTable(1);
				PdfPTable table1 = new PdfPTable(2);
				PdfPTable table2 = new PdfPTable(4);
				table = new PdfPTable(headers.length);
				titleCell.setPhrase(new Phrase("Report : " + name,
						fontStyleTitle));
				List<ReportPdfData> pdfDatas = new ArrayList<ReportPdfData>();
				List<VehicleIMEIDto> datas = new ArrayList<VehicleIMEIDto>();
				ReportData reportData = new ReportData();
				reportData.setCompanyId(compName);
				reportData.setBranchId(branchName);
				datas = adminPortalImpl.getVehilceIMEI(suffix);
				titleTable.addCell(titleCell);
				table2.getDefaultCell().setBorder(Rectangle.NO_BORDER);
				table2.addCell(setLogistic);
				table2.addCell("");
				table2.addCell("");
				table2.addCell(setLogo);
				dateCellComp.setPhrase(new Phrase("Company Name:" + compName,
						fontStyleHeader));
				dateCellBrch.setPhrase(new Phrase("Branch Name:" + branchName,
						fontStyleHeader));
				PdfPTable tableCompName = new PdfPTable(2);
				tableCompName.getDefaultCell().setBorder(Rectangle.NO_BORDER);
				tableCompName.addCell(dateCellComp);
				tableCompName.addCell(dateCellBrch);
				document.add(table2);
				document.add(tableCompName);
				document.add(titleTable);
				document.add(table1);

				// set datas in respective columns
				for (int i = 0; i < datas.size(); i++) {

					ReportPdfData reportPdfData = new ReportPdfData();
					System.out.println("::::::::" + datas.get(i).getPlateNo());
					reportPdfData.setColumn1(datas.get(i).getPlateNo());
					reportPdfData.setColumn2(datas.get(i).getImeiNo());
					reportPdfData.setColumn3(datas.get(i).getFleetUser());
					reportPdfData.setGroupBy(datas.get(i).getCompanyId());
					reportPdfData.setColumn11(datas.get(i).getCompanyId());
					pdfDatas.add(reportPdfData);
					columns = 3;

				}
				generatePdf(pdfDatas, columns, headers);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void generatePdf(List<ReportPdfData> pdfDatas, int columns,
			String[] headers) {
		try {
			for (int i = 0; i < headers.length; i++) {
				String header = headers[i];
				PdfPCell cell = new PdfPCell();
				cell.setGrayFill(0.9f);
				cell.setPhrase(new Phrase(header, fontStyleHeader));
				table.addCell(cell);
			}
			document.add(table);
			for (int i = 0; i < pdfDatas.size(); i++) {

				if (i == 0) {

					PdfPTable contentTable = new PdfPTable(headers.length);
					dateTable = new PdfPTable(1);
					PdfPCell groupByCell = new PdfPCell();
					groupByCell.setGrayFill(0.7f);
					groupByCell.setPhrase(new Phrase(pdfDatas.get(i)
							.getGroupBy(), fontStyleGroupBy));
					dateTable.addCell(groupByCell);
					companyGroup = pdfDatas.get(i).getColumn11();
					for (int j = 0; j < columns; j++) {
						switch (j) {
						case 0: {
							PdfPCell cell1 = new PdfPCell();
							cell1.setPhrase(new Phrase(pdfDatas.get(i)
									.getColumn1(), fontStyleContent));
							contentTable.addCell(cell1);
							break;
						}
						case 1: {
							PdfPCell cell1 = new PdfPCell();
							cell1.setPhrase(new Phrase(pdfDatas.get(i)
									.getColumn2(), fontStyleContent));
							contentTable.addCell(cell1);
							break;
						}
						case 2: {
							PdfPCell cell1 = new PdfPCell();
							cell1.setPhrase(new Phrase(pdfDatas.get(i)
									.getColumn3(), fontStyleContent));
							contentTable.addCell(cell1);
							break;
						}
						case 3: {
							PdfPCell cell1 = new PdfPCell();
							cell1.setPhrase(new Phrase(pdfDatas.get(i)
									.getColumn4(), fontStyleContent));
							contentTable.addCell(cell1);
							break;
						}

						case 4: {
							PdfPCell cell1 = new PdfPCell();
							cell1.setPhrase(new Phrase(pdfDatas.get(i)
									.getColumn5(), fontStyleContent));
							contentTable.addCell(cell1);
							break;
						}
						case 5: {
							PdfPCell cell1 = new PdfPCell();
							cell1.setPhrase(new Phrase(pdfDatas.get(i)
									.getColumn6(), fontStyleContent));
							contentTable.addCell(cell1);
							break;
						}
						case 6: {
							PdfPCell cell1 = new PdfPCell();
							cell1.setPhrase(new Phrase(pdfDatas.get(i)
									.getColumn7(), fontStyleContent));
							contentTable.addCell(cell1);
							break;
						}
						case 7: {
							PdfPCell cell1 = new PdfPCell();
							cell1.setPhrase(new Phrase(pdfDatas.get(i)
									.getColumn8(), fontStyleContent));
							contentTable.addCell(cell1);
							break;
						}
						case 8: {
							PdfPCell cell1 = new PdfPCell();
							cell1.setPhrase(new Phrase(pdfDatas.get(i)
									.getColumn9(), fontStyleContent));
							contentTable.addCell(cell1);
							break;
						}

						default:
							break;
						}
					}
					contentTable.completeRow();
					document.add(dateTable);
					document.add(contentTable);
				} else if ((companyGroup != null)
						&& !(companyGroup.equalsIgnoreCase(pdfDatas.get(i)
								.getColumn11()))) {
					PdfPTable contentTable = new PdfPTable(headers.length);
					dateTable = new PdfPTable(1);
					PdfPCell groupByCell = new PdfPCell();
					groupByCell.setGrayFill(0.7f);
					groupByCell.setPhrase(new Phrase(pdfDatas.get(i)
							.getGroupBy(), fontStyleGroupBy));
					dateTable.addCell(groupByCell);
					companyGroup = pdfDatas.get(i).getColumn11();
					for (int j = 0; j < columns; j++) {
						switch (j) {
						case 0: {
							PdfPCell cell1 = new PdfPCell();
							cell1.setPhrase(new Phrase(pdfDatas.get(i)
									.getColumn1(), fontStyleContent));
							contentTable.addCell(cell1);
							break;
						}
						case 1: {
							PdfPCell cell1 = new PdfPCell();
							cell1.setPhrase(new Phrase(pdfDatas.get(i)
									.getColumn2(), fontStyleContent));
							contentTable.addCell(cell1);
							break;
						}
						case 2: {
							PdfPCell cell1 = new PdfPCell();
							cell1.setPhrase(new Phrase(pdfDatas.get(i)
									.getColumn3(), fontStyleContent));
							contentTable.addCell(cell1);
							break;
						}
						case 3: {
							PdfPCell cell1 = new PdfPCell();
							cell1.setPhrase(new Phrase(pdfDatas.get(i)
									.getColumn4(), fontStyleContent));
							contentTable.addCell(cell1);
							break;
						}

						case 4: {
							PdfPCell cell1 = new PdfPCell();
							cell1.setPhrase(new Phrase(pdfDatas.get(i)
									.getColumn5(), fontStyleContent));
							contentTable.addCell(cell1);
							break;
						}
						case 5: {
							PdfPCell cell1 = new PdfPCell();
							cell1.setPhrase(new Phrase(pdfDatas.get(i)
									.getColumn6(), fontStyleContent));
							contentTable.addCell(cell1);
							break;
						}
						case 6: {
							PdfPCell cell1 = new PdfPCell();
							cell1.setPhrase(new Phrase(pdfDatas.get(i)
									.getColumn7(), fontStyleContent));
							contentTable.addCell(cell1);
							break;
						}
						case 7: {
							PdfPCell cell1 = new PdfPCell();
							cell1.setPhrase(new Phrase(pdfDatas.get(i)
									.getColumn8(), fontStyleContent));
							contentTable.addCell(cell1);
							break;
						}
						case 8: {
							PdfPCell cell1 = new PdfPCell();
							cell1.setPhrase(new Phrase(pdfDatas.get(i)
									.getColumn9(), fontStyleContent));
							contentTable.addCell(cell1);
							break;
						}

						default:
							break;
						}
					}
					contentTable.completeRow();
					document.add(dateTable);
					document.add(contentTable);

				} else {
					PdfPTable contentTable = new PdfPTable(headers.length);
					dateTable = new PdfPTable(1);
					PdfPCell groupByCell = new PdfPCell();
					groupByCell.setGrayFill(0.7f);
					groupByCell.setPhrase(new Phrase(pdfDatas.get(i)
							.getGroupBy(), fontStyleGroupBy));
					dateTable.addCell(groupByCell);
					companyGroup = pdfDatas.get(i).getColumn11();
					for (int j = 0; j < columns; j++) {
						switch (j) {
						case 0: {
							PdfPCell cell1 = new PdfPCell();
							cell1.setPhrase(new Phrase(pdfDatas.get(i)
									.getColumn1(), fontStyleContent));
							contentTable.addCell(cell1);
							break;
						}
						case 1: {
							PdfPCell cell1 = new PdfPCell();
							cell1.setPhrase(new Phrase(pdfDatas.get(i)
									.getColumn2(), fontStyleContent));
							contentTable.addCell(cell1);
							break;
						}
						case 2: {
							PdfPCell cell1 = new PdfPCell();
							cell1.setPhrase(new Phrase(pdfDatas.get(i)
									.getColumn3(), fontStyleContent));
							contentTable.addCell(cell1);
							break;
						}
						case 3: {
							PdfPCell cell1 = new PdfPCell();
							cell1.setPhrase(new Phrase(pdfDatas.get(i)
									.getColumn4(), fontStyleContent));
							contentTable.addCell(cell1);
							break;
						}

						case 4: {
							PdfPCell cell1 = new PdfPCell();
							cell1.setPhrase(new Phrase(pdfDatas.get(i)
									.getColumn5(), fontStyleContent));
							contentTable.addCell(cell1);
							break;
						}
						case 5: {
							PdfPCell cell1 = new PdfPCell();
							cell1.setPhrase(new Phrase(pdfDatas.get(i)
									.getColumn6(), fontStyleContent));
							contentTable.addCell(cell1);
							break;
						}
						case 6: {
							PdfPCell cell1 = new PdfPCell();
							cell1.setPhrase(new Phrase(pdfDatas.get(i)
									.getColumn7(), fontStyleContent));
							contentTable.addCell(cell1);
							break;
						}
						case 7: {
							PdfPCell cell1 = new PdfPCell();
							cell1.setPhrase(new Phrase(pdfDatas.get(i)
									.getColumn8(), fontStyleContent));
							contentTable.addCell(cell1);
							break;
						}
						case 8: {
							PdfPCell cell1 = new PdfPCell();
							cell1.setPhrase(new Phrase(pdfDatas.get(i)
									.getColumn9(), fontStyleContent));
							contentTable.addCell(cell1);
							break;
						}

						default:
							break;
						}
					}
					document.add(contentTable);
				}

			}
			document.setPageSize(PageSize.A4);
			document.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void generatePdf1(List<ReportPdfData> pdfDatas, int columns,
			String[] headers) {
		try {
			for (int i = 0; i < headers.length; i++) {
				String header = headers[i];
				PdfPCell cell = new PdfPCell();
				cell.setGrayFill(0.9f);
				cell.setPhrase(new Phrase(header, fontStyleHeader));
				table.addCell(cell);
			}
			document.add(table);
			for (int i = 0; i < pdfDatas.size(); i++) {

				if (i == 0) {

					PdfPTable contentTable = new PdfPTable(headers.length);
					dateTable = new PdfPTable(1);
					PdfPCell groupByCell = new PdfPCell();
					groupByCell.setGrayFill(0.7f);
					groupByCell.setPhrase(new Phrase(pdfDatas.get(i)
							.getGroupBy(), fontStyleGroupBy));
					dateTable.addCell(groupByCell);
					companyGroup = pdfDatas.get(i).getColumn11();
					for (int j = 0; j < columns; j++) {
						switch (j) {
						case 0: {
							PdfPCell cell1 = new PdfPCell();
							cell1.setPhrase(new Phrase(pdfDatas.get(i)
									.getColumn1(), fontStyleContent));
							contentTable.addCell(cell1);
							break;
						}
						case 1: {
							PdfPCell cell1 = new PdfPCell();
							cell1.setPhrase(new Phrase(pdfDatas.get(i)
									.getColumn2(), fontStyleContent));
							contentTable.addCell(cell1);
							break;
						}
						case 2: {
							PdfPCell cell1 = new PdfPCell();
							cell1.setPhrase(new Phrase(pdfDatas.get(i)
									.getColumn3(), fontStyleContent));
							contentTable.addCell(cell1);
							break;
						}
						case 3: {
							PdfPCell cell1 = new PdfPCell();
							cell1.setPhrase(new Phrase(pdfDatas.get(i)
									.getColumn4(), fontStyleContent));
							contentTable.addCell(cell1);
							break;
						}

						case 4: {
							PdfPCell cell1 = new PdfPCell();
							cell1.setPhrase(new Phrase(pdfDatas.get(i)
									.getColumn5(), fontStyleContent));
							contentTable.addCell(cell1);
							break;
						}
						case 5: {
							PdfPCell cell1 = new PdfPCell();
							cell1.setPhrase(new Phrase(pdfDatas.get(i)
									.getColumn6(), fontStyleContent));
							contentTable.addCell(cell1);
							break;
						}
						case 6: {
							PdfPCell cell1 = new PdfPCell();
							cell1.setPhrase(new Phrase(pdfDatas.get(i)
									.getColumn7(), fontStyleContent));
							contentTable.addCell(cell1);
							break;
						}
						case 7: {
							PdfPCell cell1 = new PdfPCell();
							cell1.setPhrase(new Phrase(pdfDatas.get(i)
									.getColumn8(), fontStyleContent));
							contentTable.addCell(cell1);
							break;
						}
						case 8: {
							PdfPCell cell1 = new PdfPCell();
							cell1.setPhrase(new Phrase(pdfDatas.get(i)
									.getColumn9(), fontStyleContent));
							contentTable.addCell(cell1);
							break;
						}
						default:
							break;
						}
					}
					contentTable.completeRow();
					document.add(dateTable);
					document.add(contentTable);
				} else if ((companyGroup != null)
						&& !(companyGroup.equalsIgnoreCase(pdfDatas.get(i)
								.getColumn11()))) {
					PdfPTable contentTable = new PdfPTable(headers.length);
					dateTable = new PdfPTable(1);
					PdfPCell groupByCell = new PdfPCell();
					groupByCell.setGrayFill(0.7f);
					groupByCell.setPhrase(new Phrase(pdfDatas.get(i)
							.getGroupBy(), fontStyleGroupBy));
					dateTable.addCell(groupByCell);
					companyGroup = pdfDatas.get(i).getColumn11();
					for (int j = 0; j < columns; j++) {
						switch (j) {
						case 0: {
							PdfPCell cell1 = new PdfPCell();
							cell1.setPhrase(new Phrase(pdfDatas.get(i)
									.getColumn1(), fontStyleContent));
							contentTable.addCell(cell1);
							break;
						}
						case 1: {
							PdfPCell cell1 = new PdfPCell();
							cell1.setPhrase(new Phrase(pdfDatas.get(i)
									.getColumn2(), fontStyleContent));
							contentTable.addCell(cell1);
							break;
						}
						case 2: {
							PdfPCell cell1 = new PdfPCell();
							cell1.setPhrase(new Phrase(pdfDatas.get(i)
									.getColumn3(), fontStyleContent));
							contentTable.addCell(cell1);
							break;
						}
						case 3: {
							PdfPCell cell1 = new PdfPCell();
							cell1.setPhrase(new Phrase(pdfDatas.get(i)
									.getColumn4(), fontStyleContent));
							contentTable.addCell(cell1);
							break;
						}

						case 4: {
							PdfPCell cell1 = new PdfPCell();
							cell1.setPhrase(new Phrase(pdfDatas.get(i)
									.getColumn5(), fontStyleContent));
							contentTable.addCell(cell1);
							break;
						}
						case 5: {
							PdfPCell cell1 = new PdfPCell();
							cell1.setPhrase(new Phrase(pdfDatas.get(i)
									.getColumn6(), fontStyleContent));
							contentTable.addCell(cell1);
							break;
						}
						case 6: {
							PdfPCell cell1 = new PdfPCell();
							cell1.setPhrase(new Phrase(pdfDatas.get(i)
									.getColumn7(), fontStyleContent));
							contentTable.addCell(cell1);
							break;
						}
						case 7: {
							PdfPCell cell1 = new PdfPCell();
							cell1.setPhrase(new Phrase(pdfDatas.get(i)
									.getColumn8(), fontStyleContent));
							contentTable.addCell(cell1);
							break;
						}
						case 8: {
							PdfPCell cell1 = new PdfPCell();
							cell1.setPhrase(new Phrase(pdfDatas.get(i)
									.getColumn9(), fontStyleContent));
							contentTable.addCell(cell1);
							break;
						}
						default:
							break;
						}
					}
					contentTable.completeRow();
					document.add(dateTable);
					document.add(contentTable);

				} else {
					PdfPTable contentTable = new PdfPTable(headers.length);
					dateTable = new PdfPTable(1);
					PdfPCell groupByCell = new PdfPCell();
					groupByCell.setGrayFill(0.7f);
					groupByCell.setPhrase(new Phrase(pdfDatas.get(i)
							.getGroupBy(), fontStyleGroupBy));
					dateTable.addCell(groupByCell);
					companyGroup = pdfDatas.get(i).getColumn11();
					for (int j = 0; j < columns; j++) {
						switch (j) {
						case 0: {
							PdfPCell cell1 = new PdfPCell();
							cell1.setPhrase(new Phrase(pdfDatas.get(i)
									.getColumn1(), fontStyleContent));
							contentTable.addCell(cell1);
							break;
						}
						case 1: {
							PdfPCell cell1 = new PdfPCell();
							cell1.setPhrase(new Phrase(pdfDatas.get(i)
									.getColumn2(), fontStyleContent));
							contentTable.addCell(cell1);
							break;
						}
						case 2: {
							PdfPCell cell1 = new PdfPCell();
							cell1.setPhrase(new Phrase(pdfDatas.get(i)
									.getColumn3(), fontStyleContent));
							contentTable.addCell(cell1);
							break;
						}
						case 3: {
							PdfPCell cell1 = new PdfPCell();
							cell1.setPhrase(new Phrase(pdfDatas.get(i)
									.getColumn4(), fontStyleContent));
							contentTable.addCell(cell1);
							break;
						}

						case 4: {
							PdfPCell cell1 = new PdfPCell();
							cell1.setPhrase(new Phrase(pdfDatas.get(i)
									.getColumn5(), fontStyleContent));
							contentTable.addCell(cell1);
							break;
						}
						case 5: {
							PdfPCell cell1 = new PdfPCell();
							cell1.setPhrase(new Phrase(pdfDatas.get(i)
									.getColumn6(), fontStyleContent));
							contentTable.addCell(cell1);
							break;
						}
						case 6: {
							PdfPCell cell1 = new PdfPCell();
							cell1.setPhrase(new Phrase(pdfDatas.get(i)
									.getColumn7(), fontStyleContent));
							contentTable.addCell(cell1);
							break;
						}
						case 7: {
							PdfPCell cell1 = new PdfPCell();
							cell1.setPhrase(new Phrase(pdfDatas.get(i)
									.getColumn8(), fontStyleContent));
							contentTable.addCell(cell1);
							break;
						}
						case 8: {
							PdfPCell cell1 = new PdfPCell();
							cell1.setPhrase(new Phrase(pdfDatas.get(i)
									.getColumn9(), fontStyleContent));
							contentTable.addCell(cell1);
							break;
						}
						default:
							break;
						}
					}
					document.add(contentTable);
				}

			}
			document.setPageSize(PageSize.A4);
			document.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}