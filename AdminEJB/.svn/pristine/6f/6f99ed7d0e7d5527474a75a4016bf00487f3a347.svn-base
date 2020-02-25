package com.eiw.device.solar;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Date;

public class ModbusData {

	private Date dateTime;
	private String gsmSignal;
	private String statusBit;
	private int requestId;
	private String oilPressure;
	private String coolandTemperature;
	private String fuelLeval;
	private String chargeAlternatorVoltage;
	private String batteryVoltage;
	private String engineSpeed;
	private String generatorFrequency;
	private String genL1_N_V;
	private String genL2_N_V;
	private String genL3_N_V;
	private String genL1_L2_V;
	private String genL2_L3_V;
	private String genL3_L1_V;
	private String genL1C;
	private String genL2C;
	private String genL3C;
	private String genEarthC;
	private String genL1W;
	private String genL2W;
	private String genL3W;
	private String mainsFrequency;
	private String genL1VA;
	private String genL2VA;
	private String genL3VA;
	private String gentotVA;
	private String genL1VAr;
	private String genL2VAr;
	private String genL3VAr;
	private String gentotVAr;
	private String genPFL1;
	private String genPFL2;
	private String genPFL3;
	private String genPFAvg;
	private String numberOfNamedAlarm;
	private String genC_lag;
	private String lattitude;
	private String longitude;
	private String mainsL1_N_V;
	private String mainsL2_N_V;
	private String mainsL3_N_V;
	private String mainsL1L2_N_V;
	private String mainsL2L3_N_V;
	private String mainsL3L1_N_V;
	private String mainsV_lag;
	private String gen_P_R;
	private String mains_P_R;
	private String mainsC_lag;
	private String mainsL1C;
	private String mainsL2C;
	private String mainsL3C;
	private String mainsEarthC;
	private String mainsL1W;
	private String mainsL2W;
	private String mainsL3W;
	private String busC_lag;
	private String busFrequency;
	private String busL1_N_V;
	private String busL2_N_V;
	private String busL3_N_V;
	private String busL1L2_N_V;
	private String busL2L3_N_V;
	private String busL3L1_N_V;
	private String busL1C;
	private String busL2C;
	private String busL3C;
	private String busEarthC;
	private String busL1W;
	private String busL2W;
	private String busL3W;
	private String bus_P_R;
	private String gen_tot_W;
	private String genFullPower;
	private String genFullVAr;
	private String mains_tot_W;
	private String mainsL1VA;
	private String mainsL2VA;
	private String mainsL3VA;
	private String mainstotVA;
	private String mainsL1VAr;
	private String mainsL2VAr;
	private String mainsL3VAr;
	private String mainstotVAr;
	private String mainsPFL1;
	private String mainsPFL2;
	private String mainsPFL3;
	private String mainsPFAvg;
	private String genL1lag;
	private String genL2lag;
	private String genL3lag;
	private String gentotlag;
	private String genL1FullPower;
	private String genL2FullPower;
	private String genL3FullPower;
	private String engineRunTime;
	private String timeOfNextEngineMainatenance;
	private String timeToNextEngineMainatenance;
	private String currentTime;
	private String genPositiveKWH;
	private String genNegativeKWH;
	private String genKVAH;
	private String genKVArH;
	private String numberOfStart;
	private Object mainsPositiveKWH;
	private String mainsNegativeKWH;
	private String mainsKVAH;
	private String mainsKVArH;
	private String busPositiveKWH;
	private String busNegativeKWH;
	private String busKVAH;
	private String busKVArH;
	private String fuelUsed;
	private String maxPositiveMains_ROCOF;
	private String maxNegativeMains_ROCOF;
	private String maxPositiveMains_Vector;
	private String maxNegativeMains_Vector;
	private String timeToNextEngineMainatenanceAlerm1;
	private String timeOfNextEngineMainatenanceAlerm1;
	private String timeToNextEngineMainatenanceAlerm2;
	private String timeOfNextEngineMainatenanceAlerm2;
	private String timeToNextEngineMainatenanceAlerm3;
	private String alarm;
	private String oilTemperature;
	private int speed;
	private String rV;
	private String yV;
	private String bV;
	private String rC;
	private String yC;
	private String bC;
	private String rP;
	private String yP;
	private String bP;
	private String pvV;
	private String pvC;
	private String sptot;
	private String setoday;
	private String setot;
	private String batBV;
	private String batCC;
	private String batDC;
	private String batBT;
	private String batBP;
	private String batCE;
	private String batDE;
	private String batSOC;
	private String mpptBC;
	private String mpptBP;
	private String acLCR;
	private String acLCY;
	private String acLCB;
	private String acLF;
	private String acLPF;
	private String acLPMaxDay;
	private String acLVR;
	private String acLVY;
	private String acLVB;
	private String acLP;
	private String acLEtoday;
	private String acLETP;
	private String acLETNP;
	private String energyMeterCount;
	private String typeOfInverter;
	private String lvdLevel;
	private String dgStartCommandEnable;
	private String dgRuntimeSettings;
	private String dgStartBV;
	private String bulkV;
	private String floatV;
	private String acChargerStartV;
	private String acChargerStopV;
	private String gridChargerBatCLimit;
	private String maxBatCL;
	private String maxExportPower;
	private String windSpeed;
	private String windDirection;
	private String irradiance;
	private String pvCellTemp;
	private String ambientTemperature1;
	private String ambientTemperature2;
	private String dI1;
	private String dI2;
	private String dI3;
	private String dI4;
	private String dI5;
	private String dI6;
	private String dI7;
	private String dI8;
	private String dO1;
	private String dO2;
	private String dO3;
	private String dO4;
	private String dO5;
	private String dO6;
	private String dO7;
	private String dO8;
	private String analog5;
	private String solarV_S1;
	private String solarC_S1;
	private String solarE_S1;
	private String totSolarE1_S1;
	private String totSolarE2_S1;
	private String solarPower_S1;
	private String solarV_S2;
	private String solarC_S2;
	private String solarE_S2;
	private String totSolarE1_S2;
	private String totSolarE2_S2;
	private String solarPower_S2;
	private String solarV_S3;
	private String solarC_S3;
	private String solarE_S3;
	private String totSolarE1_S3;
	private String totSolarE2_S3;
	private String solarPower_S3;

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public String getOilTemperature() {
		return oilTemperature;
	}

	public void setOilTemperature(String oilTemperature) {
		this.oilTemperature = oilTemperature;
	}

	public String getLattitude() {
		return lattitude;
	}

