package com.eiw.server.bo;

import javax.naming.Context;
import javax.naming.InitialContext;

import org.jboss.logging.Logger;

import com.eiw.admin.ejb.EMSAdminPortal;
import com.eiw.alerts.AlertsEJBRemote;
import com.eiw.cron.AlertConfigEJB;
import com.eiw.cron.AlertConfigEJBRemote;
import com.eiw.cron.DispatcherEJBRemote;
import com.eiw.cron.archival.ArchivalEJBRemote;
import com.eiw.cron.archival.ArchivalVehicleSummaryEJBRemote;
import com.eiw.cron.report.SummaryEJBRemote;
import com.eiw.cron.skywave.SkywaveInjectingEJBRemote;
//import com.eiw.device.ejb.CommonEMSFleetTracking;
import com.eiw.device.ejb.FleetTrackingDeviceListenerBORemote;
import com.skt.alerts.SKTAlertsEJBremote;

public class BOFactory {

	private static final Logger log = Logger.getLogger("admin");

	public static FleetTrackingDeviceListenerBORemote getFleetTrackingDeviceListenerBORemote() {
		try {
			Context context = new InitialContext();
			FleetTrackingDeviceListenerBORemote dispatcherEJBRemote = (FleetTrackingDeviceListenerBORemote) context
					.lookup("java:global/AdminEAR/AdminEJB/FleetTrackingDeviceListenerBO");
			return dispatcherEJBRemote;
		} catch (Throwable e) {
			log.error("FleetTrackingDeviceListenerBORemote ", e);
		}
		return null;
	}

	public static DispatcherEJBRemote getDispatcherEJBRemote() {
		try {
			Context context = new InitialContext();
			DispatcherEJBRemote dispatcherEJBRemote = (DispatcherEJBRemote) context
					.lookup("java:global/AdminEAR/AdminEJB/DispatcherEJB");
			return dispatcherEJBRemote;
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}

	public static ArchivalEJBRemote getArchivalEJBRemote() {
		try {
			Context context = new InitialContext();
			ArchivalEJBRemote archivalEJBRemote = (ArchivalEJBRemote) context
					.lookup("java:global/AdminEAR/AdminEJB/ArchivalEJB");
			return archivalEJBRemote;
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}

	public static SkywaveInjectingEJBRemote getSkywaveInjectionEJBRemote() {
		try {
			Context context = new InitialContext();
			SkywaveInjectingEJBRemote skywaveInjectionEJBRemote = (SkywaveInjectingEJBRemote) context
					.lookup("java:global/AdminEAR/AdminEJB/SkywaveInjectingEJB");
			return skywaveInjectionEJBRemote;
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}

	public static AlertsEJBRemote getAlertsEJBRemote() {
		try {
			Context context = new InitialContext();
			AlertsEJBRemote alertsEJBRemote = (AlertsEJBRemote) context
					.lookup("java:global/AdminEAR/AdminEJB/AlertsEJB");
			return alertsEJBRemote;
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}

	public static AlertConfigEJB getAlertsConfigEJB() {
		try {
			Context context = new InitialContext();
			AlertConfigEJB alertConfigEJB = (AlertConfigEJB) context
					.lookup("java:global/AdminEAR/AdminEJB/AlertConfigEJB");
			return alertConfigEJB;
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}

	public static SummaryEJBRemote getSummaryEJBRemote() {
		try {
			Context context = new InitialContext();
			SummaryEJBRemote summaryEJBRemote = (SummaryEJBRemote) context
					.lookup("java:global/AdminEAR/AdminEJB/SummaryEJB");
			return summaryEJBRemote;
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}

	public static ArchivalVehicleSummaryEJBRemote getArhivalVehicleSummaryEJBRemote() {
		try {
			Context context = new InitialContext();
			ArchivalVehicleSummaryEJBRemote archivalVehicleSummaryEJBRemote = (ArchivalVehicleSummaryEJBRemote) context
					.lookup("java:global/AdminEAR/AdminEJB/ArchivalVehicleSummaryEJB");
			return archivalVehicleSummaryEJBRemote;
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}

	public static SKTAlertsEJBremote getStudentalertEJBremote() {
		// TODO Auto-generated method stub
		try {
			Context context = new InitialContext();
			SKTAlertsEJBremote studentEJBremote = (SKTAlertsEJBremote) context
					.lookup("java:global/AdminEAR/AdminEJB/SKTAlertsEJB");
			return studentEJBremote;
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}

	public static RuptelaDeviceMgmtRemote getRuptelaDeviceMgmtEJBRemote() {
		try {
			Context context = new InitialContext();
			RuptelaDeviceMgmtRemote ruptelaDeviceMgmtRemote = (RuptelaDeviceMgmtRemote) context
					.lookup("java:global/AdminEAR/AdminEJB/RuptelaDeviceMgmt");
			return ruptelaDeviceMgmtRemote;
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}

	public static AlertConfigEJBRemote getAlertConfigEJBRemote() {
		try {
			Context context = new InitialContext();
			AlertConfigEJBRemote alertConfigEJBRemote = (AlertConfigEJBRemote) context
					.lookup("java:global/AdminEAR/AdminEJB/AlertConfigEJB");
			return alertConfigEJBRemote;
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}

	public static TeltonikaDeviceMgmtRemote getTeltonikaDeviceMgmtEJBRemote() {
		try {
			Context context = new InitialContext();
			TeltonikaDeviceMgmtRemote teltonikaDeviceMgmtRemote = (TeltonikaDeviceMgmtRemote) context
					.lookup("java:global/AdminEAR/AdminEJB/TeltonikaDeviceMgmt");
			return teltonikaDeviceMgmtRemote;
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}

	public static EMSAdminPortal getEmsAdminPortal() {
		try {
			Context context = new InitialContext();
			EMSAdminPortal emsAdminPortal = (EMSAdminPortal) context
					.lookup("java:global/AdminEAR/AdminEJB/EMSAdminPortalImpl");
			return emsAdminPortal;
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}

	/*public static CommonEMSFleetTracking getCommonEMSFleetTracking() {
		try {
			Context context = new InitialContext();
			CommonEMSFleetTracking commonemsfleet = (CommonEMSFleetTracking) context
					.lookup("java:global/WebEAR/WebEJB/CommonEMSFleetTracking");
			return commonemsfleet;
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;

	}*/
}