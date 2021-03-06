package com.eiw.device.simulator;

import java.io.Serializable;

public class SimulatorData implements Serializable {

	static String IMEI_LENGTH = "15";

	static String[] IMEI_NOS = { "352848026736948", "356307041513945",
			"356307040950726", "356307040153958", "352848027240833",
			"356307040972373", "356307041582379", "352848029139082" };

	static String SIM1_IMEI_No = "868254030094175";// "013226008605764";//"352848027277447";//
													// 352848025843265";// "352848025977683";//
													// 352848027237508";// 353976014178671";//352848025709383
													// //352848026764908
													// 352848022748004

	// geofence
	// static String[][] SIM1_dataArray = {
	// { "13028809", "80272143", "10", "1=1" },
	// { "13028684", "80273280", "20", "1=1" },
	// { "13028872", "80275512", "30", "1=1" },
	// { "13028558", "80276499", "40", "1=1" },
	// { "13027659", "80276027", "50", "1=1" },
	// { "13023917", "80273709", "50", "1=1" },
	// { "13025130", "80268989", "50", "1=1" }
	//
	// };

	// freeform
	// static String[][] SIM1_dataArray = {
	// { "13024837", "80277400", "50", "1=1" },
	// { "13036669", "80278044", "10", "1=1" },
	// { "13035331", "80277658", "20", "1=1" },
	// { "13034182", "80277486", "30", "1=1" },
	// { "13033241", "80277421", "40", "1=1" },
	// { "13032238", "80278559", "50", "1=1" },
	// { "13029708", "80278666", "50", "1=1" },
	// { "13024837", "80277400", "50", "1=1" },
	// { "13024795", "80271778", "50", "1=1" }

	// road
	// static String[][] SIM1_dataArray = {
	// { "13018919", "80262905", "20",
	// "1=1,9=32000,10=50000,11=10000,69=0" },
	// { "13019034", "80264150", "90", "1=1,9=3600,10=42000,11=7500" },
	// { "13018705", "80261242", "10", "1=0,9=2200,10=30000,11=20000" },
	// { "130288", "802719", "0", "1=1,9=7000,10=25000,11=25500,69=0" },
	// { "130288", "80272", "10", "1=1,9=28000,10=20000,11=30000" },
	// { "130289", "802746", "10", "1=1,9=28000,10=15000,11=45000" }
	// };

	// { "13018921", "80262907", "60", "1=1,2=0,10=4350" },
	// { "13018922", "80262908", "80", "1=1,2=1,10=4200" },
	// { "13018923", "80262909", "0", "1=1,2=1,10=3500" },
	// { "13018924", "80262910", "0", "1=0,2=1,10=2800" },
	// { "13018925", "80262911", "0", "1=0,2=1,10=1500" },
	// { "13018926", "80262912", "0", "1=1,2=1,10=650" },
	// { "13018927", "80262913", "0", "1=1,2=1,10=120" },
	// { "13018928", "80262914", "0", "1=1,2=0,10=100" },
	// { "13018929", "80262915", "20", "1=1,10=140" }, };

	// static String[][] SIM1_dataArray = {
	// { "13084327", "80270426", "40", "1=1,2=0,9=4500,199=1914587" },
	// { "13082948", "80270598", "90", "1=1,2=0,9=4800" },
	// { "13081945", "80270727", "50", "1=1,2=0,9=4700,199=1914858" },
	// { "13080816", "80270898", "120", "1=1,2=0,9=5200,199=1915015" },
	// { "13081318", "802721", "160", "1=1,2=1,9=5300,199=1915365" },
	// { "13081359", "80272486", "85", "1=1,2=1,9=4000,199=1915961" },
	// { "13081276", "80273345", "70", "1=1,2=1,9=2500" },
	// { "13081485", "80274503", "60", "1=1,2=1,9=500" },
	// { "13081610", "80275319", "98", "1=1,2=1,9=100" },
	// { "13081777", "80276306", "10", "1=1,2=0,9=400" },
	// { "13081945", "80277379", "20", "1=1,2=0,9=500" },
	// { "13082028", "80278709", "30", "1=1,2=0,9=1000" },
	// { "13082195", "80280297", "0", "1=0,2=0,9=1500" },
	// { "13082655", "80282013", "0", "1=0,2=0,9=1800" },
	// { "13084829", "80283601", "0", "1=0,2=0,9=2000" },
	// { "13085289", "8028549", "0", "1=0,2=0,9=6000" },
	// { "13085163", "80287893", "0", "1=0,2=0,9=7500" },
	// { "13084202", "8028918", "85", "1=1,2=1,9=4000,199=1915961" },
	// { "13082613", "80288966", "70", "1=1,2=1,9=2500" },
	// { "13080189", "8028815", "60", "1=1,2=1,9=500" },
	// { "13078391", "8028785", "98", "1=1,2=1,9=100" },
	// { "13076385", "80286992", "10", "1=1,2=0,9=400" },
	// { "13074211", "80286133", "20", "1=1,2=0,9=500" },
	// { "13072121", "80284417", "85", "1=1,2=1,9=4000,199=1915961" },
	// { "13066059", "80283902", "70", "1=1,2=1,9=2500" },
	// { "13063886", "80283387", "60", "1=1,2=1,9=500" },
	// { "13061210", "80282786", "98", "1=1,2=1,9=100" },
	// { "13058953", "80282185", "10", "1=1,2=0,9=400" },
	// { "13055399", "80281627", "20", "1=1,2=0,9=500" },
	// { "13052305", "80281026", "30", "1=1,2=0,9=1000" },
	// { "13050299", "80280812", "60", "1=1,2=1,9=500" },
	// { "13048543", "8028059", "98", "1=1,2=1,9=100" },
	// { "13045491", "80280039", "10", "1=1,2=0,9=400" },
	// { "13043275", "8027961", "20", "1=1,2=0,9=500" },
	// { "13040516", "80279192", "30", "1=1,2=0,9=1000" },
	// { "13037602", "80278364", "40", "1=1,2=0,9=1500" },
	// { "13036724", "80278198", "0", "1=0,2=0,9=1800" },
	// { "13036724", "80278198", "0", "1=0,2=0,9=2000" },
	// { "13036724", "80278198", "0", "1=0,2=0,9=6000" }, };