	public void setLattitude(String lattitude) {
		this.lattitude = lattitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getMainsL1_N_V() {
		return mainsL1_N_V;
	}

	public void setMainsL1_N_V(String mainsL1_N_V) {
		this.mainsL1_N_V = mainsL1_N_V;
	}

	public String getMainsL2_N_V() {
		return mainsL2_N_V;
	}

	public void setMainsL2_N_V(String mainsL2_N_V) {
		this.mainsL2_N_V = mainsL2_N_V;
	}

	public String getMainsL3_N_V() {
		return mainsL3_N_V;
	}

	public void setMainsL3_N_V(String mainsL3_N_V) {
		this.mainsL3_N_V = mainsL3_N_V;
	}

	public String getMainsL1L2_N_V() {
		return mainsL1L2_N_V;
	}

	public void setMainsL1L2_N_V(String mainsL1L2_N_V) {
		this.mainsL1L2_N_V = mainsL1L2_N_V;
	}

	public String getMainsL2L3_N_V() {
		return mainsL2L3_N_V;
	}

	public void setMainsL2L3_N_V(String mainsL2L3_N_V) {
		this.mainsL2L3_N_V = mainsL2L3_N_V;
	}

	public String getMainsL3L1_N_V() {
		return mainsL3L1_N_V;
	}

	public void setMainsL3L1_N_V(String mainsL3L1_N_V) {
		this.mainsL3L1_N_V = mainsL3L1_N_V;
	}

	public String getMainsV_lag() {
		return mainsV_lag;
	}

	public void setMainsV_lag(String mainsV_lag) {
		this.mainsV_lag = mainsV_lag;
	}

	public String getGen_P_R() {
		return gen_P_R;
	}

	public void setGen_P_R(String gen_P_R) {
		this.gen_P_R = gen_P_R;
	}

	public String getMains_P_R() {
		return mains_P_R;
	}

	public void setMains_P_R(String mains_P_R) {
		this.mains_P_R = mains_P_R;
	}

	public String getMainsC_lag() {
		return mainsC_lag;
	}

	public void setMainsC_lag(String mainsC_lag) {
		this.mainsC_lag = mainsC_lag;
	}

	public String getMainsL1C() {
		return mainsL1C;
	}

	public void setMainsL1C(String mainsL1C) {
		this.mainsL1C = mainsL1C;
	}

	public String getMainsL2C() {
		return mainsL2C;
	}

	public void setMainsL2C(String mainsL2C) {
		this.mainsL2C = mainsL2C;
	}

	public String getMainsL3C() {
		return mainsL3C;
	}

	public void setMainsL3C(String mainsL3C) {
		this.mainsL3C = mainsL3C;
	}

	public String getMainsEarthC() {
		return mainsEarthC;
	}

	public void setMainsEarthC(String mainsEarthC) {
		this.mainsEarthC = mainsEarthC;
	}

	public String getMainsL1W() {
		return mainsL1W;
	}

	public void setMainsL1W(String mainsL1W) {
		this.mainsL1W = mainsL1W;
	}

	public String getMainsL2W() {
		return mainsL2W;
	}

	public void setMainsL2W(String mainsL2W) {
		this.mainsL2W = mainsL2W;
	}

	public String getMainsL3W() {
		return mainsL3W;
	}

	public void setMainsL3W(String mainsL3W) {
		this.mainsL3W = mainsL3W;
	}

	public String getBusC_lag() {
		return busC_lag;
	}

	public void setBusC_lag(String busC_lag) {
		this.busC_lag = busC_lag;
	}

	public String getBusFrequency() {
		return busFrequency;
	}

	public void setBusFrequency(String busFrequency) {
		this.busFrequency = busFrequency;
	}

	public String getBusL1_N_V() {
		return busL1_N_V;
	}

	public void setBusL1_N_V(String busL1_N_V) {
		this.busL1_N_V = busL1_N_V;
	}

	public String getBusL2_N_V() {
		return busL2_N_V;
	}

	public void setBusL2_N_V(String busL2_N_V) {
		this.busL2_N_V = busL2_N_V;
	}

	public String getBusL3_N_V() {
		return busL3_N_V;
	}

	public void setBusL3_N_V(String busL3_N_V) {
		this.busL3_N_V = busL3_N_V;
	}

	public String getBusL1L2_N_V() {
		return busL1L2_N_V;
	}

	public void setBusL1L2_N_V(String busL1L2_N_V) {
		this.busL1L2_N_V = busL1L2_N_V;
	}

	public String getBusL2L3_N_V() {
		return busL2L3_N_V;
	}

	public void setBusL2L3_N_V(String busL2L3_N_V) {
		this.busL2L3_N_V = busL2L3_N_V;
	}

	public String getBusL3L1_N_V() {
		return busL3L1_N_V;
	}

	public void setBusL3L1_N_V(String busL3L1_N_V) {
		this.busL3L1_N_V = busL3L1_N_V;
	}

	public String getBusL1C() {
		return busL1C;
	}

	public void setBusL1C(String busL1C) {
		this.busL1C = busL1C;
	}

	public String getBusL2C() {
		return busL2C;
	}

	public void setBusL2C(String busL2C) {
		this.busL2C = busL2C;
	}

	public String getBusL3C() {
		return busL3C;
	}

	public void setBusL3C(String busL3C) {
		this.busL3C = busL3C;
	}

	public String getBusEarthC() {
		return busEarthC;
	}

	public void setBusEarthC(String busEarthC) {
		this.busEarthC = busEarthC;
	}

	public String getBusL1W() {
		return busL1W;
	}

	public void setBusL1W(String busL1W) {
		this.busL1W = busL1W;
	}

	public String getBusL2W() {
		return busL2W;
	}

	public void setBusL2W(String busL2W) {
		this.busL2W = busL2W;
	}

	public String getBusL3W() {
		return busL3W;
	}

	public void setBusL3W(String busL3W) {
		this.busL3W = busL3W;
	}

	public String getBus_P_R() {
		return bus_P_R;
	}

	public void setBus_P_R(String bus_P_R) {
		this.bus_P_R = bus_P_R;
	}

	public String getGen_tot_W() {
		return gen_tot_W;
	}

	public void setGen_tot_W(String gen_tot_W) {
		this.gen_tot_W = gen_tot_W;
	}

	public String getGenFullPower() {
		return genFullPower;
	}

	public void setGenFullPower(String genFullPower) {
		this.genFullPower = genFullPower;
	}

	public String getGenFullVAr() {
		return genFullVAr;
	}

	public void setGenFullVAr(String genFullVAr) {
		this.genFullVAr = genFullVAr;
	}

	public String getMains_tot_W() {
		return mains_tot_W;
	}

	public void setMains_tot_W(String mains_tot_W) {
		this.mains_tot_W = mains_tot_W;
	}

	public String getMainsL1VA() {
		return mainsL1VA;
	}

	public void setMainsL1VA(String mainsL1VA) {
		this.mainsL1VA = mainsL1VA;
	}

	public String getMainsL2VA() {
		return mainsL2VA;
	}

	public void setMainsL2VA(String mainsL2VA) {
		this.mainsL2VA = mainsL2VA;
	}

	public String getMainsL3VA() {
		return mainsL3VA;
	}

	public void setMainsL3VA(String mainsL3VA) {
		this.mainsL3VA = mainsL3VA;
	}

	public String getMainstotVA() {
		return mainstotVA;
	}

	public void setMainstotVA(String mainstotVA) {
		this.mainstotVA = mainstotVA;
	}

	public String getMainsL1VAr() {
		return mainsL1VAr;
	}

	public void setMainsL1VAr(String mainsL1VAr) {
		this.mainsL1VAr = mainsL1VAr;
	}

	public String getMainsL2VAr() {
		return mainsL2VAr;
	}

	public void setMainsL2VAr(String mainsL2VAr) {
		this.mainsL2VAr = mainsL2VAr;
	}

	public String getMainsL3VAr() {
		return mainsL3VAr;
	}

	public void setMainsL3VAr(String mainsL3VAr) {
		this.mainsL3VAr = mainsL3VAr;
	}

	public String getMainstotVAr() {
		return mainstotVAr;
	}

	public void setMainstotVAr(String mainstotVAr) {
		this.mainstotVAr = mainstotVAr;
	}

	public String getMainsPFL1() {
		return mainsPFL1;
	}

	public void setMainsPFL1(String mainsPFL1) {
		this.mainsPFL1 = mainsPFL1;
	}

	public String getMainsPFL2() {
		return mainsPFL2;
	}

	public void setMainsPFL2(String mainsPFL2) {
		this.mainsPFL2 = mainsPFL2;
	}

	public String getMainsPFL3() {
		return mainsPFL3;
	}

	public void setMainsPFL3(String mainsPFL3) {
		this.mainsPFL3 = mainsPFL3;
	}

	public String getMainsPFAvg() {
		return mainsPFAvg;
	}

	public void setMainsPFAvg(String mainsPFAvg) {
		this.mainsPFAvg = mainsPFAvg;
	}

	public String getGenL1lag() {
		return genL1lag;
	}

	public void setGenL1lag(String genL1lag) {
		this.genL1lag = genL1lag;
	}

	public String getGenL2lag() {
		return genL2lag;
	}

	public void setGenL2lag(String genL2lag) {
		this.genL2lag = genL2lag;
	}

	public String getGenL3lag() {
		return genL3lag;
	}

	public void setGenL3lag(String genL3lag) {
		this.genL3lag = genL3lag;
	}

	public String getGentotlag() {
		return gentotlag;
	}

	public void setGentotlag(String gentotlag) {
		this.gentotlag = gentotlag;
	}

	public String getGenL1FullPower() {
		return genL1FullPower;
	}

	public void setGenL1FullPower(String genL1FullPower) {
		this.genL1FullPower = genL1FullPower;
	}

	public String getGenL2FullPower() {
		return genL2FullPower;
	}

	public void setGenL2FullPower(String genL2FullPower) {
		this.genL2FullPower = genL2FullPower;
	}

	public String getGenL3FullPower() {
		return genL3FullPower;
	}

	public void setGenL3FullPower(String genL3FullPower) {
		this.genL3FullPower = genL3FullPower;
	}

	public String getEngineRunTime() {
		return engineRunTime;
	}

	public void setEngineRunTime(String engineRunTime) {
		this.engineRunTime = engineRunTime;
	}

	public String getTimeOfNextEngineMainatenance() {
		return timeOfNextEngineMainatenance;
	}

	public void setTimeOfNextEngineMainatenance(
			String timeOfNextEngineMainatenance) {
		this.timeOfNextEngineMainatenance = timeOfNextEngineMainatenance;
	}

	public String getTimeToNextEngineMainatenance() {
		return timeToNextEngineMainatenance;
	}

	public void setTimeToNextEngineMainatenance(
			String timeToNextEngineMainatenance) {
		this.timeToNextEngineMainatenance = timeToNextEngineMainatenance;
	}

	public String getCurrentTime() {
		return currentTime;
	}

	public void setCurrentTime(String currentTime) {
		this.currentTime = currentTime;
	}

	public String getGenPositiveKWH() {
		return genPositiveKWH;
	}

	public void setGenPositiveKWH(String genPositiveKWH) {
		this.genPositiveKWH = genPositiveKWH;
	}

	public String getGenNegativeKWH() {
		return genNegativeKWH;
	}

	public void setGenNegativeKWH(String genNegativeKWH) {
		this.genNegativeKWH = genNegativeKWH;
	}

	public String getGenKVAH() {
		return genKVAH;
	}

	public void setGenKVAH(String genKVAH) {
		this.genKVAH = genKVAH;
	}

	public String getGenKVArH() {
		return genKVArH;
	}

	public void setGenKVArH(String genKVArH) {
		this.genKVArH = genKVArH;
	}

	public String getNumberOfStart() {
		return numberOfStart;
	}

	public void setNumberOfStart(String numberOfStart) {
		this.numberOfStart = numberOfStart;
	}

	public Object getMainsPositiveKWH() {
		return mainsPositiveKWH;
	}

	public void setMainsPositiveKWH(Object mainsPositiveKWH) {
		this.mainsPositiveKWH = mainsPositiveKWH;
	}

	public String getMainsNegativeKWH() {
		return mainsNegativeKWH;
	}

	public void setMainsNegativeKWH(String mainsNegativeKWH) {
		this.mainsNegativeKWH = mainsNegativeKWH;
	}

	public String getMainsKVAH() {
		return mainsKVAH;
	}

	public void setMainsKVAH(String mainsKVAH) {
		this.mainsKVAH = mainsKVAH;
	}

	public String getMainsKVArH() {
		return mainsKVArH;
	}

	public void setMainsKVArH(String mainsKVArH) {
		this.mainsKVArH = mainsKVArH;
	}

	public String getBusPositiveKWH() {
		return busPositiveKWH;
	}

	public void setBusPositiveKWH(String busPositiveKWH) {
		this.busPositiveKWH = busPositiveKWH;
	}

	public String getBusNegativeKWH() {
		return busNegativeKWH;
	}

	public void setBusNegativeKWH(String busNegativeKWH) {
		this.busNegativeKWH = busNegativeKWH;
	}

	public String getBusKVAH() {
		return busKVAH;
	}

	public void setBusKVAH(String busKVAH) {
		this.busKVAH = busKVAH;
	}

	public String getBusKVArH() {
		return busKVArH;
	}

	public void setBusKVArH(String busKVArH) {
		this.busKVArH = busKVArH;
	}

	public String getFuelUsed() {
		return fuelUsed;
	}

	public void setFuelUsed(String fuelUsed) {
		this.fuelUsed = fuelUsed;
	}

	public String getMaxPositiveMains_ROCOF() {
		return maxPositiveMains_ROCOF;
	}

	public void setMaxPositiveMains_ROCOF(String maxPositiveMains_ROCOF) {
		this.maxPositiveMains_ROCOF = maxPositiveMains_ROCOF;
	}

	public String getMaxNegativeMains_ROCOF() {
		return maxNegativeMains_ROCOF;
	}

	public void setMaxNegativeMains_ROCOF(String maxNegativeMains_ROCOF) {
		this.maxNegativeMains_ROCOF = maxNegativeMains_ROCOF;
	}

	public String getMaxPositiveMains_Vector() {
		return maxPositiveMains_Vector;
	}

	public void setMaxPositiveMains_Vector(String maxPositiveMains_Vector) {
		this.maxPositiveMains_Vector = maxPositiveMains_Vector;
	}

	public String getMaxNegativeMains_Vector() {
		return maxNegativeMains_Vector;
	}

	public void setMaxNegativeMains_Vector(String maxNegativeMains_Vector) {
		this.maxNegativeMains_Vector = maxNegativeMains_Vector;
	}

	public String getTimeToNextEngineMainatenanceAlerm1() {
		return timeToNextEngineMainatenanceAlerm1;
	}

	public void setTimeToNextEngineMainatenanceAlerm1(
			String timeToNextEngineMainatenanceAlerm1) {
		this.timeToNextEngineMainatenanceAlerm1 = timeToNextEngineMainatenanceAlerm1;
	}

	public String getTimeOfNextEngineMainatenanceAlerm1() {
		return timeOfNextEngineMainatenanceAlerm1;
	}

	public void setTimeOfNextEngineMainatenanceAlerm1(
			String timeOfNextEngineMainatenanceAlerm1) {
		this.timeOfNextEngineMainatenanceAlerm1 = timeOfNextEngineMainatenanceAlerm1;
	}

	public String getTimeToNextEngineMainatenanceAlerm2() {
		return timeToNextEngineMainatenanceAlerm2;
	}

	public void setTimeToNextEngineMainatenanceAlerm2(
			String timeToNextEngineMainatenanceAlerm2) {
		this.timeToNextEngineMainatenanceAlerm2 = timeToNextEngineMainatenanceAlerm2;
	}

	public String getTimeOfNextEngineMainatenanceAlerm2() {
		return timeOfNextEngineMainatenanceAlerm2;
	}

	public void setTimeOfNextEngineMainatenanceAlerm2(
			String timeOfNextEngineMainatenanceAlerm2) {
		this.timeOfNextEngineMainatenanceAlerm2 = timeOfNextEngineMainatenanceAlerm2;
	}

	public String getTimeToNextEngineMainatenanceAlerm3() {
		return timeToNextEngineMainatenanceAlerm3;
	}

	public void setTimeToNextEngineMainatenanceAlerm3(
			String timeToNextEngineMainatenanceAlerm3) {
		this.timeToNextEngineMainatenanceAlerm3 = timeToNextEngineMainatenanceAlerm3;
	}

	public String getAlarm() {
		return alarm;
	}

	public String getGenL1_N_V() {
		return genL1_N_V;
	}

	public void setGenL1_N_V(String genL1_N_V) {
		this.genL1_N_V = genL1_N_V;
	}

	public String getGenL2_N_V() {
		return genL2_N_V;
	}

	public void setGenL2_N_V(String genL2_N_V) {
		this.genL2_N_V = genL2_N_V;
	}

	public String getGenL3_N_V() {
		return genL3_N_V;
	}

	public void setGenL3_N_V(String genL3_N_V) {
		this.genL3_N_V = genL3_N_V;
	}

	public String getGenL1_L2_V() {
		return genL1_L2_V;
	}

	public void setGenL1_L2_V(String genL1_L2_V) {
		this.genL1_L2_V = genL1_L2_V;
	}

	public String getGenL2_L3_V() {
		return genL2_L3_V;
	}

	public void setGenL2_L3_V(String genL2_L3_V) {
		this.genL2_L3_V = genL2_L3_V;
	}

	public String getGenL3_L1_V() {
		return genL3_L1_V;
	}

	public void setGenL3_L1_V(String genL3_L1_V) {
		this.genL3_L1_V = genL3_L1_V;
	}

	public String getGenL1C() {
		return genL1C;
	}

	public void setGenL1C(String genL1C) {
		this.genL1C = genL1C;
	}

	public String getGenL2C() {
		return genL2C;
	}

	public void setGenL2C(String genL2C) {
		this.genL2C = genL2C;
	}

	public String getGenL3C() {
		return genL3C;
	}

	public void setGenL3C(String genL3C) {
		this.genL3C = genL3C;
	}

	public String getGenEarthC() {
		return genEarthC;
	}

	public void setGenEarthC(String genEarthC) {
		this.genEarthC = genEarthC;
	}

	public String getGenL1W() {
		return genL1W;
	}

	public void setGenL1W(String genL1W) {
		this.genL1W = genL1W;
	}

	public String getGenL2W() {
		return genL2W;
	}

	public void setGenL2W(String genL2W) {
		this.genL2W = genL2W;
	}

	public String getGenL3W() {
		return genL3W;
	}

	public void setGenL3W(String genL3W) {
		this.genL3W = genL3W;
	}

	public String getGenL1VA() {
		return genL1VA;
	}

	public void setGenL1VA(String genL1VA) {
		this.genL1VA = genL1VA;
	}

	public String getGenL2VA() {
		return genL2VA;
	}

	public void setGenL2VA(String genL2VA) {
		this.genL2VA = genL2VA;
	}

	public String getGenL3VA() {
		return genL3VA;
	}

	public void setGenL3VA(String genL3VA) {
		this.genL3VA = genL3VA;
	}

	public String getGentotVA() {
		return gentotVA;
	}

	public void setGentotVA(String gentotVA) {
		this.gentotVA = gentotVA;
	}

	public String getGenL1VAr() {
		return genL1VAr;
	}

	public void setGenL1VAr(String genL1VAr) {
		this.genL1VAr = genL1VAr;
	}

	public String getGenL2VAr() {
		return genL2VAr;
	}

	public void setGenL2VAr(String genL2VAr) {
		this.genL2VAr = genL2VAr;
	}

	public String getGenL3VAr() {
		return genL3VAr;
	}

	public void setGenL3VAr(String genL3VAr) {
		this.genL3VAr = genL3VAr;
	}

	public String getGentotVAr() {
		return gentotVAr;
	}

	public void setGentotVAr(String gentotVAr) {
		this.gentotVAr = gentotVAr;
	}

	public String getGenC_lag() {
		return genC_lag;
	}

	public void setGenC_lag(String genC_lag) {
		this.genC_lag = genC_lag;
	}

	public Date getDateTime() {
		return dateTime;
	}

	public String getStatusBit() {
		return statusBit;
	}

	public void setStatusBit(String statusBit) {
		this.statusBit = statusBit;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public String getGsmSignal() {
		return gsmSignal;
	}

	public void setGsmSignal(String gsmSignal) {
		this.gsmSignal = gsmSignal;
	}

	public String getstatusBit() {
		return statusBit;
	}

	public void setstatusBit(String statusBit) {
		this.statusBit = statusBit;
	}

	public int getRequestId() {
		return requestId;
	}

	public void setRequestId(int requestId) {
		this.requestId = requestId;
	}

	public String getOilPressure() {
		return oilPressure;
	}

	public void setOilPressure(String oilPressure) {
		this.oilPressure = oilPressure;
	}

	public String getCoolandTemperature() {
		return coolandTemperature;
	}

	public void setCoolandTemperature(String coolandTemperature) {
		this.coolandTemperature = coolandTemperature;
	}

	public String getFuelLeval() {
		return fuelLeval;
	}

	public void setFuelLeval(String fuelLeval) {
		this.fuelLeval = fuelLeval;
	}

	public String getChargeAlternatorVoltage() {
		return chargeAlternatorVoltage;
	}

	public void setChargeAlternatorVoltage(String chargeAlternatorVoltage) {
		this.chargeAlternatorVoltage = chargeAlternatorVoltage;
	}

	public String getBatteryVoltage() {
		return batteryVoltage;
	}

	public void setBatteryVoltage(String batteryVoltage) {
		this.batteryVoltage = batteryVoltage;
	}

	public String getEngineSpeed() {
		return engineSpeed;
	}

	public void setEngineSpeed(String engineSpeed) {
		this.engineSpeed = engineSpeed;
	}

	public String getGeneratorFrequency() {
		return generatorFrequency;
	}

	public void setGeneratorFrequency(String generatorFrequency) {
		this.generatorFrequency = generatorFrequency;
	}

	public String getMainsFrequency() {
		return mainsFrequency;
	}

	public void setMainsFrequency(String mainsFrequency) {
		this.mainsFrequency = mainsFrequency;
	}

	public String getGenPFL1() {
		return genPFL1;
	}

	public void setGenPFL1(String genPFL1) {
		this.genPFL1 = genPFL1;
	}

	public String getGenPFL2() {
		return genPFL2;
	}

	public void setGenPFL2(String genPFL2) {
		this.genPFL2 = genPFL2;
	}

	public String getGenPFL3() {
		return genPFL3;
	}

	public void setGenPFL3(String genPFL3) {
		this.genPFL3 = genPFL3;
	}

	public String getGenPFAvg() {
		return genPFAvg;
	}

	public void setGenPFAvg(String genPFAvg) {
		this.genPFAvg = genPFAvg;
	}

	public String getNumberOfNamedAlarm() {
		return numberOfNamedAlarm;
	}

	public void setNumberOfNamedAlarm(String numberOfNamedAlarm) {
		this.numberOfNamedAlarm = numberOfNamedAlarm;
	}

	public String getrV() {
		return rV;
	}

	public void setrV(String rV) {
		this.rV = rV;
	}

	public String getyV() {
		return yV;
	}

	public void setyV(String yV) {
		this.yV = yV;
	}

	public String getbV() {
		return bV;
	}

	public void setbV(String bV) {
		this.bV = bV;
	}

	public String getrC() {
		return rC;
	}

	public void setrC(String rC) {
		this.rC = rC;
	}

	public String getyC() {
		return yC;
	}

	public void setyC(String yC) {
		this.yC = yC;
	}

	public String getbC() {
		return bC;
	}

	public void setbC(String bC) {
		this.bC = bC;
	}

	public String getrP() {
		return rP;
	}

	public void setrP(String rP) {
		this.rP = rP;
	}

	public String getyP() {
		return yP;
	}

	public void setyP(String yP) {
		this.yP = yP;
	}

	public String getbP() {
		return bP;
	}

	public void setbP(String bP) {
		this.bP = bP;
	}

	public String getPvV() {
		return pvV;
	}

	public void setPv1V(String pvv) {
		pvV = pvv;
	}

	public String getPvC() {
		return pvC;
	}

	public void setPvC(String pvc) {
		pvC = pvc;
	}

	public String getSptot() {
		return sptot;
	}

	public void setSptot(String sptot) {
		this.sptot = sptot;
	}

	public String getSetoday() {
		return setoday;
	}

	public void setSetoday(String setoday) {
		this.setoday = setoday;
	}

	public String getSetot() {
		return setot;
	}

	public void setSetot(String setot) {
		this.setot = setot;
	}

	public String getBatBV() {
		return batBV;
	}

	public void setBatBV(String batBV) {
		this.batBV = batBV;
	}

	public String getBatCC() {
		return batCC;
	}

	public void setBatCC(String batCC) {
		this.batCC = batCC;
	}

	public String getBatDC() {
		return batDC;
	}

	public void setBatDC(String batDC) {
		this.batDC = batDC;
	}

	public String getBatBT() {
		return batBT;
	}

	public void setBatBT(String batBT) {
		this.batBT = batBT;
	}

	public String getBatBP() {
		return batBP;
	}

	public void setBatBP(String batBP) {
		this.batBP = batBP;
	}

	public String getBatCE() {
		return batCE;
	}

	public void setBatCE(String batCE) {
		this.batCE = batCE;
	}

	public String getBatDE() {
		return batDE;
	}

	public void setBatDE(String batDE) {
		this.batDE = batDE;
	}

	public String getBatSOC() {
		return batSOC;
	}

	public void setBatSOC(String batSOC) {
		this.batSOC = batSOC;
	}

	public String getMpptBC() {
		return mpptBC;
	}

	public void setMpptBC(String mpptBC) {
		this.mpptBC = mpptBC;
	}

	public String getMpptBP() {
		return mpptBP;
	}

	public void setMpptBP(String mpptBP) {
		this.mpptBP = mpptBP;
	}

	public String getAcLCR() {
		return acLCR;
	}

	public void setAcLCR(String acLCR) {
		this.acLCR = acLCR;
	}

	public String getAcLCY() {
		return acLCY;
	}

	public void setAcLCY(String acLCY) {
		this.acLCY = acLCY;
	}

	public String getAcLCB() {
		return acLCB;
	}

	public void setAcLCB(String acLCB) {
		this.acLCB = acLCB;
	}

	public String getAcLF() {
		return acLF;
	}

	public void setAcLF(String acLF) {
		this.acLF = acLF;
	}

	public String getAcLPF() {
		return acLPF;
	}

	public void setAcLPF(String acLPF) {
		this.acLPF = acLPF;
	}

	public String getAcLPMaxDay() {
		return acLPMaxDay;
	}

	public void setAcLPMaxDay(String acLPMaxDay) {
		this.acLPMaxDay = acLPMaxDay;
	}

	public String getAcLVR() {
		return acLVR;
	}

	public void setAcLVR(String acLVR) {
		this.acLVR = acLVR;
	}

	public String getAcLVY() {
		return acLVY;
	}

	public void setAcLVY(String acLVY) {
		this.acLVY = acLVY;
	}

	public String getAcLVB() {
		return acLVB;
	}

	public void setAcLVB(String acLVB) {
		this.acLVB = acLVB;
	}

	public String getAcLP() {
		return acLP;
	}

	public void setAcLP(String acLP) {
		this.acLP = acLP;
	}

	public String getAcLEtoday() {
		return acLEtoday;
	}

	public void setAcLEtoday(String acLEtoday) {
		this.acLEtoday = acLEtoday;
	}

	public String getAcLETP() {
		return acLETP;
	}

	public void setAcLETP(String acLETP) {
		this.acLETP = acLETP;
	}

	public String getAcLETNP() {
		return acLETNP;
	}

	public void setAcLETNP(String acLETNP) {
		this.acLETNP = acLETNP;
	}

	public String getEnergyMeterCount() {
		return energyMeterCount;
	}

	public void setEnergyMeterCount(String energyMeterCount) {
		this.energyMeterCount = energyMeterCount;
	}

	public String getTypeOfInverter() {
		return typeOfInverter;
	}

	public void setTypeOfInverter(String typeOfInverter) {
		this.typeOfInverter = typeOfInverter;
	}

	public String getLvdLevel() {
		return lvdLevel;
	}

	public void setLvdLevel(String lvdLevel) {
		this.lvdLevel = lvdLevel;
	}

	public String getDgStartCommandEnable() {
		return dgStartCommandEnable;
	}

	public void setDgStartCommandEnable(String dgStartCommandEnable) {
		this.dgStartCommandEnable = dgStartCommandEnable;
	}

	public String getDgRuntimeSettings() {
		return dgRuntimeSettings;
	}

	public void setDgRuntimeSettings(String dgRuntimeSettings) {
		this.dgRuntimeSettings = dgRuntimeSettings;
	}

	public String getDgStartBV() {
		return dgStartBV;
	}

	public void setDgStartBV(String dgStartBV) {
		this.dgStartBV = dgStartBV;
	}

	public String getBulkV() {
		return bulkV;
	}

	public void setBulkV(String bulkV) {
		this.bulkV = bulkV;
	}

	public String getFloatV() {
		return floatV;
	}

	public void setFloatV(String floatV) {
		this.floatV = floatV;
	}

	public String getAcChargerStartV() {
		return acChargerStartV;
	}

	public void setAcChargerStartV(String acChargerStartV) {
		this.acChargerStartV = acChargerStartV;
	}

	public String getAcChargerStopV() {
		return acChargerStopV;
	}

	public void setAcChargerStopV(String acChargerStopV) {
		this.acChargerStopV = acChargerStopV;
	}

	public String getGridChargerBatCLimit() {
		return gridChargerBatCLimit;
	}

	public void setGridChargerBatCLimit(String gridChargerBatCLimit) {
		this.gridChargerBatCLimit = gridChargerBatCLimit;
	}

	public String getMaxBatCL() {
		return maxBatCL;
	}

	public void setMaxBatCL(String maxBatCL) {
		this.maxBatCL = maxBatCL;
	}

	public String getMaxExportPower() {
		return maxExportPower;
	}

	public void setMaxExportPower(String maxExportPower) {
		this.maxExportPower = maxExportPower;
	}

	public String getWindSpeed() {
		return windSpeed;
	}

	public void setWindSpeed(String windSpeed) {
		this.windSpeed = windSpeed;
	}

	public String getWindDirection() {
		return windDirection;
	}

	public void setWindDirection(String windDirection) {
		this.windDirection = windDirection;
	}

	public String getIrradiance() {
		return irradiance;
	}

	public void setIrradiance(String irradiance) {
		this.irradiance = irradiance;
	}

	public String getPvCellTemp() {
		return pvCellTemp;
	}

	public void setPvCellTemp(String pvCellTemp) {
		this.pvCellTemp = pvCellTemp;
	}

	public String getAmbientTemperature1() {
		return ambientTemperature1;
	}

	public void setAmbientTemperature1(String ambientTemperature1) {
		this.ambientTemperature1 = ambientTemperature1;
	}

	public String getAmbientTemperature2() {
		return ambientTemperature2;
	}

	public void setAmbientTemperature2(String ambientTemperature2) {
		this.ambientTemperature2 = ambientTemperature2;
	}

	public String getdI1() {
		return dI1;
	}

	public void setdI1(String dI1) {
		this.dI1 = dI1;
	}

	public String getdI2() {
		return dI2;
	}

	public void setdI2(String dI2) {
		this.dI2 = dI2;
	}

	public String getdI3() {
		return dI3;
	}

	public void setdI3(String dI3) {
		this.dI3 = dI3;
	}

	public String getdI4() {
		return dI4;
	}

	public void setdI4(String dI4) {
		this.dI4 = dI4;
	}

	public String getdI5() {
		return dI5;
	}

	public void setdI5(String dI5) {
		this.dI5 = dI5;
	}

	public String getdI6() {
		return dI6;
	}

	public void setdI6(String dI6) {
		this.dI6 = dI6;
	}

	public String getdI7() {
		return dI7;
	}

	public void setdI7(String dI7) {
		this.dI7 = dI7;
	}

	public String getdI8() {
		return dI8;
	}

	public void setdI8(String dI8) {
		this.dI8 = dI8;
	}

	public String getdO1() {
		return dO1;
	}

	public void setdO1(String dO1) {
		this.dO1 = dO1;
	}

	public String getdO2() {
		return dO2;
	}

	public void setdO2(String dO2) {
		this.dO2 = dO2;
	}

	public String getdO3() {
		return dO3;
	}

	public void setdO3(String dO3) {
		this.dO3 = dO3;
	}

	public String getdO4() {
		return dO4;
	}

	public void setdO4(String dO4) {
		this.dO4 = dO4;
	}

	public String getdO5() {
		return dO5;
	}

	public void setdO5(String dO5) {
		this.dO5 = dO5;
	}

	public String getdO6() {
		return dO6;
	}

	public void setdO6(String dO6) {
		this.dO6 = dO6;
	}

	public String getdO7() {
		return dO7;
	}

	public void setdO7(String dO7) {
		this.dO7 = dO7;
	}

	public String getdO8() {
		return dO8;
	}

	public void setdO8(String dO8) {
		this.dO8 = dO8;
	}

	public String getAnalog5() {
		return analog5;
	}

	public void setAnalog5(String analog5) {
		this.analog5 = analog5;
	}

	public String getSolarV_S1() {
		return solarV_S1;
	}

	public void setSolarV_S1(String solarV_S1) {
		this.solarV_S1 = solarV_S1;
	}

	public String getSolarC_S1() {
		return solarC_S1;
	}

	public void setSolarC_S1(String solarC_S1) {
		this.solarC_S1 = solarC_S1;
	}

	public String getSolarE_S1() {
		return solarE_S1;
	}

	public void setSolarE_S1(String solarE_S1) {
		this.solarE_S1 = solarE_S1;
	}

	public String getTotSolarE1_S1() {
		return totSolarE1_S1;
	}

	public void setTotSolarE1_S1(String totSolarE1_S1) {
		this.totSolarE1_S1 = totSolarE1_S1;
	}

	public String getTotSolarE2_S1() {
		return totSolarE2_S1;
	}

	public void setTotSolarE2_S1(String totSolarE2_S1) {
		this.totSolarE2_S1 = totSolarE2_S1;
	}

	public String getSolarPower_S1() {
		return solarPower_S1;
	}

	public void setSolarPower_S1(String solarPower_S1) {
		this.solarPower_S1 = solarPower_S1;
	}

	public String getSolarV_S2() {
		return solarV_S2;
	}

	public void setSolarV_S2(String solarV_S2) {
		this.solarV_S2 = solarV_S2;
	}

	public String getSolarC_S2() {
		return solarC_S2;
	}

	public void setSolarC_S2(String solarC_S2) {
		this.solarC_S2 = solarC_S2;
	}

	public String getSolarE_S2() {
		return solarE_S2;
	}

	public void setSolarE_S2(String solarE_S2) {
		this.solarE_S2 = solarE_S2;
	}

	public String getTotSolarE1_S2() {
		return totSolarE1_S2;
	}

	public void setTotSolarE1_S2(String totSolarE1_S2) {
		this.totSolarE1_S2 = totSolarE1_S2;
	}

	public String getTotSolarE2_S2() {
		return totSolarE2_S2;
	}

	public void setTotSolarE2_S2(String totSolarE2_S2) {
		this.totSolarE2_S2 = totSolarE2_S2;
	}

	public String getSolarPower_S2() {
		return solarPower_S2;
	}

	public void setSolarPower_S2(String solarPower_S2) {
		this.solarPower_S2 = solarPower_S2;
	}

	public String getSolarV_S3() {
		return solarV_S3;
	}

	public void setSolarV_S3(String solarV_S3) {
		this.solarV_S3 = solarV_S3;
	}

	public String getSolarC_S3() {
		return solarC_S3;
	}

	public void setSolarC_S3(String solarC_S3) {
		this.solarC_S3 = solarC_S3;
	}

	public String getSolarE_S3() {
		return solarE_S3;
	}

	public void setSolarE_S3(String solarE_S3) {
		this.solarE_S3 = solarE_S3;
	}

	public String getTotSolarE1_S3() {
		return totSolarE1_S3;
	}

	public void setTotSolarE1_S3(String totSolarE1_S3) {
		this.totSolarE1_S3 = totSolarE1_S3;
	}

	public String getTotSolarE2_S3() {
		return totSolarE2_S3;
	}

	public void setTotSolarE2_S3(String totSolarE2_S3) {
		this.totSolarE2_S3 = totSolarE2_S3;
	}

	public String getSolarPower_S3() {
		return solarPower_S3;
	}

	public void setSolarPower_S3(String solarPower_S3) {
		this.solarPower_S3 = solarPower_S3;
	}

	public void read(String[] deviceData) {
		long epochTime = hex2decimal(deviceData[5]);
		this.dateTime = new Date(epochTime * 1000);
		System.out.println("dateTime: " + dateTime);
		int statusInDec = hex2decimal(deviceData[8]);
		this.statusBit = Integer.toBinaryString(statusInDec);
		System.out.println("statusBit: " + statusBit);
		this.lattitude = deviceData[12];
		this.longitude = deviceData[13];
		this.speed = Double.valueOf(deviceData[16]).intValue();
		this.requestId = hex2decimal(deviceData[25]);
		System.out.println("requestId: " + requestId);
		String modBusData = deviceData[26];
		if (!modBusData.equalsIgnoreCase(";")
				&& !modBusData.equalsIgnoreCase("")) {
			System.out.println("modBusData: " + modBusData);
			DataInputStream in = new DataInputStream(new ByteArrayInputStream(
					modBusData.getBytes()));
			try {
				switch (requestId) {
				case 1:
					// String oilPressure = readByte(in);
					// this.oilPressure = String.valueOf(hex2Long(oilPressure));
					String coolandTemperature = readByte(in);
					this.coolandTemperature = String
							.valueOf(hex2Long(coolandTemperature));
					String oilTemperature = readByte(in);
					this.oilTemperature = String
							.valueOf(hex2decimal(oilTemperature));
					String fuelLeval = readByte(in);
					this.fuelLeval = String.valueOf(hex2decimal(fuelLeval));
					String chargeAlternatorVoltage = readByte(in);
					this.chargeAlternatorVoltage = String
							.valueOf(hex2Long(chargeAlternatorVoltage) / 10);
					String batteryVoltage = readByte(in);
					this.batteryVoltage = String
							.valueOf(hex2Long(batteryVoltage) / 10);
					String engineSpeed = readByte(in);
					this.engineSpeed = String.valueOf(hex2decimal(engineSpeed));
					String generatorFrequency = readByte(in);
					this.generatorFrequency = String
							.valueOf(hex2Long(generatorFrequency) / 10);
					String genL1_N_V = readByte(in) + readByte(in);
					this.genL1_N_V = String
							.valueOf(hex2decimal(genL1_N_V) / 10);
					String genL2_N_V = readByte(in) + readByte(in);
					this.genL2_N_V = String
							.valueOf(hex2decimal(genL2_N_V) / 10);
					String genL3_N_V = readByte(in) + readByte(in);
					this.genL3_N_V = String
							.valueOf(hex2decimal(genL3_N_V) / 10);
					String genL1_L2_V = readByte(in) + readByte(in);
					this.genL1_L2_V = String
							.valueOf(hex2decimal(genL1_L2_V) / 10);
					String genL2_L3_V = readByte(in) + readByte(in);
					this.genL2_L3_V = String
							.valueOf(hex2decimal(genL2_L3_V) / 10);
					String genL3_L1_V = readByte(in) + readByte(in);
					this.genL3_L1_V = String
							.valueOf(hex2decimal(genL3_L1_V) / 10);
					String genL1C = readByte(in) + readByte(in);
					this.genL1C = String.valueOf(hex2decimal(genL1C) / 10);
					String genL2C = readByte(in) + readByte(in);
					this.genL2C = String.valueOf(hex2decimal(genL2C) / 10);
					String genL3C = readByte(in) + readByte(in);
					this.genL3C = String.valueOf(hex2decimal(genL3C) / 10);
					String genEarthC = readByte(in) + readByte(in);
					this.genEarthC = String
							.valueOf(hex2decimal(genEarthC) / 10);
					String genL1W = readByte(in) + readByte(in);
					this.genL1W = String.valueOf(hex2decimal(genL1W));
					String genL2W = readByte(in) + readByte(in);
					this.genL2W = String.valueOf(hex2decimal(genL2W));
					String genL3W = readByte(in) + readByte(in);
					this.genL3W = String.valueOf(hex2decimal(genL3W));
					String genC_lag = readByte(in);
					this.genC_lag = String.valueOf(hex2decimal(genC_lag));
					String mainsFrequency = readByte(in);
					this.mainsFrequency = String
							.valueOf(hex2decimal(mainsFrequency) / 10);
					String mainsL1_N_V = readByte(in) + readByte(in);
					this.mainsL1_N_V = String
							.valueOf(hex2decimal(mainsL1_N_V) / 10);
					String mainsL2_N_V = readByte(in) + readByte(in);
					this.mainsL2_N_V = String
							.valueOf(hex2decimal(mainsL2_N_V) / 10);
					String mainsL3_N_V = readByte(in) + readByte(in);
					this.mainsL3_N_V = String
							.valueOf(hex2decimal(mainsL3_N_V) / 10);
					String mainsL1L2_N_V = readByte(in) + readByte(in);
					this.mainsL1L2_N_V = String
							.valueOf(hex2decimal(mainsL1L2_N_V) / 10);
					String mainsL2L3_N_V = readByte(in) + readByte(in);
					this.mainsL2L3_N_V = String
							.valueOf(hex2decimal(mainsL2L3_N_V) / 10);
					String mainsL3L1_N_V = readByte(in) + readByte(in);
					this.mainsL3L1_N_V = String
							.valueOf(hex2decimal(mainsL3L1_N_V) / 10);
					String mainsV_lag = readByte(in);
					this.mainsV_lag = String.valueOf(hex2decimal(mainsV_lag));
					String gen_P_R = readByte(in);
					this.gen_P_R = String.valueOf(hex2decimal(gen_P_R));
					String mains_P_R = readByte(in);
					this.mains_P_R = String.valueOf(hex2decimal(mains_P_R));
					String mainsC_lag = readByte(in);
					this.mainsC_lag = String.valueOf(hex2decimal(mainsC_lag));
					String mainsL1C = readByte(in) + readByte(in);
					this.mainsL1C = String.valueOf(hex2decimal(mainsL1C) / 10);
					String mainsL2C = readByte(in) + readByte(in);
					this.mainsL2C = String.valueOf(hex2decimal(mainsL2C) / 10);
					String mainsL3C = readByte(in) + readByte(in);
					this.mainsL3C = String.valueOf(hex2decimal(mainsL3C) / 10);
					String mainsEarthC = readByte(in) + readByte(in);
					this.mainsEarthC = String
							.valueOf(hex2decimal(mainsEarthC) / 10);
					String mainsL1W = readByte(in) + readByte(in);
					this.mainsL1W = String.valueOf(hex2decimal(mainsL1W));
					String mainsL2W = readByte(in) + readByte(in);
					this.mainsL2W = String.valueOf(hex2decimal(mainsL2W));
					String mainsL3W = readByte(in) + readByte(in);
					this.mainsL3W = String.valueOf(hex2decimal(mainsL3W));
					int count = in.available();
					byte[] junk = new byte[count];
					in.readFully(junk);
					break;
				case 2:
					String busC_lag = readByte(in);
					this.busC_lag = String.valueOf(hex2decimal(busC_lag));
					String busFrequency = readByte(in);
					this.busFrequency = String
							.valueOf(hex2decimal(busFrequency) / 10);
					String busL1_N_V = readByte(in) + readByte(in);
					this.busL1_N_V = String
							.valueOf(hex2decimal(busL1_N_V) / 10);
					String busL2_N_V = readByte(in) + readByte(in);
					this.busL2_N_V = String
							.valueOf(hex2decimal(busL2_N_V) / 10);
					String busL3_N_V = readByte(in) + readByte(in);
					this.busL3_N_V = String
							.valueOf(hex2decimal(busL3_N_V) / 10);
					String busL1L2_N_V = readByte(in) + readByte(in);
					this.busL1L2_N_V = String
							.valueOf(hex2decimal(busL1L2_N_V) / 10);
					String busL2L3_N_V = readByte(in) + readByte(in);
					this.busL2L3_N_V = String
							.valueOf(hex2decimal(busL2L3_N_V) / 10);
					String busL3L1_N_V = readByte(in) + readByte(in);
					this.busL3L1_N_V = String
							.valueOf(hex2decimal(busL3L1_N_V) / 10);
					String busL1C = readByte(in) + readByte(in);
					this.busL1C = String.valueOf(hex2decimal(busL1C) / 10);
					String busL2C = readByte(in) + readByte(in);
					this.busL2C = String.valueOf(hex2decimal(busL2C) / 10);
					String busL3C = readByte(in) + readByte(in);
					this.busL3C = String.valueOf(hex2decimal(busL3C) / 10);
					String busEarthC = readByte(in) + readByte(in);
					this.busEarthC = String
							.valueOf(hex2decimal(busEarthC) / 10);
					int count2 = in.available();
					if (count2 >= 32) {
						String busL1W = readByte(in) + readByte(in);
						this.busL1W = String.valueOf(hex2decimal(busL1W));
						String busL2W = readByte(in) + readByte(in);
						this.busL2W = String.valueOf(hex2decimal(busL2W));
						String busL3W = readByte(in) + readByte(in);
						this.busL3W = String.valueOf(hex2decimal(busL3W));
						String bus_P_R = readByte(in);
						this.bus_P_R = String.valueOf(hex2decimal(bus_P_R));
						byte[] junk2 = new byte[count2 - 32];
						in.readFully(junk2);
					}
					break;
				case 3:
					String gen_tot_W = readByte(in) + readByte(in);
					this.gen_tot_W = String.valueOf(hex2decimal(gen_tot_W));
					String genL1VA = readByte(in) + readByte(in);
					this.genL1VA = String.valueOf(hex2decimal(genL1VA));
					String genL2VA = readByte(in) + readByte(in);
					this.genL2VA = String.valueOf(hex2decimal(genL2VA));
					String genL3VA = readByte(in) + readByte(in);
					this.genL3VA = String.valueOf(hex2decimal(genL3VA));
					String gentotVA = readByte(in) + readByte(in);
					this.gentotVA = String.valueOf(hex2decimal(gentotVA));
					String genL1VAr = readByte(in) + readByte(in);
					this.genL1VAr = String.valueOf(hex2decimal(genL1VAr));
					String genL2VAr = readByte(in) + readByte(in);
					this.genL2VAr = String.valueOf(hex2decimal(genL2VAr));
					String genL3VAr = readByte(in) + readByte(in);
					this.genL3VAr = String.valueOf(hex2decimal(genL3VAr));
					String gentotVAr = readByte(in) + readByte(in);
					this.gentotVAr = String.valueOf(hex2decimal(gentotVAr));
					String genPFL1 = readByte(in);
					this.genPFL1 = String.valueOf(hex2decimal(genPFL1) / 10);
					String genPFL2 = readByte(in);
					this.genPFL2 = String.valueOf(hex2decimal(genPFL2) / 10);
					String genPFL3 = readByte(in);
					this.genPFL3 = String.valueOf(hex2decimal(genPFL3) / 10);
					String genPFAvg = readByte(in);
					this.genPFAvg = String.valueOf(hex2decimal(genPFAvg) / 10);
					String genFullPower = readByte(in);
					this.genFullPower = String
							.valueOf(hex2decimal(genFullPower));
					String genFullVAr = readByte(in);
					this.genFullVAr = String.valueOf(hex2decimal(genFullVAr));
					String mains_tot_W = readByte(in) + readByte(in);
					this.mains_tot_W = String.valueOf(hex2decimal(mains_tot_W));
					String mainsL1VA = readByte(in) + readByte(in);
					this.mainsL1VA = String.valueOf(hex2decimal(mainsL1VA));
					String mainsL2VA = readByte(in) + readByte(in);
					this.mainsL2VA = String.valueOf(hex2decimal(mainsL2VA));
					String mainsL3VA = readByte(in) + readByte(in);
					this.mainsL3VA = String.valueOf(hex2decimal(mainsL3VA));
					String mainstotVA = readByte(in) + readByte(in);
					this.mainstotVA = String.valueOf(hex2decimal(mainstotVA));
					String mainsL1VAr = readByte(in) + readByte(in);
					this.mainsL1VAr = String.valueOf(hex2decimal(mainsL1VAr));
					String mainsL2VAr = readByte(in) + readByte(in);
					this.mainsL2VAr = String.valueOf(hex2decimal(mainsL2VAr));
					String mainsL3VAr = readByte(in) + readByte(in);
					this.mainsL3VAr = String.valueOf(hex2decimal(mainsL3VAr));
					String mainstotVAr = readByte(in) + readByte(in);
					this.mainstotVAr = String.valueOf(hex2decimal(mainstotVAr));
					String mainsPFL1 = readByte(in);
					this.mainsPFL1 = String
							.valueOf(hex2decimal(mainsPFL1) / 100);
					String mainsPFL2 = readByte(in);
					this.mainsPFL2 = String
							.valueOf(hex2decimal(mainsPFL2) / 100);
					String mainsPFL3 = readByte(in);
					this.mainsPFL3 = String
							.valueOf(hex2decimal(mainsPFL3) / 100);
					String mainsPFAvg = readByte(in);
					this.mainsPFAvg = String
							.valueOf(hex2decimal(mainsPFAvg) / 100);
					int count3 = in.available();
					byte[] junk3 = new byte[count3];
					in.readFully(junk3);
					break;
				case 4:

					in.readInt();
					in.readInt();
					in.readInt();
					in.readInt();
					in.readInt();
					in.readInt();
					in.readInt();
					in.readInt();
					in.readInt();
					in.readInt();
					in.readInt();
					in.readInt();
					String genL1lag = readByte(in);
					this.genL1lag = String.valueOf(hex2decimal(genL1lag));
					String genL2lag = readByte(in);
					this.genL2lag = String.valueOf(hex2decimal(genL2lag));
					String genL3lag = readByte(in);
					this.genL3lag = String.valueOf(hex2decimal(genL3lag));
					String gentotlag = readByte(in);
					this.gentotlag = String.valueOf(hex2decimal(gentotlag));
					String genL1FullPower = readByte(in);
					this.genL1FullPower = String
							.valueOf(hex2decimal(genL1FullPower) / 10);
					String genL2FullPower = readByte(in);
					this.genL2FullPower = String
							.valueOf(hex2decimal(genL2FullPower) / 10);
					String genL3FullPower = readByte(in);
					this.genL3FullPower = String
							.valueOf(hex2decimal(genL3FullPower) / 10);
					int count4 = in.available();
					byte[] junk4 = new byte[count4];
					in.readFully(junk4);
					break;
				case 5:
					String currentTime = readByte(in) + readByte(in);
					this.currentTime = String.valueOf(hex2decimal(currentTime));
					String timeToNextEngineMainatenance = readByte(in)
							+ readByte(in);
					this.timeToNextEngineMainatenance = String
							.valueOf(hex2decimal(timeToNextEngineMainatenance));
					String timeOfNextEngineMainatenance = readByte(in)
							+ readByte(in);
					this.timeOfNextEngineMainatenance = String
							.valueOf(hex2decimal(timeOfNextEngineMainatenance));
					String engineRunTime = readByte(in) + readByte(in);
					this.engineRunTime = String
							.valueOf(hex2decimal(engineRunTime));
					String genPositiveKWH = readByte(in) + readByte(in);
					this.genPositiveKWH = String
							.valueOf(hex2decimal(genPositiveKWH) / 10);
					String genNegativeKWH = readByte(in) + readByte(in);
					this.genNegativeKWH = String
							.valueOf(hex2decimal(genNegativeKWH) / 10);
					String genKVAH = readByte(in) + readByte(in);
					this.genKVAH = String.valueOf(hex2decimal(genKVAH) / 10);
					String genKVArH = readByte(in) + readByte(in);
					this.genKVArH = String.valueOf(hex2decimal(genKVArH) / 10);
					String numberOfStart = readByte(in) + readByte(in);
					this.numberOfStart = String
							.valueOf(hex2decimal(numberOfStart));
					String mainsPositiveKWH = readByte(in) + readByte(in);
					this.mainsPositiveKWH = String
							.valueOf(hex2decimal(mainsPositiveKWH) / 10);
					String mainsNegativeKWH = readByte(in) + readByte(in);
					this.mainsNegativeKWH = String
							.valueOf(hex2decimal(mainsNegativeKWH) / 10);
					String mainsKVAH = readByte(in) + readByte(in);
					this.mainsKVAH = String
							.valueOf(hex2decimal(mainsKVAH) / 10);
					String mainsKVArH = readByte(in) + readByte(in);
					this.mainsKVArH = String
							.valueOf(hex2decimal(mainsKVArH) / 10);
					String busPositiveKWH = readByte(in) + readByte(in);
					this.busPositiveKWH = String
							.valueOf(hex2decimal(busPositiveKWH) / 10);
					String busNegativeKWH = readByte(in) + readByte(in);
					this.busNegativeKWH = String
							.valueOf(hex2decimal(busNegativeKWH) / 10);
					String busKVAH = readByte(in) + readByte(in);
					this.busKVAH = String.valueOf(hex2decimal(busKVAH) / 10);
					String busKVArH = readByte(in) + readByte(in);
					this.busKVArH = String.valueOf(hex2decimal(busKVArH) / 10);
					String fuelUsed = readByte(in) + readByte(in);
					this.fuelUsed = String.valueOf(hex2decimal(fuelUsed));
					String maxPositiveMains_ROCOF = readByte(in) + readByte(in);
					this.maxPositiveMains_ROCOF = String
							.valueOf(hex2decimal(maxPositiveMains_ROCOF) / 100);
					String maxNegativeMains_ROCOF = readByte(in) + readByte(in);
					this.maxNegativeMains_ROCOF = String
							.valueOf(hex2decimal(maxNegativeMains_ROCOF) / 100);
					String maxPositiveMains_Vector = readByte(in)
							+ readByte(in);
					this.maxPositiveMains_Vector = String
							.valueOf(hex2decimal(maxPositiveMains_Vector) / 10);
					String maxNegativeMains_Vector = readByte(in)
							+ readByte(in);
					this.maxNegativeMains_Vector = String
							.valueOf(hex2decimal(maxNegativeMains_Vector) / 10);
					String timeToNextEngineMainatenanceAlerm1 = readByte(in)
							+ readByte(in);
					this.timeToNextEngineMainatenanceAlerm1 = String
							.valueOf(hex2decimal(timeToNextEngineMainatenanceAlerm1));
					String timeOfNextEngineMainatenanceAlerm1 = readByte(in)
							+ readByte(in);
					this.timeOfNextEngineMainatenanceAlerm1 = String
							.valueOf(hex2decimal(timeOfNextEngineMainatenanceAlerm1));
					String timeToNextEngineMainatenanceAlerm2 = readByte(in)
							+ readByte(in);
					this.timeToNextEngineMainatenanceAlerm2 = String
							.valueOf(hex2decimal(timeToNextEngineMainatenanceAlerm2));
					String timeOfNextEngineMainatenanceAlerm2 = readByte(in)
							+ readByte(in);
					this.timeOfNextEngineMainatenanceAlerm2 = String
							.valueOf(hex2decimal(timeOfNextEngineMainatenanceAlerm2));
					// String timeToNextEngineMainatenanceAlerm3 = readByte(in)
					// + readByte(in);
					// this.timeToNextEngineMainatenanceAlerm3 = String
					// .valueOf(hex2decimal(timeToNextEngineMainatenanceAlerm3));
					int count5 = in.available();
					byte[] junk5 = new byte[count5];
					in.readFully(junk5);
					break;
				case 6:
					String numberOfNamedAlarm = read(in);
					this.numberOfNamedAlarm = String
							.valueOf(hex2decimal(numberOfNamedAlarm));
					String register2049 = read(in);
					if (((Integer.parseInt(register2049, 16) >> 12) & 0x000f) != 1
							&& ((Integer.parseInt(register2049, 16) >> 12) & 0x000f) != 15) {
						setAlarm("Emergency stop");
					}
					if (((Integer.parseInt(register2049, 16) >> 8) & 0x000f) != 1
							&& ((Integer.parseInt(register2049, 16) >> 8) & 0x000f) != 15) {
						setAlarm("Low oil pressure");
					}
					if (((Integer.parseInt(register2049, 16) >> 4) & 0x000f) != 1
							&& ((Integer.parseInt(register2049, 16) >> 4) & 0x000f) != 15) {
						setAlarm("High coolant temperature");
					}
					if ((Integer.parseInt(register2049, 16) & 0x000f) != 1
							&& (Integer.parseInt(register2049, 16) & 0x000f) != 15) {
						setAlarm("Low coolant temperature");
					}
					String register2050 = read(in);
					if (((Integer.parseInt(register2050, 16) >> 12) & 0x000f) != 1
							&& ((Integer.parseInt(register2050, 16) >> 12) & 0x000f) != 15) {
						setAlarm("Under speed");
					}
					if (((Integer.parseInt(register2050, 16) >> 8) & 0x000f) != 1
							&& ((Integer.parseInt(register2050, 16) >> 8) & 0x000f) != 15) {
						setAlarm("Over speed");
					}
					if (((Integer.parseInt(register2050, 16) >> 4) & 0x000f) != 1
							&& ((Integer.parseInt(register2050, 16) >> 4) & 0x000f) != 15) {
						setAlarm("Generator Under frequency");
					}
					if ((Integer.parseInt(register2050, 16) & 0x000f) != 1
							&& (Integer.parseInt(register2050, 16) & 0x000f) != 15) {
						setAlarm("Generator Over frequency");
					}
					String register2051 = read(in);
					if (((Integer.parseInt(register2051, 16) >> 12) & 0x000f) != 1
							&& ((Integer.parseInt(register2051, 16) >> 12) & 0x000f) != 15) {
						setAlarm("Generator low voltage");
					}
					if (((Integer.parseInt(register2051, 16) >> 8) & 0x000f) != 1
							&& ((Integer.parseInt(register2051, 16) >> 8) & 0x000f) != 15) {
						setAlarm("Generator high voltage");
					}
					if (((Integer.parseInt(register2051, 16) >> 4) & 0x000f) != 1
							&& ((Integer.parseInt(register2051, 16) >> 4) & 0x000f) != 15) {
						setAlarm("Battery low voltage");
					}
					if ((Integer.parseInt(register2051, 16) & 0x000f) != 1
							&& (Integer.parseInt(register2051, 16) & 0x000f) != 15) {
						setAlarm("Battery high voltage");
					}
					String register2052 = read(in);
					if (((Integer.parseInt(register2052, 16) >> 12) & 0x000f) != 1
							&& ((Integer.parseInt(register2052, 16) >> 12) & 0x000f) != 15) {
						setAlarm("Charge alternator failure");
					}
					if (((Integer.parseInt(register2052, 16) >> 8) & 0x000f) != 1
							&& ((Integer.parseInt(register2052, 16) >> 8) & 0x000f) != 15) {
						setAlarm("Fail to start");
					}
					if (((Integer.parseInt(register2052, 16) >> 4) & 0x000f) != 1
							&& ((Integer.parseInt(register2052, 16) >> 4) & 0x000f) != 15) {
						setAlarm("Fail to stop");
					}
					if ((Integer.parseInt(register2052, 16) & 0x000f) != 1
							&& (Integer.parseInt(register2052, 16) & 0x000f) != 15) {
						setAlarm("Generator fail to close");
					}
					String register2053 = read(in);
					if (((Integer.parseInt(register2053, 16) >> 12) & 0x000f) != 1
							&& ((Integer.parseInt(register2053, 16) >> 12) & 0x000f) != 15) {
						setAlarm("Mains fail to close");
					}
					if (((Integer.parseInt(register2053, 16) >> 8) & 0x000f) != 1
							&& ((Integer.parseInt(register2053, 16) >> 8) & 0x000f) != 15) {
						setAlarm("Oil pressure sender fault");
					}
					if (((Integer.parseInt(register2053, 16) >> 4) & 0x000f) != 1
							&& ((Integer.parseInt(register2053, 16) >> 4) & 0x000f) != 15) {
						setAlarm("Loss of magnetic pick up");
					}
					if ((Integer.parseInt(register2053, 16) & 0x000f) != 1
							&& (Integer.parseInt(register2053, 16) & 0x000f) != 15) {
						setAlarm("Magnetic pick up open circuit");
					}
					String register2054 = read(in);
					if (((Integer.parseInt(register2054, 16) >> 12) & 0x000f) != 1
							&& ((Integer.parseInt(register2054, 16) >> 12) & 0x000f) != 15) {
						setAlarm("Generator high current");
					}
					if (((Integer.parseInt(register2054, 16) >> 8) & 0x000f) != 1
							&& ((Integer.parseInt(register2054, 16) >> 8) & 0x000f) != 15) {
						setAlarm("Calibration lost");
					}
					if (((Integer.parseInt(register2054, 16) >> 4) & 0x000f) != 1
							&& ((Integer.parseInt(register2054, 16) >> 4) & 0x000f) != 15) {
						setAlarm("Low fuel level");
					}
					if ((Integer.parseInt(register2054, 16) & 0x000f) != 1
							&& (Integer.parseInt(register2054, 16) & 0x000f) != 15) {
						setAlarm("CAN ECU Warning");
					}
					String register2055 = read(in);
					if (((Integer.parseInt(register2055, 16) >> 12) & 0x000f) != 1
							&& ((Integer.parseInt(register2055, 16) >> 12) & 0x000f) != 15) {
						setAlarm("CAN ECU Shutdown");
					}
					if (((Integer.parseInt(register2055, 16) >> 8) & 0x000f) != 1
							&& ((Integer.parseInt(register2055, 16) >> 8) & 0x000f) != 15) {
						setAlarm("CAN ECU Data fail");
					}
					if (((Integer.parseInt(register2055, 16) >> 4) & 0x000f) != 1
							&& ((Integer.parseInt(register2055, 16) >> 4) & 0x000f) != 15) {
						setAlarm("Low oil level switch");
					}
					if ((Integer.parseInt(register2055, 16) & 0x000f) != 1
							&& (Integer.parseInt(register2055, 16) & 0x000f) != 15) {
						setAlarm("High temperature switch");
					}
					String register2056 = read(in);
					if (((Integer.parseInt(register2056, 16) >> 12) & 0x000f) != 1
							&& ((Integer.parseInt(register2056, 16) >> 12) & 0x000f) != 15) {
						setAlarm("Low fuel level switch");
					}
					if (((Integer.parseInt(register2056, 16) >> 8) & 0x000f) != 1
							&& ((Integer.parseInt(register2056, 16) >> 8) & 0x000f) != 15) {
						setAlarm("Expansion unit watchdog alarm");
					}
					if (((Integer.parseInt(register2056, 16) >> 4) & 0x000f) != 1
							&& ((Integer.parseInt(register2056, 16) >> 4) & 0x000f) != 15) {
						setAlarm("kW overload alarm");
					}
					if ((Integer.parseInt(register2056, 16) & 0x000f) != 1
							&& (Integer.parseInt(register2056, 16) & 0x000f) != 15) {
						setAlarm("Negative phase sequence current alarm");
					}
					String register2057 = read(in);
					if (((Integer.parseInt(register2057, 16) >> 12) & 0x000f) != 1
							&& ((Integer.parseInt(register2057, 16) >> 12) & 0x000f) != 15) {
						setAlarm("Earth fault trip alarm");
					}
					if (((Integer.parseInt(register2057, 16) >> 8) & 0x000f) != 1
							&& ((Integer.parseInt(register2057, 16) >> 8) & 0x000f) != 15) {
						setAlarm("Generator phase rotation alarm");
					}
					if (((Integer.parseInt(register2057, 16) >> 4) & 0x000f) != 1
							&& ((Integer.parseInt(register2057, 16) >> 4) & 0x000f) != 15) {
						setAlarm("Auto Voltage Sense Fail");
					}
					if ((Integer.parseInt(register2057, 16) & 0x000f) != 1
							&& (Integer.parseInt(register2057, 16) & 0x000f) != 15) {
						setAlarm("Maintenance alarm");
					}
					String register2058 = read(in);
					if (((Integer.parseInt(register2058, 16) >> 12) & 0x000f) != 1
							&& ((Integer.parseInt(register2058, 16) >> 12) & 0x000f) != 15) {
						setAlarm("Loading frequency alarm");
					}
					if (((Integer.parseInt(register2058, 16) >> 8) & 0x000f) != 1
							&& ((Integer.parseInt(register2058, 16) >> 8) & 0x000f) != 15) {
						setAlarm("Loading Voltage alarm");
					}
					if (((Integer.parseInt(register2058, 16) >> 4) & 0x000f) != 1
							&& ((Integer.parseInt(register2058, 16) >> 4) & 0x000f) != 15) {
						setAlarm("Fuel usage running");
					}
					if ((Integer.parseInt(register2058, 16) & 0x000f) != 1
							&& (Integer.parseInt(register2058, 16) & 0x000f) != 15) {
						setAlarm("Fuel usage stopped");
					}
					int count6 = in.available();
					byte[] junk6 = new byte[count6];
					in.readFully(junk6);
					break;
				case 7:
					int count7 = in.available();
					byte[] junk7 = new byte[count7];
					in.readFully(junk7);
					break;
				case 8:
					String rV = readByte(in);
					this.rV = String.valueOf(hex2Long(rV) / 10);
					String yV = readByte(in);
					this.yV = String.valueOf(hex2Long(yV) / 10);
					String bV = readByte(in);
					this.bV = String.valueOf(hex2Long(bV) / 10);
					String rC = readByte(in);
					this.rC = String.valueOf(hex2Long(rC) / 10);
					String yC = readByte(in);
					this.yC = String.valueOf(hex2Long(yC) / 10);
					String bC = readByte(in);
					this.bC = String.valueOf(hex2Long(bC) / 10);
					String rP = readByte(in);
					this.rP = String.valueOf(hex2Long(rP) / 10);
					String yP = readByte(in);
					this.yP = String.valueOf(hex2Long(yP) / 10);
					String bP = readByte(in);
					this.bP = String.valueOf(hex2Long(bP) / 10);
					String pvV = readByte(in);
					this.pvV = String.valueOf(hex2Long(pvV) / 10);
					String pvC = readByte(in);
					this.pvC = String.valueOf(hex2Long(pvC) / 10);
					String sptot = readByte(in);
					this.sptot = String.valueOf(hex2Long(sptot) / 10);
					String setoday = readByte(in);
					this.setoday = String.valueOf(hex2Long(setoday) / 10);
					String mediator = readByte(in);
					String setot = readByte(in) + mediator;
					this.setot = String.valueOf(hex2Long(setot) / 1000);
					String batBV = readByte(in);
					this.batBV = String.valueOf(hex2Long(batBV) / 10);
					String batCC = readByte(in);
					this.batCC = String.valueOf(hex2Long(batCC) / 10);
					String batDC = readByte(in);
					this.batDC = String.valueOf(hex2Long(batDC) / 10);
					String batBT = readByte(in);
					this.batBT = String.valueOf(hex2Long(batBT) / 10);
					String batBP = readByte(in);
					this.batBP = String.valueOf(hex2Long(batBP) / 10);
					String batCE = readByte(in);
					this.batCE = String.valueOf(hex2Long(batCE) / 10);
					String batDE = readByte(in);
					this.batDE = String.valueOf(hex2Long(batDE) / 10);
					String batSOC = readByte(in);
					this.batSOC = String.valueOf(hex2Long(batSOC));
					String mpptBC = readByte(in);
					this.mpptBC = String.valueOf(hex2Long(mpptBC) / 10);
					String mpptBP = readByte(in);
					this.mpptBP = String.valueOf(hex2Long(mpptBP) / 10);
					String acLCR = readByte(in);
					this.acLCR = String.valueOf(hex2Long(acLCR) / 10);
					String acLCY = readByte(in);
					this.acLCY = String.valueOf(hex2Long(acLCY) / 10);
					String acLCB = readByte(in);
					this.acLCB = String.valueOf(hex2Long(acLCB) / 10);
					String acLF = readByte(in);
					this.acLF = String.valueOf(hex2Long(acLF) / 10);
					String acLPF = readByte(in);
					this.acLPF = String.valueOf(hex2Long(acLPF) / 100);
					String acLPMaxDay = readByte(in);
					this.acLPMaxDay = String.valueOf(hex2Long(acLPMaxDay) / 10);
					String acLVR = readByte(in);
					this.acLVR = String.valueOf(hex2Long(acLVR) / 10);
					String acLVY = readByte(in);
					this.acLVY = String.valueOf(hex2Long(acLVY) / 10);
					String acLVB = readByte(in);
					this.acLVB = String.valueOf(hex2Long(acLVB) / 10);
					String acLP = readByte(in);
					this.acLP = String.valueOf(hex2Long(acLP) / 10);
					String acLEtoday = readByte(in);
					this.acLEtoday = String.valueOf(hex2Long(acLEtoday) / 10);
					mediator = readByte(in);
					String acLETP = readByte(in) + mediator;
					this.acLETP = String.valueOf(hex2Long(acLETP) / 10);
					mediator = readByte(in);
					String acLETNP = readByte(in) + mediator;
					this.acLETNP = String.valueOf(hex2Long(acLETNP) / 10);
					String energyMeterCount = readByte(in);
					this.energyMeterCount = String
							.valueOf(hex2Long(energyMeterCount));
					String typeOfInverter = readByte(in);
					this.typeOfInverter = String
							.valueOf(hex2Long(typeOfInverter));
					String lvdLevel = readByte(in);
					this.lvdLevel = String.valueOf(hex2Long(lvdLevel));
					mediator = readByte(in);
					String invertorAlarm = readByte(in) + mediator;
					int invertorAlarmBit = hex2decimal(invertorAlarm);
					if (((invertorAlarmBit >> 0) & 1) == 1) {
						setAlarm("Plant Battery Low voltage");
					}
					if (((invertorAlarmBit >> 1) & 1) == 1) {
						setAlarm("Plant Battery High voltage");
					}
					if (((invertorAlarmBit >> 2) & 1) == 1) {
						setAlarm("Plant Battery Equalization( absorved Stage)");
					}
					if (((invertorAlarmBit >> 3) & 1) == 1) {
						setAlarm("Plant Battery High Temperature");
					}
					if (((invertorAlarmBit >> 4) & 1) == 1) {
						setAlarm("Solar Charge Controller UV");
					}
					if (((invertorAlarmBit >> 5) & 1) == 1) {
						setAlarm("Solar Charge Controller OV");
					}
					if (((invertorAlarmBit >> 6) & 1) == 1) {
						setAlarm("Solar Charge Controller Over Temperature");
					}
					if (((invertorAlarmBit >> 7) & 1) == 1) {
						setAlarm("Solar Charge Controller Fail");
					}
					if (((invertorAlarmBit >> 8) & 1) == 1) {
						setAlarm("Inverter UV");
					}
					if (((invertorAlarmBit >> 9) & 1) == 1) {
						setAlarm("Inverter OV");
					}
					if (((invertorAlarmBit >> 10) & 1) == 1) {
						setAlarm("Inverter  Over Temperature");
					}
					if (((invertorAlarmBit >> 11) & 1) == 1) {
						setAlarm("Inverter  Fail");
					}
					if (((invertorAlarmBit >> 12) & 1) == 1) {
						setAlarm("Inverter Overload");
					}
					if (((invertorAlarmBit >> 13) & 1) == 1) {
						setAlarm("Rectifier UV");
					}
					if (((invertorAlarmBit >> 14) & 1) == 1) {
						setAlarm("Rectifier OV");
					}
					if (((invertorAlarmBit >> 15) & 1) == 1) {
						setAlarm("Rectifier  Over Temperature");
					}
					if (((invertorAlarmBit >> 16) & 1) == 1) {
						setAlarm("Rectifier  Fail");
					}
					if (((invertorAlarmBit >> 17) & 1) == 1) {
						setAlarm("Rectifier Overload");
					}
					if (((invertorAlarmBit >> 18) & 1) == 1) {
						setAlarm("Alarm on System output load < 0.25Amp ");
					}
					if (((invertorAlarmBit >> 19) & 1) == 1) {
						setAlarm("Grid under Voltage/ Fail");
					}
					if (((invertorAlarmBit >> 24) & 1) == 1) {
						setAlarm("Smoke Alarm");
					}
					if (((invertorAlarmBit >> 25) & 1) == 1) {
						setAlarm("Panel Door Open");
					}

					String dgStartCommandEnable = readByte(in);
					this.dgStartCommandEnable = String
							.valueOf(hex2Long(dgStartCommandEnable));
					String dgRuntimeSettings = readByte(in);
					this.dgRuntimeSettings = String
							.valueOf(hex2Long(dgRuntimeSettings));
					String dgStartBV = readByte(in);
					this.dgStartBV = String.valueOf(hex2Long(dgStartBV) / 10);
					String bulkV = readByte(in);
					this.bulkV = String.valueOf(hex2Long(bulkV));
					String floatV = readByte(in);
					this.floatV = String.valueOf(hex2Long(floatV));
					String acChargerStartV = readByte(in);
					this.acChargerStartV = String
							.valueOf(hex2Long(acChargerStartV));
					String acChargerStopV = readByte(in);
					this.acChargerStopV = String
							.valueOf(hex2Long(acChargerStopV));
					String gridChargerBatCLimit = readByte(in);
					this.gridChargerBatCLimit = String
							.valueOf(hex2Long(gridChargerBatCLimit));
					String maxBatCL = readByte(in);
					this.maxBatCL = String.valueOf(hex2Long(maxBatCL));
					int count8 = in.available();
					if (count8 > 0) {
						String maxExportPower = readByte(in);
						this.maxExportPower = String
								.valueOf(hex2Long(maxExportPower));
						byte[] junk8 = new byte[count8 - 1];
						in.readFully(junk8);
					}
					break;
				case 9:
					String windDirection = readByte(in);
					this.windDirection = String
							.valueOf(hex2Long(windDirection) / 10);
					String windSpeed = readByte(in);
					this.windSpeed = String.valueOf(hex2Long(windSpeed) / 10);
					String irradiance = readByte(in);
					this.irradiance = String.valueOf(hex2Long(irradiance) / 10);
					String pvCellTemp = readByte(in);
					this.pvCellTemp = String.valueOf(hex2Long(pvCellTemp) / 10);
					String ambientTemperature1 = readByte(in);
					this.ambientTemperature1 = String
							.valueOf(hex2Long(ambientTemperature1) / 10);
					String ambientTemperature2 = readByte(in);
					this.ambientTemperature2 = String
							.valueOf(hex2Long(ambientTemperature2) / 10);
					String analog5 = readByte(in);
					this.analog5 = String.valueOf(hex2Long(analog5));
					String diValues = readByte(in);
					int diValuesBit = hex2decimal(diValues);
					this.dI1 = String.valueOf((diValuesBit >> 0) & 1);
					this.dI2 = String.valueOf((diValuesBit >> 1) & 1);
					this.dI3 = String.valueOf((diValuesBit >> 2) & 1);
					this.dI4 = String.valueOf((diValuesBit >> 3) & 1);
					this.dI5 = String.valueOf((diValuesBit >> 4) & 1);
					this.dI6 = String.valueOf((diValuesBit >> 5) & 1);
					this.dI7 = String.valueOf((diValuesBit >> 6) & 1);
					this.dI8 = String.valueOf((diValuesBit >> 7) & 1);
					String dOValues = readByte(in);
					int dOValuesBit = hex2decimal(dOValues);
					this.dO1 = String.valueOf((dOValuesBit >> 0) & 1);
					this.dO2 = String.valueOf((dOValuesBit >> 1) & 1);
					this.dO3 = String.valueOf((dOValuesBit >> 2) & 1);
					this.dO4 = String.valueOf((dOValuesBit >> 3) & 1);
					this.dO5 = String.valueOf((dOValuesBit >> 4) & 1);
					this.dO6 = String.valueOf((dOValuesBit >> 5) & 1);
					this.dO7 = String.valueOf((dOValuesBit >> 6) & 1);
					this.dO8 = String.valueOf((dOValuesBit >> 7) & 1);
					int count9 = in.available();
					byte[] junk9 = new byte[count9];
					in.readFully(junk9);
					break;
				case 10:
					readByte(in);
					String solarV_S1 = readByte(in);
					this.solarV_S1 = String.valueOf(hex2Long(solarV_S1) / 10);
					String solarC_S1 = readByte(in);
					this.solarC_S1 = String.valueOf(hex2Long(solarC_S1) / 10);
					String solarE_S1 = readByte(in);
					this.solarE_S1 = String.valueOf(hex2Long(solarE_S1) / 10);
					String totSolarE1_S1 = readByte(in) + readByte(in);
					this.totSolarE1_S1 = String
							.valueOf(hex2Long(totSolarE1_S1) / 1000);
					String totSolarE2_S1 = readByte(in);
					this.totSolarE2_S1 = String
							.valueOf(hex2Long(totSolarE2_S1));
					String solarPower_S1 = readByte(in);
					this.solarPower_S1 = String
							.valueOf(hex2Long(solarPower_S1) / 10);
					int count10 = in.available();
					byte[] junk10 = new byte[count10];
					in.readFully(junk10);
					break;
				case 11:
					readByte(in);
					String solarV_S2 = readByte(in);
					this.solarV_S2 = String.valueOf(hex2Long(solarV_S2) / 10);
					String solarC_S2 = readByte(in);
					this.solarC_S2 = String.valueOf(hex2Long(solarC_S2) / 10);
					String solarE_S2 = readByte(in);
					this.solarE_S2 = String.valueOf(hex2Long(solarE_S2) / 10);
					String totSolarE1_S2 = readByte(in) + readByte(in);
					this.totSolarE1_S2 = String
							.valueOf(hex2Long(totSolarE1_S2) / 1000);
					String totSolarE2_S2 = readByte(in);
					this.totSolarE2_S2 = String
							.valueOf(hex2Long(totSolarE2_S2));
					String solarPower_S2 = readByte(in);
					this.solarPower_S2 = String
							.valueOf(hex2Long(solarPower_S2) / 10);
					int count11 = in.available();
					byte[] junk11 = new byte[count11];
					in.readFully(junk11);
					break;
				case 12:
					readByte(in);
					String solarV_S3 = readByte(in);
					this.solarV_S3 = String.valueOf(hex2Long(solarV_S3) / 10);
					String solarC_S3 = readByte(in);
					this.solarC_S3 = String.valueOf(hex2Long(solarC_S3) / 10);
					String solarE_S3 = readByte(in);
					this.solarE_S3 = String.valueOf(hex2Long(solarE_S3) / 10);
					String totSolarE1_S3 = readByte(in) + readByte(in);
					this.totSolarE1_S3 = String
							.valueOf(hex2Long(totSolarE1_S3) / 1000);
					String totSolarE2_S3 = readByte(in);
					this.totSolarE2_S3 = String
							.valueOf(hex2Long(totSolarE2_S3));
					String solarPower_S3 = readByte(in);
					this.solarPower_S3 = String
							.valueOf(hex2Long(solarPower_S3) / 10);
					int count12 = in.available();
					byte[] junk12 = new byte[count12];
					in.readFully(junk12);
					break;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public int hex2decimal(String s) {
		String digits = "0123456789ABCDEF";
		s = s.toUpperCase();
		int val = 0;
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			int d = digits.indexOf(c);
			val = 16 * val + d;
		}
		return val;
	}

	public float hex2Long(String s) {
		String digits = "0123456789ABCDEF";
		s = s.toUpperCase();
		float val = 0;
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			float d = (long) digits.indexOf(c);
			val = 16 * val + d;
		}
		return val;
	}

	public void setAlarm(String alarm) {
		if (this.alarm == null)
			this.alarm = alarm;
		else
			this.alarm += "," + alarm;
	}

	public String readByte(DataInputStream in) {
		StringBuilder sb = new StringBuilder();
		String result = "0";
		try {
			sb.append((char) in.readByte()).append((char) in.readByte())
					.append((char) in.readByte()).append((char) in.readByte());
			result = sb.toString();
			if (result.equalsIgnoreCase("fff8")
					|| result.equalsIgnoreCase("fff9")
					|| result.equalsIgnoreCase("fffa")
					|| result.equalsIgnoreCase("fffb")
					|| result.equalsIgnoreCase("fffc")
					|| result.equalsIgnoreCase("fffd")
					|| result.equalsIgnoreCase("fffe")
					|| result.equalsIgnoreCase("ffff")
					|| result.equalsIgnoreCase("7ff8")
					|| result.equalsIgnoreCase("7ff9")
					|| result.equalsIgnoreCase("7ffa")
					|| result.equalsIgnoreCase("7ffb")
					|| result.equalsIgnoreCase("7ffc")
					|| result.equalsIgnoreCase("7ffd")
					|| result.equalsIgnoreCase("7ffe")
					|| result.equalsIgnoreCase("7fff"))
				result = "0";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	public String read(DataInputStream in) {
		StringBuilder sb = new StringBuilder();
		String result = "ffff";
		try {
			sb.append((char) in.readByte()).append((char) in.readByte())
					.append((char) in.readByte()).append((char) in.readByte());
			result = sb.toString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
}
