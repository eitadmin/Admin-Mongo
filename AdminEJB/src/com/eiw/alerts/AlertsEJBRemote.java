package com.eiw.alerts;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

import javax.ejb.Local;
import javax.websocket.CloseReason;
import javax.websocket.Session;

import com.eiw.client.dto.CompanyData;
import com.eiw.device.ejb.VehicleComposite;
import com.eiw.server.companyadminpu.Provider;
import com.eiw.server.companyadminpu.Pushnotificationdevices;
import com.eiw.server.companyadminpu.Smssent;
import com.eiw.server.fleettrackingpu.Alertconfig;
import com.eiw.server.fleettrackingpu.Calendarevent;
import com.eiw.server.fleettrackingpu.Freeformgeo;
import com.eiw.server.fleettrackingpu.Fueltanklid;
import com.eiw.server.fleettrackingpu.Heartbeatevent;
import com.eiw.server.fleettrackingpu.Hourmeter;
import com.eiw.server.fleettrackingpu.Landmarks;
import com.eiw.server.fleettrackingpu.Maintenancedue;
import com.eiw.server.fleettrackingpu.MaintenancedueId;
import com.eiw.server.fleettrackingpu.Operator;
import com.eiw.server.fleettrackingpu.Route;
import com.eiw.server.fleettrackingpu.Vehicle;
import com.eiw.server.fleettrackingpu.VehicleHasFreeform;
import com.eiw.server.fleettrackingpu.VehicleHasGeozones;
import com.eiw.server.fleettrackingpu.VehicleHasIo;
import com.eiw.server.fleettrackingpu.VehicleHasLandmark;
import com.eiw.server.fleettrackingpu.VehicleHasOdometer;
import com.eiw.server.fleettrackingpu.VehicleHasRoute;
import com.eiw.server.fleettrackingpu.Vehiclealerts;
import com.eiw.server.fleettrackingpu.Vehicleevent;
import com.eiw.server.studenttrackingpu.Alertevents;
import com.eiw.server.studenttrackingpu.Routetrip;
import com.eiw.server.studenttrackingpu.StudentGeozone;
import com.eiw.server.studenttrackingpu.Studentalertsubscription;
import com.eiw.server.studenttrackingpu.Studentdetails;
import com.eiw.server.studenttrackingpu.Studentevent;
import com.eiw.server.studenttrackingpu.Vehicletimings;

@Local
public interface AlertsEJBRemote {

	List<Alertconfig> getAlertConfig(String vin);

	List<VehicleHasGeozones> getGeoConfig(String vin, String mode);

	Vehicle getVehicle(String vin);

	boolean isAlertAlreadySent(String vin, String expectedTime,
			String interval, String alertType);

	boolean isSmsBalAvail(Vehiclealerts vehiclealerts);

	String getTimeZoneRegion(String region);

	boolean isSmsBalAvail(List<String> strs);

	boolean insertSMS(List<String> strs);

	boolean insertSMS1(Smssent smssent);

	boolean insertVehicleAlert(Vehiclealerts vehiclealerts, String mode);

	List<Vehicleevent> getIdleVehicleevents(Vehicleevent ve);

	Vehicleevent getFatigueVehicleevents(String vin);

	Vehicleevent getGeoPrevVe(String vin);

	boolean isGeoTrue(String latCen, String latRad, String latMar);

	Map<Integer, Boolean> getGeoStatusHashMap(String query);

	int getDay(Date eventTimeStamp, String timeZone);

	boolean getTime(List<Vehicleevent> vehicleEvents, String range);

	// Freeform
	List<VehicleHasFreeform> getFreeformGeoConfig(String vin, String mode);

	Freeformgeo getFreeformGeo(int id);

	boolean getFreeformGeoStatus(Vehicleevent vehicleEvent, int id);

	// Route

	List<VehicleHasRoute> getRoadGeoConfig(String vin);

	Route getRoadGeo(int id);

	boolean getRoadGeoStatus(Vehicleevent vehicleEvent, int id);

	// Route

	List<VehicleHasLandmark> getLandmarkGeoConfig(String vin);

	Landmarks getLandmarkGeo(String id);

	Map<String, Boolean> getLandmarkGeoStatusHashMap(String query);

	List<VehicleHasIo> getVehicleHasIos(String vin, String alertsubtype);

	SortedMap<Integer, Boolean> getDayAlert(int alertDay);

	float getanalogReading(String vin, int type, int millivolts);

	float getmaxanalogReading(String vin, int type, boolean engine);

	Vehicleevent getLastVEData(String vin, String eventtimestamp, int interval);

	boolean getValidity(List<Vehicleevent> vehicleevents, Date validityExp);

	String getOdometer(String vin);

	List<Alertconfig> getAlertConfigOdometer(String vin);

	int getVehicleHasIO(String vin);

	Fueltanklid getFueltanklid(String vin);

	boolean insertFuelTankLid(List<Fueltanklid> fueltanklids);

	Vehicleevent getPreviousVE(String vin, String eventDate);

	String getAddress(String lat, String lng);

	List<Studentalertsubscription> getAlertsSubscribed(String vin);

	Provider getProviderDetails(String companyId);

	boolean insertVehicleAlerts(Alertevents alertEvent);

	List<Calendarevent> getMaintanceAlert();

	void insertTagintoDB(String tagid);

	List<Provider> getProvidersDetails(String companyId);

	List<Vehicleevent> getPrevValues(String vin);

	List<Pushnotificationdevices> getAllActiveDeviceId(String companyId,
			String userId);

	boolean getFreeformGeoStatus(Studentevent studentevent, int id);

	List<Vehicleevent> getStopVehicleevents(Vehicleevent ve);

	Map<String, Boolean> strGetGeoStatusHashMap(String queryStr);

	String getServerKey(String appName, String deviceOs);

	// void clearAlertsEJBsession();

	CompanyData getCompanySettings(String companyId);

	List<VehicleHasOdometer> getOverAllOdometer(String vin, String dt);

	String getPreferencesData(String keyValue, String corpId);

	List<Alertconfig> getOverAllAlertConfig(String vin, String alerttype);

	Maintenancedue getMaintanceDue(MaintenancedueId maintenancedueId);

	void updateMaintenanceDue(Maintenancedue maintenance, String type);

	Hourmeter getHourMeter(String vin);

	Heartbeatevent getPrevHeartbeatEvent(String vin, Date timeStamp);

	boolean getAlertTime(Heartbeatevent heartbeatevent, String range);

	boolean getAlertValidity(Heartbeatevent heartbeatevent, Date validityExp);

	int getStoppedTime(String vin, String eventtimestamp);

	String getOperatorName(String vin);

	boolean isEmailValidOrNot(String mail);

	void insertTicket(Vehicleevent ve, String vin, String plateNo,
			VehicleComposite vehicleComposite);

	Operator getOperatorForVehicle(String vin);

	void registerTicketManagementSession(Session session, String data);

	void closeTicketManagementSession(Session session, CloseReason reason);

	String getApplicationSetting(String key);

	long getBetweenSec(String time1, String time2);

	String getRoadGeoSubStationStatus(Vehicleevent vehicleEvent, int id);

	String getRegionByCompany(String companyId);

	int getOverAllAlertCount(String companyId);
}
