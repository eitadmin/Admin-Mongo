package com.eiw.device.listener;

import com.eiw.device.handler.AIS140DeviceHandlerV1;
import com.eiw.device.handler.AIS140DeviceHandlerV2;
import com.eiw.device.handler.AIS140DeviceHandlerV3;
import com.eiw.device.handler.APMKT_AIS1401ADeviceHandler;
import com.eiw.device.handler.AndroidDeviceHandler;
import com.eiw.device.handler.CantrackDeviceHandler;
import com.eiw.device.handler.ConcoxDeviceHandler;
import com.eiw.device.handler.DeepSeaDeviceHandler;
import com.eiw.device.handler.DeepSeaDeviceHandlerTest;
import com.eiw.device.handler.DeepseaDeviceHandlerV2;
import com.eiw.device.handler.DeepseaDeviceHandler_V1;
import com.eiw.device.handler.ItracDeviceHandler1;
import com.eiw.device.handler.ItracDeviceHandlerForGPSWatch;
import com.eiw.device.handler.IvetelDeviceHandler;
import com.eiw.device.handler.MeiTrackDeviceHandler;
import com.eiw.device.handler.Prime07DeviceHandler;
import com.eiw.device.handler.RuptelaDeviceHandler;
import com.eiw.device.handler.SolarDeviceHandler;
import com.eiw.device.handler.SolarTimeStampHandler;
import com.eiw.device.handler.SpectrumDeviceHandler;
import com.eiw.device.handler.TZoneDeviceHandlerForSchoolBus;
import com.eiw.device.handler.TeltonikaDeviceHandler;
import com.eiw.device.handler.TestDeviceHandler;
import com.eiw.device.handler.Tk103DeviceHandler;
import com.eiw.device.handler.UpsIvetelDeviceHandler;

public enum Devices {

	TELTONIKA(5419, TeltonikaDeviceHandler.class), RUPTELA(9010,
			RuptelaDeviceHandler.class), TZONE(5415,
			TZoneDeviceHandlerForSchoolBus.class), CONCOX(5421,
			ConcoxDeviceHandler.class), ITRAC_BASIC(5422,
			ItracDeviceHandler1.class), ITRAC_ADVANCED(5423,
			ItracDeviceHandler1.class), MEITRACK(5424,
			MeiTrackDeviceHandler.class), ITRAC_GPS_WATCH(8010,
			ItracDeviceHandlerForGPSWatch.class), ANDROID(5426,
			AndroidDeviceHandler.class), SPECTRUM(5427,
			SpectrumDeviceHandler.class), TESTDEVICE(5428,
			TestDeviceHandler.class), IVETEL(5429, IvetelDeviceHandler.class), UPS_IVETEL(
			5430, UpsIvetelDeviceHandler.class), DEEPSEA(5435,
			DeepSeaDeviceHandler.class), RUPTELA2(5436,
			RuptelaDeviceHandler.class), SOLAR(5437, SolarDeviceHandler.class), TIMESTAMP(
			5432, SolarTimeStampHandler.class), AIS140_V1(5439,
			AIS140DeviceHandlerV1.class), AIS140_V2(5440,
			AIS140DeviceHandlerV2.class), TK103(5442, Tk103DeviceHandler.class), Prime07(
			5443, Prime07DeviceHandler.class), AIS140_V3(5444,
			AIS140DeviceHandlerV3.class), CANTRACK(5445,
			CantrackDeviceHandler.class), APMKT_AIS1401A(5446,
			APMKT_AIS1401ADeviceHandler.class), DeepseaTest(5447,
			DeepSeaDeviceHandlerTest.class), DeepSeaV1(5448,
			DeepseaDeviceHandler_V1.class), DeepSeaV2(5449,
			DeepseaDeviceHandlerV2.class);

	private final int portNumber;
	private final Class deviceHandlerClass;

	Devices(int portNumber, Class deviceHandlerClass) {
		this.portNumber = portNumber;
		this.deviceHandlerClass = deviceHandlerClass;
	}

	public int getPortNumber() {
		return portNumber;
	}

	public Class getDeviceHandlerClass() {
		return deviceHandlerClass;
	}
}