	static String[][] SIM1_dataArray = {
			{ "13094067", "80286176", "40", "1=1,2=0,9=4500,199=1914587,253=1", "0" },
			{ "13095195", "80279825", "50", "1=1,2=0,9=4800,253=2", "15" },
			{ "13092646", "80279396", "60", "1=1,2=0,9=4700,199=1914858,253=1", "30" },
			{ "13092562", "80275147", "70", "1=1,2=0,9=5200,199=1915015,253=1", "45" },
			{ "13088173", "80275683", "80", "1=1,2=1,9=5300,199=1915365,253=1", "60" },
			{ "13081736", "80276284", "70", "1=1,2=1,9=4000,199=1915961,253=2", "75" },
			{ "13080482", "8026341", "90", "1=1,10=32000,9=6200,11=10000,69=0,253=2",
					"90" },
			{ "13078559", "80251479", "60",
					"1=1,10=32000,9=6500,11=10000,69=0,253=3", "105" },
			{ "13077054", "80240579", "50",
					"1=1,10=32000,9=5500,11=10000,69=0,253=3", "120" },
			{ "13066018", "80243411", "40",
					"1=1,10=32000,9=5100,11=10000,69=0,253=3", "150" },
			{ "13056904", "80242295", "70",
					"1=1,10=32000,9=5000,11=10000,69=0", "180" },
			{ "13063593", "80235257", "80",
					"1=1,10=32000,9=6500,11=10000,69=0", "210" },
			{ "13069780", "80224099", "80",
					"1=1,10=32000,9=5500,11=10000,69=0,253=3", "240" },
			{ "13072873", "80214915", "70",
					"1=1,10=32000,9=5100,11=10000,69=0", "270" },
			{ "13075967", "80198951", "50",
					"1=1,10=32000,9=5000,11=10000,69=0,253=3", "300" },
			{ "13072205", "80188136", "60",
					"1=1,10=32000,9=6500,11=10000,69=0", "30" } };

	static int SIM1_interval = 60000; // 352848022776575

	// IO test
	// static String[][] SIM1_dataArray = {
	// { "13028725440088149", "8027246475219727", "50",
	// "1=0,9=1000,72=1000,73=521,74=455,10=1200,11=3000,2=0,3=1,66=24500" },
	// { "13028725440088149", "8027364492416382", "50",
	// "1=1,9=40,72=1000,73=521,74=455,10=1200,11=3000,2=0,3=1,66=27589" },
	// { "13028788155952451", "8027516841888428", "30",
	// "1=1,155=1,9=60,72=1000,73=521,74=455,10=1200,11=3000,2=0,3=1,66=23587"
	// },
	// { "13028746345378005", "8027591943740845", "50",
	// "1=0,9=55,72=1000,73=521,74=455,10=1200,11=3000,2=0,3=1,66=21024" },
	// { "13028307333920166", "8027628421783447", "60",
	// "1=1,155=0,9=70,72=1000,73=521,74=455,10=1200,11=3000,2=0,3=1" },
	// { "13027742889473535", "8027617692947388", "50", "1=1,9=80" },
	// { "13027073916611478", "8027579069137573", "70",
	// "1=1,156=1,9=10,72=1000,73=521,74=455,10=1200,11=3000,2=0,3=1,66=11587"
	// },
	// { "1303838349600592", "8027862310409546", "50", "1=1,9=5" } };
	//
	// static int SIM1_interval = 10000;

