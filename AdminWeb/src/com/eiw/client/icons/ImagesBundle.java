package com.eiw.client.icons;

import java.util.HashMap;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

public interface ImagesBundle extends ClientBundle {
	ImagesBundle INSTANCE = GWT.create(ImagesBundle.class);

	public static class Util {

		/* mapping of the CSS icon classes and the image resources */
		private static HashMap<String, ImageResource> ICONS = new HashMap<String, ImageResource>();

		/* here we build the mapping */
		static {
			// Header Panel
			add("ICON-dashBoard", INSTANCE.dashBoard());
			add("ICON-tracker", INSTANCE.tracker());
			add("ICON-fleetManagement", INSTANCE.fleetManagement());
			add("ICON-alertIcon", INSTANCE.alertIcon());
			add("ICON-userPreference", INSTANCE.userPreference());
			add("ICON-reportsIcon", INSTANCE.reportsIcon());
			add("ICON-associate", INSTANCE.associateIcon());
			add("ICON-vehicle1", INSTANCE.vehicle1());

			// Calendar
			add("ICON-del", INSTANCE.del());
			add("ICON-editEvent", INSTANCE.editEvent());
			add("ICON-back", INSTANCE.back());
			add("ICON-next", INSTANCE.next());
			add("ICON-calViewDay", INSTANCE.getCalViewDay());
			add("ICON-calViewMonth", INSTANCE.getCalViewMonth());
			add("ICON-calViewWeek", INSTANCE.getCalViewWeek());
			add("ICON-addTask", INSTANCE.addTask());
			add("ICON-legend_more", INSTANCE.legendMore());
			add("ICON-addEvent", INSTANCE.addEvent());
			add("ICON-apply", INSTANCE.apply());
			add("ICON-cancel", INSTANCE.cancel());
			add("ICON-Logout", INSTANCE.LogoutIcon());

			// Dashboard
			add("ICON-soundEnable", INSTANCE.soundEnable());
			add("ICON-soundDisable", INSTANCE.soundDisable());
			add("ICON-alertDisable", INSTANCE.alertDisable());
			add("ICON-fuelSummary", INSTANCE.fuelSummary());
			add("ICON-livetracking1", INSTANCE.livetracking1());
			add("ICON-vehicleTracking", INSTANCE.vehicleTracking());
			add("ICON-maint", INSTANCE.maint());
			add("ICON-alert", INSTANCE.alert());
			add("ICON-overSpdSummaryChart", INSTANCE.overSpdSummaryChart());
			add("ICON-add24", INSTANCE.add16Icon());
			add("ICON-delivery", INSTANCE.delivery());

			// Tracking
			add("ICON-live", INSTANCE.live());
			add("ICON-bktrk", INSTANCE.bktrk());
			add("ICON-device", INSTANCE.deviceIcon());
			add("ICON-operator", INSTANCE.operator());
			add("ICON-alertsystemIcon", INSTANCE.alertsystemIcon());
			add("ICON-landmarksIcon", INSTANCE.landmarksIcon());
			add("ICON-myvehicleIcon", INSTANCE.myvehicleIcon());
			add("ICON-geoAlertMap", INSTANCE.geoAlertMap());
			add("ICON-mapvehicleIcon", INSTANCE.mapvehicleIcon());
			add("ICON-vehiclemapIcon", INSTANCE.vehiclemapIcon());

			// Reports
			add("ICON-maintRpt", INSTANCE.maintRpt());
			add("ICON-overSpeedRpt", INSTANCE.overSpeedRpt());
			add("ICON-vehiAlertRpt", INSTANCE.vehiAlertRpt());
			add("ICON-vehiCompSummRpt", INSTANCE.vehiCompSummRpt());
			add("ICON-vehiMoveRpt", INSTANCE.vehiMoveRpt());
			add("ICON-vehiStatusRpt", INSTANCE.vehiStatusRpt());
			add("ICON-reportIcon", INSTANCE.reportIcon());
			add("ICON-I_16add", INSTANCE.I_16add());
			add("ICON-pdf", INSTANCE.pdf());
			add("ICON-csv", INSTANCE.csv());
			add("ICON-print", INSTANCE.print());

			// Vehicle Mgmt
			add("ICON-I_16edit", INSTANCE.I_16edit());
			add("ICON-I_16delete", INSTANCE.I_16delete());
			add("ICON-I_16disassociate", INSTANCE.I_16disassociate());
			add("ICON-I_16activate", INSTANCE.I_16activate());
			add("ICON-I_16deactivate1", INSTANCE.I_16deactivate1());
			add("ICON-I_16geofen1", INSTANCE.I_16geofen1());
			add("ICON-CIRCLERED", INSTANCE.circleRed());
			add("ICON-CIRCLEGREEN", INSTANCE.circleGreen());
			add("ICON-rectanglegreen1", INSTANCE.rectanglegreen());
			add("ICON-rectanglered1", INSTANCE.rectanglered());
			add("ICON-I_sim", INSTANCE.I_sim());
			add("ICON-I_16vehiDeviceAss1", INSTANCE.I_16vehiDeviceAss1());
			add("ICON-vehicle", INSTANCE.vehicleIcon());
			add("ICON-VehicleGeo", INSTANCE.vehicleGeo());
			add("ICON-remove", INSTANCE.removeIcon());
			add("ICON-save", INSTANCE.saveIcon());
			add("ICON-I_16simDeviceAss", INSTANCE.I_16simDeviceAss());
			add("ICON-vehicledevice", INSTANCE.vehicledeviceIcon());

			// Company Mgmt
			add("ICON-lastLogin", INSTANCE.lastLogin());
			add("ICON-I_24maint.png", INSTANCE.I_maint());

			// SuperAdmin
			add("ICON-feature", INSTANCE.feature());
			add("ICON-Newcompany", INSTANCE.company());
			add("ICON-count", INSTANCE.countsms());
			add("ICON-error", INSTANCE.error());
			add("ICON-bwusage", INSTANCE.bwusage());
			add("ICON-login", INSTANCE.login());
			add("ICON-vehiclereport", INSTANCE.vehiclereport());
			add("ICON-bulkreport", INSTANCE.bulkreport());

			// AlertConfiguration
			add("ICON-legend_enable", INSTANCE.legendEnable());
			add("ICON-legend_disable", INSTANCE.legendDisable());
			add("ICON-legend_delete", INSTANCE.legendDelete());
			add("ICON-legend_OverSpeed", INSTANCE.vehiOverSpeedLegend());
			add("ICON-legend_vehicletow", INSTANCE.vehiTowed2());
			add("ICON-legend_VehicleIdle1", INSTANCE.vehiIdle1());
			add("ICON-legend_FuelStatus2", INSTANCE.vehiFuelStatus());
			add("ICON-legend_geoprefered", INSTANCE.vehiPrefArea());
			add("ICON-legend_georestricted", INSTANCE.vehiRestArea());
			add("ICON-legend_Temperature2", INSTANCE.vehiTemp());
			add("ICON-legend_dooropen", INSTANCE.vehiDoorOpenLegend());
			add("ICON-legend_Vehicleidle", INSTANCE.vehiIdle());
			add("ICON-legend_VehicleTowed", INSTANCE.vehiTowed());
			add("ICON-fuelAlertLegend", INSTANCE.fuelAlertLegend());
			add("ICON-legend_edit", INSTANCE.legendEdit());
			add("ICON-legend_OverSpeedSmall",
					INSTANCE.vehiOverSpeedLegendSmall());
			add("ICON-legend_VehicleTowed_Small", INSTANCE.vehiTowedSmall());
			add("ICON-legend_vehiOperTimeSmall", INSTANCE.vehiOperTimeSmall());
			add("ICON-legend_vehiIdleSmall", INSTANCE.vehiIdleSmall());
			add("ICON-legend_vehiTempSmall", INSTANCE.vehiTempSmall());
			add("ICON-legend_vehiFuelStatusSmall",
					INSTANCE.vehiFuelStatusSmall());
			add("ICON-legend)vehiDoorOpenLegendSmall",
					INSTANCE.vehiDoorOpenLegendSmall());
			add("ICON-legend_vehiRestAreaSmall", INSTANCE.vehiRestAreaSmall());
			add("ICON-legend_geoprefered_small", INSTANCE.vehiPrefAreaSmall());

			// Back Tracking
			add("ICON-16Go", INSTANCE.Go1());
			add("ICON-pauseTr", INSTANCE.pauseTr());
			add("ICON-stopTr", INSTANCE.stopTr());
			add("ICON-playTr", INSTANCE.playTr());

			add("ICON-listener", INSTANCE.listener());

			add("ICON-refresh", INSTANCE.refresh());

			add("ICON-allcompany", INSTANCE.allCompany());

			add("ICON-sms", INSTANCE.sms());

		}

