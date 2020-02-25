package com.skt.alerts;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.jboss.logging.Logger;
import org.jsmpp.InvalidResponseException;
import org.jsmpp.PDUException;
import org.jsmpp.bean.Alphabet;
import org.jsmpp.bean.BindType;
import org.jsmpp.bean.ESMClass;
import org.jsmpp.bean.GeneralDataCoding;
import org.jsmpp.bean.MessageClass;
import org.jsmpp.bean.NumberingPlanIndicator;
import org.jsmpp.bean.RegisteredDelivery;
import org.jsmpp.bean.TypeOfNumber;
import org.jsmpp.extra.NegativeResponseException;
import org.jsmpp.extra.ResponseTimeoutException;
import org.jsmpp.extra.SessionState;
import org.jsmpp.session.SMPPSession;

import com.skt.client.dto.StudentData;

public class SktStressClient {
	private static final String DEFAULT_PASSWORD = "welc0meR";
	private static final String DEFAULT_SYSID = "eitworks";
	private static final String DEFAULT_DESTADDR = "9043296957";
	private static final String DEFAULT_SOURCEADDR = "EITALT";
	private static final Logger logger = Logger.getLogger("alerts");
	private static final String DEFAULT_LOG4J_PATH = "stress/client-log4j.properties";
	private static final String DEFAULT_HOST = "210.16.103.238";
	private static final Integer DEFAULT_PORT = 6060;
	private static final Long DEFAULT_TRANSACTIONTIMER = 10000L;
	private static final Integer DEFAULT_BULK_SIZE = 2;
	private static final Integer DEFAULT_PROCESSOR_DEGREE = 3;
	private static final Integer DEFAULT_MAX_OUTSTANDING = 10;

	private static SMPPSession smppSession = null;
	private AtomicInteger requestCounter = new AtomicInteger();
	private AtomicInteger totalRequestCounter = new AtomicInteger();
	private AtomicInteger responseCounter = new AtomicInteger();
	private AtomicInteger totalResponseCounter = new AtomicInteger();
	private AtomicLong maxDelay = new AtomicLong();
	private ExecutorService execService;
	private String host;
	private int port;
	private int bulkSize;
	private AtomicBoolean exit = new AtomicBoolean();
	private int id;
	private String systemId;
	private String password;
	private String sourceAddr;
	private String destinationAddr;
	private String[] phoneNumbers = { "9003401354", "9600864194", "9944486640",
			"9566113437", "9629910030", "9944864428", "9578542879",
			"9043296957", "9751735981", "9962546418", "9787952202",
			"9380565304", "9789864118", "9789041838", "9698761859",
			"9789957188", "9962969412", "9750029793", "9787417516",
			"9941722357", "9840617297", "9944165370", "9790954662",
			"9940247343", "9941629941", "9841414589", "9840692002",
			"9994809670", "9940198255" };
	List<StudentData> studentDatas;

	public SktStressClient(int id, String host, int port, int bulkSize,
			String systemId, String password, String sourceAddr,
			String destinationAddr, int maxOutstanding,
			List<StudentData> studentDatas) {
		this.id = id;
		this.host = host;
		this.port = port;
		this.bulkSize = bulkSize;
		this.systemId = systemId;
		this.password = password;
		this.sourceAddr = sourceAddr;
		this.destinationAddr = destinationAddr;

		execService = Executors.newFixedThreadPool(maxOutstanding);
		this.studentDatas = studentDatas;
	}

	public static SMPPSession getSession() {
		if (smppSession == null) {
			smppSession = new SMPPSession();

			long transactionTimer;
			try {
				transactionTimer = Integer.parseInt(System.getProperty(
						"jsmpp.client.transactionTimer",
						DEFAULT_TRANSACTIONTIMER.toString()));
			} catch (NumberFormatException e) {
				transactionTimer = DEFAULT_TRANSACTIONTIMER;
			}
			int processorDegree;
			try {
				processorDegree = Integer.parseInt(System.getProperty(
						"jsmpp.client.procDegree",
						DEFAULT_PROCESSOR_DEGREE.toString()));
			} catch (NumberFormatException e) {
				processorDegree = DEFAULT_PROCESSOR_DEGREE;
			}
			smppSession.setPduProcessorDegree(processorDegree);
			smppSession.setTransactionTimer(transactionTimer);
		}
		// connectAndBind();
		return smppSession;
	}