	// 123456789
	static String SIM2_IMEI_No = "354718074621839";// "353976014178671";//
													// "352848026764908";//
													// "645601234123456";
	static String[][] SIM2_dataArray = {
			{ "13094067", "80286176", "0", "1=0,2=0,9=4500,199=1914587", "0" },
			{ "13095195", "80279825", "90", "1=0,2=0,9=4800", "15" },
			{ "13092646", "80279396", "60", "1=0,2=0,9=4700,199=1914858", "30" },
			{ "13092562", "80275147", "56", "1=1,2=0,9=5200,199=1915015", "45" },
			{ "13088173", "80275683", "45", "1=1,2=1,9=5300,199=1915365", "60" },
			{ "13081736", "80276284", "34", "1=0,2=1,9=4000,199=1915961", "75" },
			{ "13080482", "8026341", "77", "1=1,10=32000,9=6200,11=10000,69=0",
					"90" },
			{ "13078559", "80251479", "63",
					"1=1,10=32000,9=6500,11=10000,69=0", "105" },
			{ "13077054", "80240579", "40",
					"1=1,10=32000,9=5500,11=10000,69=0", "120" },
			{ "13066018", "80243411", "70",
					"1=1,10=32000,9=5100,11=10000,69=0", "150" },
			{ "13056904", "80242295", "30",
					"1=1,10=32000,9=5000,11=10000,69=0", "180" },
			{ "13063593", "80235257", "63",
					"1=1,10=32000,9=6500,11=10000,69=0", "210" },
			{ "13069780", "80224099", "40",
					"1=1,10=32000,9=5500,11=10000,69=0", "240" },
			{ "13072873", "80214915", "70",
					"1=1,10=32000,9=5100,11=10000,69=0", "270" },
			{ "13075967", "80198951", "30",
					"1=1,10=32000,9=5000,11=10000,69=0", "300" },
			{ "13072205", "80188136", "63",
					"1=1,10=32000,9=6500,11=10000,69=0", "30" },
			{ "13069947", "8018127", "40", "1=1,10=32000,9=5500,11=10000,69=0",
					"300" },
			{ "13066770", "80174918", "70",
					"1=1,10=32000,9=5100,11=10000,69=0", "45" },
			{ "13061839", "80161666", "30",
					"1=1,10=32000,9=5000,11=10000,69=0", "150" },
			{ "13055495", "80157479", "0", "1=1,10=32000,9=6200,11=10000,69=0",
					"180" } };

	// static String[][] SIM2_dataArray = {
	// { "25230003", "51431487", "50", "100", "1", "3", "99", "96", "1",
	// "4", "99", "154", "102" },
	// { "25232119", "51431379", "23", "95", "1", "4", "94", "92", "2",
	// "4", "89", "94", "152" },
	// { "2523406", "51431422", "43", "91", "1", "1", "97", "91", "3",
	// "4", "79", "94", "152" },
	// { "25236428", "51431401", "13", "86", "2", "5", "98", "91", "2",
	// "3", "88", "154", "102" },
	// { "25238505", "51431379", "44", "83", "2", "1", "89", "75", "5",
	// "4", "93", "94", "152" },
	// { "25240465", "51431444", "97", "78", "2", "2", "89", "95", "3",
	// "2", "88", "94", "152" },
	// { "25242697", "51431379", "76", "72", "2", "3", "99", "96", "1",
	// "4", "99", "154", "102" },
	// { "25244851", "51431358", "46", "69", "2", "1", "89", "95", "2",
	// "5", "89", "94", "152" },
	// { "25247297", "51431229", "42", "65", "2", "4", "94", "92", "2",
	// "4", "89", "94", "152" },
	// { "25249703", "51431315", "73", "61", "2", "1", "97", "91", "3",
	// "4", "79", "154", "102" },
	// { "25251081", "51431186", "73", "58", "1", "2", "89", "95", "3",
	// "2", "88", "94", "152" },
	// { "25251489", "51430199", "42", "55", "1", "1", "89", "95", "2",
	// "5", "89", "94", "152" },
	// { "25250945", "51429298", "42", "49", "1", "2", "98", "91", "1",
	// "3", "69", "154", "102" },
	// { "25250363", "51428204", "76", "46", "1", "4", "94", "92", "2",
	// "4", "89", "94", "152" },
	// { "25249587", "5142668", "42", "45", "1", "5", "98", "91", "2",
	// "3", "88", "94", "152" },
	// { "25249024", "51425757", "0", "45", "0", "1", "97", "91", "3",
	// "4", "79", "154", "102" },
	// { "25249024", "51425757", "0", "45", "0", "1", "89", "75", "5",
	// "4", "93", "94", "152" },
	// { "25249024", "51425757", "0", "45", "0", "2", "89", "95", "3",
	// "2", "88", "94", "152" },
	// { "25249024", "51425757", "0", "45", "0", "4", "94", "92", "2",
	// "4", "89", "154", "102" },
	// { "25249024", "51425757", "0", "45", "0", "1", "97", "91", "3",
	// "4", "79", "94", "152" },
	// { "25249024", "51425757", "0", "45", "3", "1", "89", "95", "2",
	// "5", "89", "94", "152" },
	// { "25249024", "51425757", "0", "45", "3", "3", "99", "96", "1",
	// "4", "99", "154", "102" },
	// { "25249024", "51425757", "0", "45", "3", "2", "98", "91", "1",
	// "3", "69", "94", "152" },
	// { "25249024", "51425757", "0", "45", "3", "4", "94", "92", "2",
	// "4", "89", "154", "102" },
	// { "25249024", "51425757", "0", "45", "3", "2", "89", "95", "3",
	// "2", "88", "94", "152" },
	// { "25250344", "5141608", "97", "40", "1", "1", "89", "95", "2",
	// "5", "89", "154", "102" },
	// { "2525048", "51415222", "30", "35", "1", "3", "99", "96", "1",
	// "4", "99", "94", "152" },
	// { "25251101", "51414127", "30", "31", "1", "5", "98", "91", "2",
	// "3", "88", "154", "102" },
	// { "25251625", "51413097", "30", "27", "1", "1", "97", "91", "3",
	// "4", "79", "94", "152" },
	// { "25252634", "51411703", "97", "22", "1", "4", "94", "92", "2",
	// "4", "89", "94", "152" },
	// { "25253391", "51410522", "20", "17", "1", "1", "89", "75", "5",
	// "4", "93", "94", "152" },
	// { "25254186", "51409321", "30", "13", "1", "2", "89", "95", "3",
	// "2", "88", "154", "102" },
	// { "25254652", "51408505", "46", "10", "1", "4", "94", "92", "2",
	// "4", "89", "154", "102" },
	// { "25255331", "51408291", "97", "07", "1", "3", "99", "96", "1",
	// "4", "99", "94", "152" },
	// { "25256399", "51409128", "46", "06", "1", "1", "97", "91", "3",
	// "4", "79", "154", "102" },
	// { "25257447", "51409321", "30", "05", "1", "1", "89", "75", "5",
	// "4", "93", "94", "152" },
	// { "25258203", "51409299", "46", "03", "1", "5", "98", "91", "2",
	// "3", "88", "154", "102" },
	// { "25260571", "51408806", "20", "02", "1", "2", "89", "95", "3",
	// "2", "88", "154", "102" } };
	static int SIM2_interval = 20000;

