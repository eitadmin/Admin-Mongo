package com.eiw.device.ejb;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.Local;
import javax.websocket.Session;

import org.json.JSONObject;

import com.eiw.device.android.Position;
import com.eiw.device.apmkt.ais1401A.APMKT_AIS1401AByteWrapper;
import com.eiw.server.fleettrackingpu.Ais140Emergency;
import com.eiw.server.fleettrackingpu.Ais140Health;
import com.eiw.server.fleettrackingpu.Fingerprintevent;
import com.eiw.server.fleettrackingpu.Heartbeatevent;
import com.eiw.server.fleettrackingpu.Odometercalc;
import com.eiw.server.fleettrackingpu.Vehicle;
import com.eiw.server.fleettrackingpu.Vehicleevent;
import com.eiw.server.studenttrackingpu.Studentalertsubscription;
import com.eiw.server.studenttrackingpu.Studentdetails;
import com.eiw.server.studenttrackingpu.Studentevent;
import com.skt.client.dto.StudentData;
import com.skt.client.dto.Vehicledetails;

@Local
public interface FleetTrackingDeviceListenerBORemote {

	Map<String, List<Session>> getTicketMap();

	VehicleComposite getVehicle(String imeiNo);

	int persistDeviceData(List<Vehicleevent> avlDataArray,
			VehicleComposite vehicleComposite);

	Vehicleevent persistDeviceData(Vehicleevent avlDataArray,
			VehicleComposite vehicleComposite);

	String getTimeZoneRegion(String region);

	String getDeviceModelName(String vin);

	String previousOdometer(String vin, String imeiNo);

	String persistOdometerCalc(Odometercalc odometercalc);

	void updateStudentData(Studentdetails studentdetails);

	Vehicleevent getPrevVe(String vin);

	Map<String, Vehicledetails> getvehicleattenderdetails(Vehicle vehicle);

	Map<String, StudentData> getStudentDetailsMap(String vin, String string,
			String string2);

	void test(Vehicleevent vehicleevent, Vehicle vehicle);

	Boolean checkDateAndTime(Vehicle vehicle);

	StudentData getStudentData(String singlePassiveTag);

	List<Studentalertsubscription> getAlertsSubscribed(String companyId,
			String branchId);

	Studentdetails getStudentDetails(String singlePassiveTag);

	boolean persistStudentEvent(Studentevent studentevent);

	void persistFingerPrintEvent(Fingerprintevent fingerPrintEvent);

	String persistIbutton(String iButtonValue, String vin, String companyId,
			String branchId);

	String getCompanySettings(String key, String companyId);

	void persistHourMeter(Vehicleevent vehicleevent, int runningduration);

	Vehicleevent getPrevVeConcox(String vin, Date eventTimeStamp);

	List<Vehicleevent> checkhourmeter(String vin, Date curdate);

	void updateLockStatus(Vehicle vehicle, int i);

	String getPreferencesData(String keyValue, String corpId);

	void updateVehicleevent(List<Vehicleevent> Vehicleeventlist, String status,
			VehicleComposite vehicleComposite);

	void persistHeartBeatEvent(Heartbeatevent heartbeatevent,
			VehicleComposite vehicleComposite);

	void updatevehicle(Vehicle v);

	void registerSession(String message, Session session);

	void liveTrackingDataPusher(Vehicleevent vehicleevent,
			VehicleComposite vehicleComposite);

	Heartbeatevent getPrevHeartbeatEvent(String vin, Date timeStamp);

	Vehicleevent getLatestVe(String nearByVins);

	void persistHealthEvent(Ais140Health ais140Health,
			VehicleComposite vehicleComposite);

	void persistEmergencyEvent(Ais140Emergency ais140Emergency,
			VehicleComposite vehicleComposite);

	void updateSupervisor(String vin, Position position);

	void updateWorkshop(String vin, Position position);

	boolean sendMessageforClient(String vin, JSONObject obj);

	String getrifdStatus(String vin);

	void persistHistoryEvent(Vehicleevent vehicleEvent);

}
