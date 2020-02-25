function ifGridExists(gridreportNames) {
	var reportName = gridreportNames.replace(/ +/g, "");
	var check1;
	if ($("#" + reportName + "gridtable").length != 1) {
		check1 = true;
	} else {
		check1 = false;
	}
	return check1;
}

function callGridDataJs(isVerify, arg, gridreportNames, formName, arrays) {

	var source;
	var reportName = gridreportNames.replace(/ +/g, "");
	if (isVerify == 1) {
	
		var tab = '';
		var ChartData = [];
		
		var theme = 'energyblue';
		var datarow = null;
		var Width, Height;

		var rowid = null;
		var type = arg.displayType;

		var myJSONText = null;
		var arrayColumn = arg.listColumns;

		var arrayListValues1 = arg.valuesDtos[0].values;
		var arrayFirst = [];
		for (var k = 0; k < arg.valuesDtos.length; k++) {
			arrayFirst.push(arg.valuesDtos[k].values);
		}
		var arrayColumns = [];
		var arrayColumnsFirst = arg.listColumns;

		for (var k = 0; k < arrayColumnsFirst.length; k++) {
			arrayColumns.push(arrayColumnsFirst[k]);
		}
		var myObject;
		var temp_arrayCvalue;
		var temp_arrayCval;
		var temp_arrayCvalue1 = "";
		var temp_arrayCval1 = "";
		var arValue;
		var arCol;
		for (var i = 0; i < arg.valuesDtos.length; i++) {
			temp_arrayCvalue = arg.valuesDtos[i].values;
			temp_arrayCval = arg.listColumns;
			if (i == 0) {
				temp_arrayCvalue1 = temp_arrayCvalue;
				temp_arrayCval1 = temp_arrayCval;

			} else {
				temp_arrayCvalue1 = temp_arrayCvalue + "," + temp_arrayCvalue1;
				temp_arrayCval1 = temp_arrayCval + "," + temp_arrayCval1;
			}
		}
		var temp = temp_arrayCvalue1.indexOf(',');
		if (temp == -1) {
			arValue = temp_arrayCvalue1;
			arCol = temp_arrayCval1;
		} else {
			arValue = temp_arrayCvalue1.split(',');
			arCol = temp_arrayCval1.split(',');
		}
		var jqChart = [];
		for (var u = 0; u <= arValue.length; u++) {
			if (u % arrayColumns.length == 0) {
				if (u != 0)
					jqChart.push(myObject);
				myObject = new Object();
			}
			myObject[arCol[u]] = arValue[u];
		}
		var xaxis = null;
		var json_chart1 = [];

		for (var Cval = 0; Cval < arrayColumns.length; Cval++) {
			var temp_Ctemp = {};
			xaxis = arrayColumns[1];
			if (arrayColumns[Cval] != xaxis) {
				temp_Ctemp['dataField'] = arrayColumns[Cval];
				temp_Ctemp['displayText'] = arrayColumns[Cval];
				json_chart1.push(temp_Ctemp);
			}
		}

		var inputtedNums = new Array();
		for (var i = 0; i < arrayColumn.length; i++) {
			inputtedNums[i] = new Array();
		}

		var sortingArray = inputtedNums;
		sortingArray.sort(function(a, b) {
			return a[0] - b[0];
		});

		var json_temp = [];
		for (var kval = 0; kval < arrayColumns.length; kval++) {
			var temp_column = {};
			temp_column['name'] = arrayColumns[kval];
			temp_column['type'] = 'string';
			json_temp.push(temp_column);
		}

		source = {
			localdata : JSON.parse(arg.datavalues),
			datafields : json_temp,
			addrow : function(rowid, rowdata, position, commit) {
				commit(true);
			},
			datatype : "json",
		};

		dataAdapter = new $.jqx.dataAdapter(source);
		var cellsrenderer = function(row, column, value, defaulthtml,
				columnproperties) {
			return '<div style="text-align:' + columnproperties.cellsalign
					+ '; margin-top: 5px;">&nbsp;' + value + '&nbsp;</div>';

		};

		var columnsrenderer = function(value) {
			return '<div style="text-align: center; margin-top: 5px;">' + value
					+ '</div>';
		};

		var json_columnValues = [];

		for (var h = 0; h < arrayColumns.length; h++) {
			var json_column = {};
			var findheader = '';
			findheader = arrayColumns[h].indexOf("https");
			if (findheader != -1) {
				json_column['text'] = '<img src=' + arrayColumns[h]
						+ ' height="20" width="20"> </img>';
			} else {
				json_column['text'] = arrayColumns[h];
			}
			if (arrayColumns[h] == 'id') {
				json_column['hidden'] = true;
			}

			json_column['datafield'] = arrayColumns[h];
			json_column['width'] = 'auto';
			json_column['columntype'] = 'textbox';
			json_column['filtertype'] = 'textbox';
			json_column['filtercondition'] = 'starts_with';
			json_column['renderer'] = columnsrenderer,
					json_column['cellsrenderer'] = cellsrenderer;
			json_columnValues.push(json_column);
		}

		if (arg.chart == 'chart') {
			Width = 670;
			Height = 520;
			Mode = false;
		} else {

			Width = arg.gridwidth + 'px';
			Height = arg.gridheight + 'px';
		}
		var d = 0;
		for (var c = 0; c < sortingArray.length; c++) {
			if (typeof sortingArray[c][1] !== 'undefined'
					&& sortingArray[c][1] !== null) {
				d = c;
			}
		}

		var groupName = sortingArray[d][1];
		var groupsrenderer = function(text, group, expanded, arrayFirst) {

			if (arrayFirst.groupcolumn.datafield == groupName) {
				var str = group + ":" + arrayFirst.subItems.length;
				var index = ChartData.indexOf(str);
				if ((ChartData.length > 0) && (index !== -1)) {

					ChartData[index] = str;

				} else {
					ChartData.push(str);
				}
			}
		};

		$("#" + reportName + "gridtable").jqxGrid({

			width : Width,
			height : Height,
			theme : theme,
			source : dataAdapter,
			pageable : true,
			filterable : true,
			filtermode : 'excel',
			groupable : true,
			groupsrenderer : groupsrenderer,
			showfilterrow : true,
			groups : [],
			showtoolbar : true,
			columns : json_columnValues,
			selectionmode : 'singlerow',
			columnsresize : true

		});

		for (var t = 0; t < sortingArray.length; t++) {
			$("#" + reportName + "gridtable").jqxGrid('addgroup',
					sortingArray[t][1]);
		}

		$("#toolbar" + reportName + "gridtable").empty();
		$("#toolbar" + reportName + "gridtable")
				.append(
						"<div style='float:right;'><img id='"
								+ reportName
								+ "refresh' src='icons/refresh.png' style='width: 26px; align:left; cursor: pointer;height: 28px;' title='refresh ' class='gwt-Image'>");
		var gridVal = arrayListValues1.length;
		if (gridVal) {
			$("#toolbar" + reportName + "gridtable")
					.append(
							"<div style='float:right;'><img id='"
									+ reportName
									+ "print' src='icons/print.png' style='width: 22px; align: left;height: 20px; cursor: pointer;' title='Print ' class='gwt-Image'>"
									+ "<img id='"
									+ reportName
									+ "excel' src='icons/exportIcon.png' style='width: 26px; align:left; cursor: pointer;height: 28px;' title='Excel ' class='gwt-Image'>"
									+ "<img id='"
									+ reportName
									+ "pdf' src='icons/pdf_icon.png' style='width: 26px; align:left; cursor: pointer;height: 28px;' title='Pdf' class='gwt-Image'>");

		}
		if ((gridVal) && (type == 'grid')) {
			if (reportName != 'copytoproduction' || reportName != 'clothStock') {
				$("#toolbar" + reportName + "gridtable")
						.append(
								"<div style='float:right;'><img id='"
										+ reportName
										+ "edit' src='icons/Editor.png' style='width: 26px; align:left; cursor: pointer;height: 28px;' title='Edit' class='gwt-Image'>"
										+ "<img id='"
										+ reportName
										+ "delete' src='icons/delete.png' style='width: 26px; align:left; cursor: pointer;height: 28px;' title='delete'></div>");
			}

		}
		if (reportName == 'copytoproduction' || reportName == 'clothStock') {
			$("#toolbar" + reportName + "gridtable")
					.append(
							"<div style='float:right;'><img id='"
									+ reportName
									+ "new' src='icons/new.png' style='width: 26px; align:left; cursor: pointer;height: 28px;' title='New' class='gwt-Image'>"
									+ "<img id='"
									+ reportName
									+ "view' src='icons/view.png' style='width: 26px; align:left; cursor: pointer;height: 28px;' title='view'></div>");

		}
		$("#" + reportName + "print").jqxButton({
			theme : theme
		});

		$("#" + reportName + "resize").click(function() {
			$("#" + reportName + "gridtable").jqxGrid('autoresizecolumns');
		});

		$("#" + reportName + "clone").click(function() {
			tab = arrays[1];
			gridprocess(formName, reportName, "clone", rowid, tab);

		});

		$("#" + reportName + "excel").click(
				function() {

					var gridContent = $("#" + reportName + "gridtable")
							.jqxGrid('exportdata', 'html');

					exportReportFunction(gridContent, "xls");
				});

		$("#" + reportName + "print")
				.click(
						function() {
							var gridContent = $("#" + reportName + "gridtable")
									.jqxGrid('exportdata', 'html');
							var newWindow = window.open('', '',
									'width=800, height=500'), document = newWindow.document
									.open(), pageContent = '<!DOCTYPE html>\n'
									+ '<html>\n' + '<head>\n'
									+ '<meta charset="utf-8" />\n'
									+ '<title>Report</title>\n' + '</head>\n'
									+ '<body>\n' + gridContent
									+ '\n</body>\n</html>';
							document.write(pageContent);
							document.close();
							newWindow.print();
						});

		$("#" + reportName + "edit").click(function(event) {
			tab = arrays[1];
			$("#" + reportName + "gridtable").jqxGrid('clearselection');
			if (rowid) {
				gridprocess(formName, reportName, "edit", rowid, tab);
			} else {
				alert("Select any row");
			}
		});

		$("#" + reportName + "delete").click(function(event) {
			tab = arrays[1];
			if (rowid) {
				gridprocess(formName, reportName, "delete", rowid, tab);
			} else {
				alert("Select any row");
			}
		});

		$("#" + reportName + "view").click(function(event) {
			tab = arrays[1];
			if (rowid) {
				gridprocess(formName, reportName, "view", rowid, tab);
			} else {
				alert("Select any row");
			}
		});

		$("#" + reportName + "new").click(function(event) {
			tab = arrays[1];
			gridprocess(formName, reportName, "copyto", rowid, tab);
		});

		$("#" + reportName + "refresh").click(function(event) {
			tab = arrays[1];
			$("#" + reportName + "gridtable").jqxGrid('clearselection');
			gridprocess(formName, reportName, "refresh", arrays[0], tab);

		});

		$("#" + reportName + "gridtable").on(
				"columnclick",
				function(event) {
					var column = event.args.datafield;
					$("#" + reportName + "gridtable").jqxGrid(
							'autoresizecolumn', column);
				});

		$("#" + reportName + "gridtable")
				.bind(
						'rowselect',
						function(event) {
							var row = event.args.rowindex;
							datarow = $("#" + reportName + "gridtable")
									.jqxGrid('getrowdata', row);

							myJSONText = JSON.stringify(datarow, replacer);
							var value = [];
							for (key in datarow) {
								value.push(datarow[key]);
							}
							rowid = myJSONText;
							$("#" + reportName + "gridtable").jqxGrid(
									'clearselection');

						});

		$("#" + reportName + "gridtable").on('rowdoubleclick', function(event) {
			tab = arrays[1];
			gridprocess(formName, reportName, "edit", rowid, tab);
		});

		$("#" + reportName + "pdf").click(
				function() {
					var gridContent = $("#" + reportName + "gridtable")
							.jqxGrid('exportdata', 'html');

					exportReportFunction(gridContent, "pdf");

				});

		if (arg.chart == 'chart') {
			var settings = {
				title : reportName,
				enableAnimations : true,
				showLegend : true,
				padding : {
					left : 5,
					top : 5,
					right : 5,
					bottom : 5
				},
				titlePadding : {
					left : 90,
					top : 0,
					right : 0,
					bottom : 10
				},
				source : jqChart,
				categoryAxis : {
					dataField : xaxis,
					textRotationAngle : 90,
					tickMarksColor : '#ffffff',
					gridLinesColor : '#ffffff',
					valuesOnTicks : false
				},
				colorScheme : 'scheme01',
				seriesGroups : [ {
					type : 'column',
					valueAxis : {
						description : 'Amount',
						minValue : 0,
						tickMarksColor : '#ffffff'
					},
					series : json_chart1
				} ]
			};
			$("#" + reportName + "jqxChart").jqxChart(settings);
		}
		var listsource = new Array();
		for (var h = 0; h < arrayColumns.length; h++) {
			listsource.push({
				label : arrayColumns[h],
				value : arrayColumns[h],
				checked : true
			});
		}
	} else {
		var valueObject;
		$("#" + reportName + "gridtable").jqxGrid('clear');
		valueObject = JSON.parse(arg.datavalues);
		$("#" + reportName + "gridtable").jqxGrid('addrow', null, valueObject);
	}
}

function replacer(key, value) {
	if (typeof value === 'number' && !isFinite(value)) {
		return String(value);
	}
	return value;
}
function exportReportFunction(reportName, exportType) {

	callExportReport(reportName, exportType);

}