	static String SIM3_IMEI_No = "352848022776575";
	static String[][] SIM3_dataArray = {
			{ "13019020", "80262929", "40", "2=1,3=0,22=4500,65=1914587" },
			{ "13018936", "80262940", "70", "2=1,3=0,22=4800" },
			{ "13018717", "80262924", "120", "2=1,3=0,22=4700,65=1914858" },
			{ "13018597", "80262913", "20", "2=1,3=0,22=5200,65=1915015" },
			{ "13018456", "80262903", "90", "2=1,3=1,22=5300,65=1915365" },
			{ "13018278", "80262903", "0", "2=0,3=1,22=4000,65=1915961" },
			{ "13018278", "80262903", "0", "2=1,23=32000,22=6200,69=0" },
			{ "13018100", "80262887", "63", "2=1,23=32000,22=6500,69=0" },
			{ "13017854", "80262881", "40", "2=1,23=32000,22=5500,69=0" },
			{ "13017504", "80262833", "70", "2=1,23=32000,22=5100,69=0" },
			{ "13017180", "80262838", "30", "2=1,23=32000,22=5000,69=0" } };
	static int SIM3_interval = 60000;

	static String SIM4_IMEI_No = "013226008605764";
	static String[][] SIM4_dataArray = {
			{ "24020546", "38178252", "21", "100", "1", "1", "89", "95", "2",
					"5", "89", "94", "152" },
			{ "24020718", "38178724", "45", "97", "1", "2", "89", "95", "3",
					"2", "88", "94", "152" },
			{ "2402088", "38179148", "59", "94", "1", "4", "91", "88", "5",
					"3", "87", "94", "152" },
			{ "24020997", "3817948", "71", "91", "1", "1", "97", "91", "3",
					"4", "79", "154", "102" },
			{ "24021183", "38179362", "45", "88", "1", "5", "98", "91", "2",
					"3", "88", "94", "152" },
			{ "24021512", "38179223", "38", "85", "2", "4", "94", "92", "2",
					"4", "89", "94", "152" },
			{ "24021992", "38178998", "82", "82", "2", "4", "91", "88", "5",
					"3", "87", "154", "102" },
			{ "24023026", "38180623", "29", "80", "2", "1", "89", "75", "5",
					"4", "93", "94", "152" },
			{ "24021693", "38182511", "60", "77", "2", "2", "89", "95", "3",
					"2", "88", "154", "102" },
			{ "24021928", "381844", "21", "73", "2", "3", "99", "96", "1", "4",
					"99", "154", "102" },
			{ "24022046", "38186889", "45", "70", "2", "1", "97", "91", "3",
					"4", "79", "154", "102" },
			{ "24022007", "38189678", "59", "69", "1", "4", "91", "88", "5",
					"3", "87", "154", "102" },
			{ "24022124", "38193154", "88", "65", "1", "5", "98", "91", "2",
					"3", "88", "154", "102" },
			{ "24022242", "38195858", "21", "62", "1", "1", "89", "95", "2",
					"5", "89", "94", "152" },
			{ "24022607", "38196048", "82", "59", "1", "1", "89", "75", "5",
					"4", "93", "154", "102" },
			{ "24022842", "38196016", "45", "56", "1", "1", "97", "91", "3",
					"4", "79", "94", "152" },
			{ "24023087", "38196003", "21", "54", "1", "2", "89", "95", "3",
					"2", "88", "154", "102" },
			{ "24023087", "38196003", "0", "54", "0", "4", "91", "88", "5",
					"3", "87", "94", "152" },
			{ "24023087", "38196003", "0", "54", "0", "5", "98", "91", "2",
					"3", "88", "94", "152" },
			{ "24023087", "38196003", "0", "54", "0", "1", "97", "91", "3",
					"4", "79", "154", "102" },
			{ "24023087", "38196003", "0", "54", "0", "3", "99", "96", "1",
					"4", "99", "94", "152" },
			{ "24023087", "38196003", "0", "54", "0", "4", "94", "92", "2",
					"4", "89", "154", "102" },
			{ "24023087", "38196003", "0", "54", "3", "5", "98", "91", "2",
					"3", "88", "94", "152" },
			{ "24023087", "38196003", "0", "54", "3", "4", "91", "88", "5",
					"3", "87", "154", "102" },
			{ "24023087", "38196003", "0", "54", "3", "2", "89", "95", "3",
					"2", "88", "94", "152" },
			{ "24023087", "38196003", "0", "54", "3", "1", "97", "91", "3",
					"4", "79", "94", "152" },
			{ "24023087", "38196003", "0", "54", "3", "1", "89", "75", "5",
					"4", "93", "154", "102" },
			{ "24023087", "38196003", "0", "54", "3", "5", "98", "91", "2",
					"3", "88", "94", "152" },
			{ "24025985", "38195721", "59", "48", "1", "4", "91", "88", "5",
					"3", "87", "154", "102" },
			{ "24026225", "38195705", "88", "42", "1", "3", "99", "96", "1",
					"4", "99", "94", "152" },
			{ "2402647", "3819567", "21", "38", "1", "1", "97", "91", "3", "4",
					"79", "154", "102" },
			{ "24026691", "38195662", "21", "33", "1", "2", "89", "95", "3",
					"2", "88", "94", "152" },
			{ "24026926", "38195633", "82", "27", "1", "5", "98", "91", "2",
					"3", "88", "154", "102" },
			{ "24027217", "38195601", "60", "21", "1", "4", "94", "92", "2",
					"4", "89", "94", "152" },
			{ "2402745", "38195584", "45", "18", "1", "1", "89", "75", "5",
					"4", "93", "154", "102" },
			{ "24027675", "3819555", "21", "12", "1", "4", "91", "88", "5",
					"3", "87", "94", "152" },
			{ "24027965", "38195531", "82", "08", "1", "1", "97", "91", "3",
					"4", "79", "154", "102" },
			{ "24028631", "38195469", "45", "02", "1", "3", "99", "96", "1",
					"4", "99", "154", "102" } };