		/* conversion function */
		public static AbstractImagePrototype get(String imageCls) {
			ImageResource ir = ICONS.get(imageCls);
			if (ir != null)
				return AbstractImagePrototype.create(ir);
			return null;
		}

		/* adding helper function */
		protected static void add(String imageCls, ImageResource ir) {
			ICONS.put(imageCls, ir);
		}

	}

	@Source("DashRefresh.png")
	ImageResource refresh();

	@Source("home.png")
	ImageResource dashBoard();

	@Source("truck.png")
	ImageResource tracker();

	@Source("people.png")
	ImageResource fleetManagement();

	@Source("alert.png")
	ImageResource alertIcon();

	@Source("user-resource.png")
	ImageResource userPreference();

	@Source("Notes.png")
	ImageResource reportsIcon();

	@Source("live.png")
	ImageResource live();

	@Source("bktrk.png")
	ImageResource bktrk();

	@Source("vehicle1.png")
	ImageResource vehicle1();

	@Source("newdevice.png")
	ImageResource deviceIcon();

	@Source("16operator1.png")
	ImageResource operator();

	@Source("AlertSystem.png")
	ImageResource alertsystemIcon();

	@Source("LandMarks.png")
	ImageResource landmarksIcon();

	@Source("MyFleets.png")
	ImageResource myvehicleIcon();

