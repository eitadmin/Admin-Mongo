package com.eiw.cron.archival;

import java.util.List;

import javax.ejb.Local;

@Local
public interface ArchivalEJBRemote {

	void startArchiveProcess(String timeZone);

	long numberOfRecords(String timeZone);

	List<String> getVins();

	// List<Datarestorestatus> getRestoreData();
	//
	// void deleteRestoreArchivedData(Datarestorestatus data);
	//
	// List<Datarestorestatus> numberOfRestoreRecords();
	//
	// int countRestoreRecords(Datarestorestatus data);
	//
	// void reInsertInVE(Datarestorestatus data);

	int numberOfOldArchivedRecords(String timeZone);

	void deleteOldArchivedRecords(String timeZone);

	String getAllRamCoBackTrackingDetails(String timeZone);

	String getAllRamCoBackTrackingDetailsNew(String timeZone);

}