	static int SIM4_interval = 60000;

	static String SIM5_IMEI_No = "356307045790127";
	static String[][] SIM5_dataArray = {
			{ "24324339", "5453064", "45", "100", "1", "2", "89", "95", "3",
					"2", "88", "94", "152" },
			{ "24324261", "54536819", "82", "95", "1", "1", "97", "91", "3",
					"4", "79", "154", "102" },
			{ "24329188", "54539137, 21", "90", "1", "4", "91", "88", "5", "3",
					"87", "94", "152" },
			{ "24333881", "54538965", "60", "88", "1", "1", "89", "75", "5",
					"4", "93", "154", "102" },
			{ "24337791", "54538364, 54", "85", "1", "3", "99", "96", "1", "4",
					"99", "94", "152" },
			{ "24337556", "5453476", "87", "83", "1", "4", "94", "92", "2",
					"4", "89", "154", "102" },
			{ "243374", "54530554, 49", "79", "1", "2", "89", "95", "3", "2",
					"88", "94", "152" },
			{ "24341153", "54529438", "21", "77", "1", "1", "97", "91", "3",
					"4", "79", "94", "152" },
			{ "24341153", "54529438", "60", "73", "2", "4", "91", "88", "5",
					"3", "87", "154", "102" },
			{ "24351085", "54528837", "45", "71", "2", "5", "98", "91", "2",
					"3", "88", "94", "152" },
			{ "24357418", "5452858", "39", "68", "2", "1", "89", "75", "5",
					"4", "93", "154", "102" },
			{ "24362892", "54528494", "21", "65", "2", "4", "94", "92", "2",
					"4", "89", "94", "152" },
			{ "24368599", "54527979", "49", "62", "2", "4", "91", "88", "5",
					"3", "87", "154", "102" },
			{ "24372665", "5452858", "85", "58", "2", "2", "89", "95", "3",
					"2", "88", "94", "152" },
			{ "24373446", "54525404", "79", "56", "1", "1", "97", "91", "3",
					"4", "79", "154", "102" },
			{ "24372899", "54519825", "39", "55", "1", "1", "89", "95", "2",
					"5", "89", "94", "152" },
			{ "24373525", "54516821", "76", "53", "1", "4", "91", "88", "5",
					"3", "87", "154", "102" },
			{ "24376808", "54515705", "60", "50", "1", "1", "89", "75", "5",
					"4", "93", "94", "152" },
			{ "24381186", "54515705", "82", "48", "1", "5", "98", "91", "2",
					"3", "88", "154", "102" },
			{ "24382124", "54520941", "56", "45", "1", "4", "94", "92", "2",
					"4", "89", "94", "152" },
			{ "24382124", "54520941", "0", "45", "0", "4", "91", "88", "5",
					"3", "87", "154", "102" },
			{ "24382124", "54520941", "0", "45", "0", "2", "89", "95", "3",
					"2", "88", "94", "152" },
			{ "24382124", "54520941", "0", "45", "0", "1", "89", "75", "5",
					"4", "93", "154", "102" },
			{ "24382124", "54520941", "0", "45", "0", "3", "99", "96", "1",
					"4", "99", "94", "152" },
			{ "24382124", "54520941", "0", "45", "0", "4", "94", "92", "2",
					"4", "89", "154", "102" },
			{ "24382124", "54520941", "0", "45", "3", "4", "91", "88", "5",
					"3", "87", "94", "152" },
			{ "24382124", "54520941", "0", "45", "3", "1", "97", "91", "3",
					"4", "79", "154", "102" },
			{ "24382124", "54520941", "0", "45", "3", "2", "89", "95", "3",
					"2", "88", "154", "102" },
			{ "24382124", "54520941", "0", "45", "3", "1", "89", "75", "5",
					"4", "93", "94", "152" },
			{ "24400572", "54529181", "45", "11", "1", "4", "91", "88", "5",
					"3", "87", "154", "102" },
			{ "24404402", "54525919", "21", "06", "1", "1", "97", "91", "3",
					"4", "79", "154", "102" } };
	static int SIM5_interval = 60000;
	static String SIM6_IMEI_No = "356307045815130";
	static String[][] SIM6_dataArray = {
			{ "27020546", "38178252", "21", "100", "1", "2", "89", "95", "3",
					"2", "88", "94", "152" },
			{ "27020718", "38178724", "45", "97", "1", "1", "97", "91", "3",
					"4", "79", "154", "102" },
			{ "2702088", "38179148", "59", "94", "1", "4", "91", "88", "5",
					"3", "87", "94", "152" },
			{ "27020997", "3817948", "71", "91", "1", "3", "99", "96", "1",
					"4", "99", "154", "102" },
			{ "27021183", "38179362", "45", "88", "1", "1", "89", "75", "5",
					"4", "93", "94", "152" },
			{ "27021512", "38179223", "38", "85", "2", "4", "91", "88", "5",
					"3", "87", "154", "102" },
			{ "27021992", "38178998", "82", "82", "2", "3", "99", "96", "1",
					"4", "99", "94", "152" },
			{ "27023026", "38180623", "29", "80", "2", "5", "98", "91", "2",
					"3", "88", "154", "102" },
			{ "27021693", "38182511", "60", "77", "2", "1", "89", "75", "5",
					"4", "93", "94", "152" },
			{ "27021928", "381844", "21", "73", "2", "2", "89", "95", "3", "2",
					"88", "154", "102" },
			{ "27022046", "38186889", "45", "70", "2", "3", "99", "96", "1",
					"4", "99", "154", "102" },
			{ "27022007", "38189678", "59", "69", "1", "1", "97", "91", "3",
					"4", "79", "154", "102" },
			{ "27022124", "38193154", "88", "65", "1", "5", "98", "91", "2",
					"3", "88", "94", "152" },
			{ "27022242", "38195858", "21", "62", "1", "4", "91", "88", "5",
					"3", "87", "154", "102" },
			{ "27022607", "38196048", "82", "59", "1", "4", "94", "92", "2",
					"4", "89", "94", "152" },
			{ "27022842", "38196016", "45", "56", "1", "1", "89", "95", "2",
					"5", "89", "154", "102" },
			{ "27023087", "38196003", "21", "54", "1", "1", "89", "75", "5",
					"4", "93", "94", "152" },
			{ "27023087", "38196003", "0", "54", "0", "2", "89", "95", "3",
					"2", "88", "154", "102" },
			{ "27023087", "38196003", "0", "54", "0", "3", "99", "96", "1",
					"4", "99", "94", "152" },
			{ "27023087", "38196003", "0", "54", "0", "5", "98", "91", "2",
					"3", "88", "154", "102" },
			{ "27023087", "38196003", "0", "54", "0", "1", "97", "91", "3",
					"4", "79", "94", "152" },
			{ "27023087", "38196003", "0", "54", "0", "4", "91", "88", "5",
					"3", "87", "154", "102" },
			{ "27023087", "38196003", "0", "54", "3", "1", "89", "95", "2",
					"5", "89", "154", "102" },
			{ "27023087", "38196003", "0", "54", "3", "1", "89", "75", "5",
					"4", "93", "94", "152" },
			{ "27023087", "38196003", "0", "54", "3", "2", "98", "91", "1",
					"3", "69", "154", "102" },
			{ "27023087", "38196003", "0", "54", "3", "2", "89", "95", "3",
					"2", "88", "94", "152" },
			{ "27023087", "38196003", "0", "54", "3", "4", "91", "88", "5",
					"3", "87", "154", "102" },
			{ "27023087", "38196003", "0", "54", "3", "4", "94", "92", "2",
					"4", "89", "94", "152" },
			{ "27025985", "38195721", "59", "48", "1", "5", "98", "91", "2",
					"3", "88", "94", "152" },
			{ "27026225", "38195705", "88", "42", "1", "1", "89", "95", "2",
					"5", "89", "94", "152" },
			{ "2702647", "3819567", "21", "38", "1", "1", "97", "91", "3", "4",
					"79", "154", "102" },
			{ "27026691", "38195662", "21", "33", "1", "2", "89", "95", "3",
					"2", "88", "94", "152" },
			{ "27026926", "38195633", "82", "27", "1", "3", "99", "96", "1",
					"4", "99", "154", "102" },
			{ "27027217", "38195601", "60", "21", "1", "1", "89", "75", "5",
					"4", "93", "94", "152" },
			{ "2702745", "38195584", "45", "18", "1", "5", "98", "91", "2",
					"3", "88", "154", "102" },
			{ "27027675", "3819555", "21", "12", "1", "4", "91", "88", "5",
					"3", "87", "94", "152" },
			{ "27027965", "38195531", "82", "08", "1", "3", "99", "96", "1",
					"4", "99", "94", "152" },
			{ "27028631", "38195469", "45", "02", "1", "2", "89", "95", "3",
					"2", "88", "154", "102" } };

