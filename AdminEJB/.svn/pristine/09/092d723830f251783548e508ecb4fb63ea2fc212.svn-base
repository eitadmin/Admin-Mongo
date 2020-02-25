/*
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
package com.skt.alerts;

import java.util.List;

import org.jboss.logging.Logger;
import org.jsmpp.examples.StressClient;

import com.skt.client.dto.StudentData;

/**
 * @author uudashr
 * 
 */
public class SendSMPPMessage {
	// private String DEFAULT_PASSWORD = "welc0meR";
	// private String DEFAULT_SYSID = "eitworks";
	// private String DEFAULT_DESTADDR = "9043296957";
	// private String DEFAULT_SOURCEADDR = "EITALT";
	private Logger logger = Logger.getLogger("alerts");
	// private Integer DEFAULT_PORT = 6060;
	private Long DEFAULT_TRANSACTIONTIMER = 2000L;
	private Integer DEFAULT_BULK_SIZE = 2;
	private Integer DEFAULT_PROCESSOR_DEGREE = 3;
	private Integer DEFAULT_MAX_OUTSTANDING = 10;

	public SendSMPPMessage() {
		// TODO Auto-generated constructor stub
	}

	public void sendSMPPMessage(String host, Integer port, String systemId,
			String password, String sourceAddr, String destinationAddr,
			String message) {

		long transactionTimer;
		try {
			transactionTimer = Integer.parseInt(System.getProperty(
					"jsmpp.client.transactionTimer",
					DEFAULT_TRANSACTIONTIMER.toString()));
		} catch (NumberFormatException e) {
			transactionTimer = DEFAULT_TRANSACTIONTIMER;
		}

		int bulkSize;
		try {
			bulkSize = Integer.parseInt(System.getProperty(
					"jsmpp.client.bulkSize", DEFAULT_BULK_SIZE.toString()));
		} catch (NumberFormatException e) {
			bulkSize = DEFAULT_BULK_SIZE;
		}

		int processorDegree;
		try {
			processorDegree = Integer.parseInt(System.getProperty(
					"jsmpp.client.procDegree",
					DEFAULT_PROCESSOR_DEGREE.toString()));
		} catch (NumberFormatException e) {
			processorDegree = DEFAULT_PROCESSOR_DEGREE;
		}

		int maxOutstanding;
		try {
			maxOutstanding = Integer.parseInt(System.getProperty(
					"jsmpp.client.maxOutstanding",
					DEFAULT_MAX_OUTSTANDING.toString()));
		} catch (NumberFormatException e) {
			maxOutstanding = DEFAULT_MAX_OUTSTANDING;
		}

		// String log4jPath = System.getProperty("jsmpp.client.log4jPath",
		// DEFAULT_LOG4J_PATH);
		// PropertyConfigurator.configure(log4jPath);

		logger.debug("Target server {}:{}" + host + port);
		logger.debug("System ID: {}" + systemId);
		logger.debug("Password: {}" + password);
		logger.debug("Source address: {}" + sourceAddr);
		logger.debug("Destination address: {}" + destinationAddr);
		logger.debug("Transaction timer: {}" + transactionTimer);
		logger.debug("Bulk size: {}" + bulkSize);
		logger.debug("Max outstanding: {}" + maxOutstanding);
		logger.debug("Processor degree: {}" + processorDegree);

		StressClient stressClient = new StressClient(0, host, port, bulkSize,
				systemId, password, sourceAddr, destinationAddr,
				transactionTimer, processorDegree, maxOutstanding);
		stressClient.run();

	}

	public void sendSMPPMessage(String host, Integer port, String systemId,
			String password, String sourceAddr, List<StudentData> studentDatas) {

		int bulkSize;
		try {
			bulkSize = Integer.parseInt(System.getProperty(
					"jsmpp.client.bulkSize", DEFAULT_BULK_SIZE.toString()));
		} catch (NumberFormatException e) {
			bulkSize = DEFAULT_BULK_SIZE;
		}

		int maxOutstanding;
		try {
			maxOutstanding = Integer.parseInt(System.getProperty(
					"jsmpp.client.maxOutstanding",
					DEFAULT_MAX_OUTSTANDING.toString()));
		} catch (NumberFormatException e) {
			maxOutstanding = DEFAULT_MAX_OUTSTANDING;
		}

		// String log4jPath = System.getProperty("jsmpp.client.log4jPath",
		// DEFAULT_LOG4J_PATH);
		// PropertyConfigurator.configure(log4jPath);

		logger.info("Target server {}:{}" + host + port);
		logger.info("System ID: {}" + systemId);
		logger.info("Password: {}" + password);
		logger.info("Source address: {}" + sourceAddr);
		// logger.info("Destination address: {}"+ destinationAddr);
		logger.info("Bulk size: {}" + bulkSize);
		logger.info("Max outstanding: {}" + maxOutstanding);

		SktStressClient stressClient = new SktStressClient(0, host, port,
				bulkSize, systemId, password, sourceAddr, "", maxOutstanding,
				studentDatas);
		logger.info("stressClient " + stressClient);

		stressClient.sendMessageList();

	}
}