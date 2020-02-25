package com.eiw.cron.archival;

import javax.ejb.Local;

@Local
public interface ArchivalVehicleSummaryEJBRemote {

    int numberOfRecords(String strRegion);
    
    void startVehicleSummaryArchive(String timeZone);

}