	@Source("Geozone Alerts Report.png")
	ImageResource geoAlertMap();

	@Source("Geozone Vehicle Map.png")
	ImageResource mapvehicleIcon();

	@Source("Vehicle Geozone Map.png")
	ImageResource vehiclemapIcon();

	@Source("Maintenance.png")
	ImageResource maintRpt();

	@Source("Overspeed Summary.png")
	ImageResource overSpeedRpt();

	@Source("Vehicle Alerts Report.png")
	ImageResource vehiAlertRpt();

	@Source("Vehicle Complete Summary.png")
	ImageResource vehiCompSummRpt();

	@Source("Vehicle Movement Report.png")
	ImageResource vehiMoveRpt();

	@Source("Vehicle Status.png")
	ImageResource vehiStatusRpt();

	@Source("Vehicle Stop Report.png")
	ImageResource reportIcon();

	@Source("16edit.png")
	ImageResource I_16edit();

	@Source("16delete.png")
	ImageResource I_16delete();

	@Source("16disassociate.png")
	ImageResource I_16disassociate();

	@Source("16add.png")
	ImageResource I_16add();

	@Source("16activate.png")
	ImageResource I_16activate();

	@Source("16deactivate1.png")
	ImageResource I_16deactivate1();

	@Source("16geofen1.png")
	ImageResource I_16geofen1();

	@Source("CIRCLERED.png")
	ImageResource circleRed();

	@Source("newCIRCLEGREEN.png")
	ImageResource circleGreen();

	@Source("newrectanglegreen1.png")
	ImageResource rectanglegreen();

	@Source("newrectanglered1.png")
	ImageResource rectanglered();

	@Source("sim.png")
	ImageResource I_sim();

	@Source("associate.png")
	ImageResource associateIcon();

	@Source("16vehiDeviceAss1.png")
	ImageResource I_16vehiDeviceAss1();

	@Source("vehicle.png")
	ImageResource vehicleIcon();

	@Source("Vehicle geozone.png")
	ImageResource vehicleGeo();

	@Source("save.png")
	ImageResource saveIcon();

	@Source("remove.png")
	ImageResource removeIcon();

	@Source("assSimDev.png")
	ImageResource I_16simDeviceAss();

	@Source("deleteEvent.png")
	ImageResource del();

	@Source("editevent.png")
	ImageResource editEvent();

	@Source("calendar_view_day.png")
	ImageResource getCalViewDay();

	@Source("calendar_view_month.png")
	ImageResource getCalViewMonth();

	@Source("calendar_view_week.png")
	ImageResource getCalViewWeek();

	@Source("left-btn.gif")
	ImageResource back();

	@Source("right-btn.gif")
	ImageResource next();

	@Source("addTask.png")
	ImageResource addTask();

	@Source("legend_more.png")
	ImageResource legendMore();