	static int SIM6_interval = 60000;
	static String SIM7_IMEI_No = "356307041582379";
	static String[][] SIM7_dataArray = {
			{ "22281102", "50061264", "48", "100", "1", "1", "97", "91", "3",
					"4", "79", "154", "102" },
			{ "2228926", "50052617", "32", "97", "1", "4", "91", "88", "5",
					"3", "87", "94", "152" },
			{ "22290645", "50051737", "23", "95", "1", "2", "89", "95", "3",
					"2", "88", "154", "102" },
			{ "22291818", "50051007", "56", "93", "1", "1", "89", "75", "5",
					"4", "93", "94", "152" },
			{ "22293242", "50050321", "56", "90", "1", "5", "98", "91", "2",
					"3", "88", "154", "102" },
			{ "22294839", "50049655", "14", "88", "2", "3", "99", "96", "1",
					"4", "99", "94", "152" },
			{ "22296281", "50048926", "39", "85", "2", "4", "91", "88", "5",
					"3", "87", "154", "102" },
			{ "22297589", "50048282", "23", "80", "2", "1", "89", "95", "2",
					"5", "89", "94", "152" },
			{ "22298936", "5004766", "44", "78", "2", "4", "94", "92", "2",
					"4", "89", "154", "102" },
			{ "2230036", "50046995", "41", "75", "2", "1", "89", "75", "5",
					"4", "93", "94", "152" },
			{ "22301802", "50046394", "23", "71", "2", "2", "89", "95", "3",
					"2", "88", "154", "102" },
			{ "22303457", "50045664", "31", "69", "1", "4", "91", "88", "5",
					"3", "87", "154", "102" },
			{ "22304822", "50045021", "39", "65", "1", "5", "98", "91", "2",
					"3", "88", "154", "102" },
			{ "22306111", "50045364", "49", "63", "1", "3", "99", "96", "1",
					"4", "99", "154", "102" },
			{ "22307304", "50046136", "23", "60", "1", "1", "89", "95", "2",
					"5", "89", "154", "102" },
			{ "22308612", "50047295", "23", "57", "1", "2", "89", "95", "3",
					"2", "88", "94", "152" },
			{ "22314594", "50053003", "49", "55", "1", "4", "91", "88", "5",
					"3", "87", "154", "102" },
			{ "22315979", "50054612", "37", "52", "1", "3", "99", "96", "1",
					"4", "99", "94", "152" },
			{ "22315985", "50054712", "37", "52", "1", "1", "97", "91", "3",
					"4", "79", "154", "102" },
			{ "22316801", "50054652", "52", "49", "4", "5", "98", "91", "2",
					"3", "88", "94", "152" },
			{ "22316991", "50054612", "60", "48", "4", "2", "89", "95", "3",
					"2", "88", "154", "102" },
			{ "22317979", "50054612", "54", "45", "4", "4", "91", "88", "5",
					"3", "87", "94", "152" },
			{ "22318279", "50054612", "38", "42", "4", "1", "89", "75", "5",
					"4", "93", "154", "102" },
			{ "22318879", "50054612", "12", "40", "4", "4", "94", "92", "2",
					"4", "89", "154", "102" },
			{ "22319579", "50054612", "71", "37", "4", "1", "97", "91", "3",
					"4", "79", "94", "152" },
			{ "22325979", "50054612", "45", "35", "4", "2", "89", "95", "3",
					"2", "88", "154", "102" },
			{ "22326379", "50054612", "42", "32", "4", "4", "91", "88", "5",
					"3", "87", "94", "152" },
			{ "22326879", "50054612", "47", "31", "4", "5", "98", "91", "2",
					"3", "88", "154", "102" },
			{ "22326711", "50076928", "23", "30", "1", "1", "89", "95", "2",
					"5", "89", "94", "152" },
			{ "22327884", "50080769", "23", "29", "1", "1", "89", "75", "5",
					"4", "93", "154", "102" },
			{ "22328384", "500824", "41", "32", "1", "2", "89", "95", "3", "2",
					"88", "94", "152" },
			{ "22329018", "50084567", "47", "27", "1", "1", "97", "91", "3",
					"4", "79", "154", "102" },
			{ "22329634", "50086391", "23", "22", "1", "5", "98", "91", "2",
					"3", "88", "154", "102" },
			{ "222330441", "50088966", "23", "17", "1", "3", "99", "96", "1",
					"4", "99", "154", "102" },
			{ "22331057", "50091176", "32", "11", "1", "4", "91", "88", "5",
					"3", "87", "94", "152" },
			{ "22331692", "50093172", "40", "08", "1", "4", "94", "92", "2",
					"4", "89", "94", "152" },
			{ "22332365", "50095146", "48", "04", "1", "1", "89", "75", "5",
					"4", "93", "154", "102" },
			{ "22332788", "50096669", "36", "02", "1", "4", "91", "88", "5",
					"3", "87", "154", "102" } };
	static int SIM7_interval = 60000;
	static String SIM8_IMEI_No = "356307045671186";// 645601234123456
	static String[][] SIM8_dataArray = {
			{ "23230003", "51431487", "50", "100", "1", "4", "91", "88", "5",
					"3", "87", "154", "102" },
			{ "23232119", "51431379", "23", "95", "1", "1", "97", "91", "3",
					"4", "79", "94", "152" },
			{ "2323406", "51431422", "43", "91", "1", "2", "89", "95", "3",
					"2", "88", "154", "102" },
			{ "23236428", "51431401", "13", "86", "2", "4", "91", "88", "5",
					"3", "87", "94", "152" },
			{ "23238505", "51431379", "44", "83", "2", "3", "99", "96", "1",
					"4", "99", "94", "152" },
			{ "23240465", "51431444", "97", "78", "2", "1", "97", "91", "3",
					"4", "79", "94", "152" },
			{ "23242697", "51431379", "76", "72", "2", "5", "98", "91", "2",
					"3", "88", "94", "152" },
			{ "23244851", "51431358", "46", "69", "2", "2", "89", "95", "3",
					"2", "88", "94", "152" },
			{ "23247297", "51431229", "42", "65", "2", "4", "94", "92", "2",
					"4", "89", "94", "152" },
			{ "23249703", "51431315", "73", "61", "2", "1", "89", "75", "5",
					"4", "93", "154", "102" },
			{ "23251081", "51431186", "73", "58", "1", "1", "97", "91", "3",
					"4", "79", "94", "152" },
			{ "23251489", "51430199", "42", "55", "1", "3", "99", "96", "1",
					"4", "99", "154", "102" },
			{ "23250945", "51429298", "42", "49", "1", "4", "91", "88", "5",
					"3", "87", "154", "102" },
			{ "23250363", "51428204", "76", "46", "1", "5", "98", "91", "2",
					"3", "88", "94", "152" },
			{ "23249587", "5142668", "42", "45", "1", "4", "91", "88", "5",
					"3", "87", "94", "152" },
			{ "23249024", "51425757", "0", "45", "0", "2", "89", "95", "3",
					"2", "88", "154", "102" },
			{ "23249024", "51425757", "0", "45", "0", "3", "99", "96", "1",
					"4", "99", "154", "102" },
			{ "23249024", "51425757", "0", "45", "0", "1", "97", "91", "3",
					"4", "79", "94", "152" },
			{ "23249024", "51425757", "0", "45", "0", "4", "91", "88", "5",
					"3", "87", "154", "102" },
			{ "23249024", "51425757", "0", "45", "0", "5", "98", "91", "2",
					"3", "88", "154", "102" },
			{ "23249024", "51425757", "0", "45", "3", "4", "94", "92", "2",
					"4", "89", "154", "102" },
			{ "23249024", "51425757", "0", "45", "3", "1", "89", "75", "5",
					"4", "93", "94", "152" },
			{ "23249024", "51425757", "0", "45", "3", "4", "91", "88", "5",
					"3", "87", "154", "102" },
			{ "23249024", "51425757", "0", "45", "3", "2", "89", "95", "3",
					"2", "88", "94", "152" },
			{ "23249024", "51425757", "0", "45", "3", "3", "99", "96", "1",
					"4", "99", "154", "102" },
			{ "23250344", "5141608", "97", "40", "1", "5", "98", "91", "2",
					"3", "88", "94", "152" },
			{ "2325048", "51415222", "30", "35", "1", "1", "89", "75", "5",
					"4", "93", "94", "152" },
			{ "23251101", "51414127", "30", "31", "1", "1", "89", "95", "2",
					"5", "89", "94", "152" },
			{ "23251625", "51413097", "30", "27", "1", "4", "94", "92", "2",
					"4", "89", "154", "102" },
			{ "23252634", "51411703", "97", "22", "1", "1", "97", "91", "3",
					"4", "79", "154", "102" },
			{ "23253391", "51410522", "20", "17", "1", "2", "89", "95", "3",
					"2", "88", "94", "152" },
			{ "23254186", "51409321", "30", "13", "1", "4", "91", "88", "5",
					"3", "87", "154", "102" },
			{ "23254652", "51408505", "46", "10", "1", "3", "99", "96", "1",
					"4", "99", "94", "152" },
			{ "23255331", "51408291", "97", "07", "1", "5", "98", "91", "2",
					"3", "88", "94", "152" },
			{ "23256399", "51409128", "46", "06", "1", "1", "89", "75", "5",
					"4", "93", "154", "102" },
			{ "23257447", "51409321", "30", "05", "1", "2", "89", "95", "3",
					"2", "88", "94", "152" },
			{ "23258203", "51409299", "46", "03", "1", "4", "91", "88", "5",
					"3", "87", "154", "102" },
			{ "23260571", "51408806", "20", "02", "1", "1", "97", "91", "3",
					"4", "79", "154", "102" } };
	static int SIM8_interval = 60000;

	static class SimData implements Serializable {
		String[][] dataArray;
		int interval;
	}
}