	public static void connectAndBind() {
		SessionState sessionState = getSession().getSessionState();
		logger.info("Is Bound to1 " + sessionState.isBound());
		logger.info("Bound to " + sessionState.name());

		if (!getSession().getSessionState().isBound()) {
			try {
				smppSession = null;
				getSession().connectAndBind(DEFAULT_HOST, DEFAULT_PORT,
						BindType.BIND_TX, DEFAULT_SYSID, DEFAULT_PASSWORD,
						"cln", TypeOfNumber.UNKNOWN,
						NumberingPlanIndicator.UNKNOWN, null);
				logger.info("Is Bound to2 "
						+ getSession().getSessionState().isBound());

			} catch (IOException e) {
				logger.info("SktStressClient connectAndBind Error= "
						+ e.getMessage());
				e.printStackTrace();
			}
		}

	}

	public void sendMessageList() {
		connectAndBind();
		try {

			logger.debug("Starting send " + bulkSize + " bulk message...");

			for (int i = 0; i < studentDatas.size() && !exit.get(); i++) {
				StudentData studentData = studentDatas.get(i);
				newSendTask(studentData.getAddress(),
						studentData.getContactNo());
			}

		} catch (Exception e) {
			logger.error("Failed initialize connection or bind", e);
			return;
		} finally {

			logger.info("unbindAndClose Before");
			// getSession().unbindAndClose();
			logger.info("unbindAndClose Done");

		}

	}

	private void newSendTask(String message, String destinationAddr) {
		try {
			requestCounter.incrementAndGet();
			long startTime = System.currentTimeMillis();
			String[] msg = message.split("#");
			if (msg[1].equalsIgnoreCase("EN")) {
				getSession().submitShortMessage(null, TypeOfNumber.UNKNOWN,
						NumberingPlanIndicator.UNKNOWN, sourceAddr,
						TypeOfNumber.UNKNOWN, NumberingPlanIndicator.UNKNOWN,
						destinationAddr, new ESMClass(), (byte) 0, (byte) 0,
						null, null, new RegisteredDelivery(0), (byte) 0,
						new GeneralDataCoding(), (byte) 0, msg[0].getBytes());
			} else {
				getSession().submitShortMessage(
						null,
						TypeOfNumber.UNKNOWN,
						NumberingPlanIndicator.UNKNOWN,
						sourceAddr,
						TypeOfNumber.UNKNOWN,
						NumberingPlanIndicator.UNKNOWN,
						destinationAddr,
						new ESMClass(),
						(byte) 0,
						(byte) 0,
						null,
						null,
						new RegisteredDelivery(0),
						(byte) 0,
						new GeneralDataCoding(false, true, MessageClass.CLASS1,
								Alphabet.ALPHA_UCS2), (byte) 0,
						msg[0].getBytes("UTF-16BE"));
			}
			long delay = System.currentTimeMillis() - startTime;
			responseCounter.incrementAndGet();
			if (maxDelay.get() < delay) {
				maxDelay.set(delay);
			}
		} catch (PDUException e) {
			logger.error("Failed submit short message '" + message + "'", e);
		} catch (ResponseTimeoutException e) {
			logger.error("Failed submit short message '" + message + "'", e);
		} catch (InvalidResponseException e) {
			logger.error("Failed submit short message '" + message + "'", e);
		} catch (NegativeResponseException e) {
			logger.error("Failed submit short message '" + message + "'", e);
		} catch (IOException e) {
			logger.error("Failed submit short message '" + message + "'", e);
		}
	}
}