	@Source("tag_blue_add.png")
	ImageResource addEvent();

	@Source("accept.png")
	ImageResource apply();

	@Source("cancel.png")
	ImageResource cancel();

	@Source("sound_high.png")
	ImageResource soundEnable();

	@Source("sound_mute.png")
	ImageResource soundDisable();

	@Source("alert_mute.png")
	ImageResource alertDisable();

	@Source("24fuel.png")
	ImageResource fuelSummary();

	@Source("livetracking1.png")
	ImageResource livetracking1();

	@Source("16search1.png")
	ImageResource vehicleTracking();

	@Source("24maint.png")
	ImageResource maint();

	@Source("16alert2.png")
	ImageResource alert();

	@Source("16eta1.png")
	ImageResource overSpdSummaryChart();

	@Source("Logout.png")
	ImageResource LogoutIcon();

	@Source("vehicledevice.png")
	ImageResource vehicledeviceIcon();

	@Source("pdf.png")
	ImageResource pdf();

	@Source("csv.png")
	ImageResource csv();

	@Source("24print.png")
	ImageResource print();

	@Source("add24.gif")
	ImageResource add16Icon();

	@Source("lastLogin.png")
	ImageResource lastLogin();

	@Source("24maint.png")
	ImageResource I_maint();

	@Source("legend_enable.png")
	ImageResource legendEnable();

	@Source("legend_disable.png")
	ImageResource legendDisable();

	@Source("legend_delete.png")
	ImageResource legendDelete();

	@Source("legend_OverSpeed.png")
	ImageResource vehiOverSpeedLegend();

	@Source("legend_vehicletow.png")
	ImageResource vehiTowed2();

	@Source("legend_idle.png")
	ImageResource vehiIdle1();

	@Source("legend_FuelStatus2.png")
	ImageResource vehiFuelStatus();

	@Source("legend_geoprefered.png")
	ImageResource vehiPrefArea();

	@Source("legend_georestricted.png")
	ImageResource vehiRestArea();

	@Source("legend_Temperature2.png")
	ImageResource vehiTemp();

	@Source("legend_dooropen.png")
	ImageResource vehiDoorOpenLegend();

	@Source("legend_Vehicleidle.png")
	ImageResource vehiIdle();

	@Source("legend_VehicleTowed.png")
	ImageResource vehiTowed();

	@Source("fuelalert.png")
	ImageResource fuelAlertLegend();

	@Source("legend_edit.png")
	ImageResource legendEdit();

	@Source("legend_OverSpeed_small.png")
	ImageResource vehiOverSpeedLegendSmall();

	@Source("legend_VehicleTowed_Small.png")
	ImageResource vehiTowedSmall();

	@Source("legend_idle_Small.png")
	ImageResource vehiOperTimeSmall();

	@Source("legend_Vehicleidle_Small.png")
	ImageResource vehiIdleSmall();

	@Source("legend_Temperature_Small.png")
	ImageResource vehiTempSmall();

	@Source("legend_FuelStatus_Small.png")
	ImageResource vehiFuelStatusSmall();

	@Source("legend_dooropen_Small.png")
	ImageResource vehiDoorOpenLegendSmall();

	@Source("legend_georestricted_Small.png")
	ImageResource vehiRestAreaSmall();

	@Source("legend_geoprefered_small.png")
	ImageResource vehiPrefAreaSmall();

	@Source("16Go.png")
	ImageResource Go1();

	@Source("stopTr.png")
	ImageResource stopTr();

	@Source("playTr.png")
	ImageResource playTr();

	@Source("pauseTr.png")
	ImageResource pauseTr();

	@Source("delivery.png")
	ImageResource delivery();

	@Source("Companyfeatured.png")
	ImageResource feature();

	@Source("Newcompany.png")
	ImageResource company();

	@Source("countsms.png")
	ImageResource countsms();

	@Source("errorlogs.png")
	ImageResource error();

	@Source("bwusage.png")
	ImageResource bwusage();

	@Source("loginaccess.png")
	ImageResource login();

	@Source("vehiclereport.png")
	ImageResource vehiclereport();

	@Source("bulkreport.png")
	ImageResource bulkreport();

	@Source("Listener.png")
	ImageResource listener();

	@Source("allcompany.png")
	ImageResource allCompany();

	@Source("sms.png")
	ImageResource sms();

}