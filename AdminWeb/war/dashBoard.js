function checkDashboard(gridreportNames) {
	var reportName = gridreportNames.replace(/ +/g, "");
	var check1 = true;

	if ($("#" + reportName + "dboard").is(':empty')) {
		check1 = false;
	}
	return check1;
}

function callDashboardJs(isVerify, arg, gridreportNames, formName, arrays) {

	var source;
	var reportName = gridreportNames.replace(/ +/g, "");

	if (isVerify == 1) {

		var tab = '';
		var ChartData = [];

		var theme = 'energyblue';
		// var btnAdd = document.createElement("BUTTON");
		// var text = document.createTextNode("Add New");
		var datarow = null;
		var Width, Height;

		var rowid = null;
		var type = arg.displayType;

		var myJSONText = null;
		// btnAdd.appendChild(text);

		var arrayColumn = arg.listColumns;

		var arrayListValues1 = arg.valuesDtos[0].values;
		// var tempColumns = [];
		//
		// var arrayValues = arg.listColumns;
		// for ( var p = 0; p < arrayValues.length; p++) {
		// tempColumns.push(arrayValues[p]);
		// }

		var arrayFirst = [];
		for (var k = 0; k < arg.valuesDtos.length; k++) {

			arrayFirst.push(arg.valuesDtos[k].values);
		}
		var arrayColumns = [];
		var arrayColumnsFirst = arg.listColumns;

		for (var k = 0; k < arrayColumnsFirst.length; k++) {
			arrayColumns.push(arrayColumnsFirst[k]);
		}

		// may be chart start
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
		console.log('arraycolumns = ' + JSON.stringify(arrayColumns));
		for (var h = 0; h < arrayColumns.length; h++) {
			var json_column = {};
			json_column['text'] = arrayColumns[h];
			json_column['hidden'] = false;
			json_column['datafield'] = arrayColumns[h];
			json_column['width'] = 'auto';
			json_column['columntype'] = 'textbox';
			json_column['filtertype'] = 'textbox';
			json_column['filtercondition'] = 'starts_with';
			json_column['renderer'] = columnsrenderer;
			json_column['cellsrenderer'] = cellsrenderer;
			json_column['cellclassname'] = 'smallFont';

			json_columnValues.push(json_column);
		}

		Width = arg.gridwidth + 'px';
		Height = arg.gridheight + 'px';

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

		$("#" + reportName + "dboard").jqxGrid({

			width : Width,
			height : Height,
			theme : theme,
			source : dataAdapter,
			pageable : true,
			filterable : true,
			showfilterrow : true,
			showtoolbar : true,
			toolbarheight : 25,
			columns : json_columnValues,
			columnsresize : true,
			pagesize : 20,
			autorowheight : true,
			

		});

		/*
		 * Below lines to add the tootbar and icons actions
		 */

		$("#toolbar" + reportName + "dboard").empty();
		$("#toolbar" + reportName + "dboard")
				.append(
						"<div style='float:right;'><img id='"
								+ reportName
								+ "pdf' src='icons/pdf_icon.png' style='width: 20px; align:left; cursor: pointer;height: 20px;' title='Pdf' class='gwt-Image'>"
								+ "<img id='"
								+ reportName
								+ "refresh' src='icons/refresh.png' style='width: 20px; align:left; cursor: pointer;height: 20px;' title='refresh ' class='gwt-Image'>");

		$("#" + reportName + "refresh").click(function(event) {
			tab = arrays[1];

			$("#" + reportName + "dboard").jqxGrid('clearselection');

			dashboardRefresh(formName);

		});
		$("#" + reportName + "pdf").click(
				function() {

					var gridContent = $("#" + reportName + "dboard").jqxGrid(
							'exportdata', 'html');

					exportReportFunction(gridContent, "pdf");

				});
		/**
		 * --------------------------------------------------
		 * Ended-------------------------------------------
		 */

		for (var t = 0; t < sortingArray.length; t++) {

			$("#" + reportName + "dboard").jqxGrid('addgroup',
					sortingArray[t][1]);

		}

	} else {
		var valueObject;
		$("#" + reportName + "dboard").jqxGrid('clear');
		valueObject = JSON.parse(arg.datavalues);
		$("#" + reportName + "dboard").jqxGrid('addrow', null, valueObject);
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