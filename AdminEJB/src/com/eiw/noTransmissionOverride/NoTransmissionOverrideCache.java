package com.eiw.noTransmissionOverride;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Singleton
@Startup
public class NoTransmissionOverrideCache {
	@PersistenceContext(unitName = "ltmsfleettrackingpu")
	private EntityManager em;

	@EJB
	private DeviceStarter deviceStarter;

	@PostConstruct
	public void loadData() {
		Query query = em
				.createNativeQuery("select * from notransmissionoverride");
		List<Object[]> vehicleList = (List<Object[]>) query.getResultList();
		for (int i = 0; i < vehicleList.size(); i++) {
			Object[] row = (Object[]) vehicleList.get(i);
			String imeiNo = row[0].toString();
			String nearByVins = row[3].toString();
			deviceStarter.StartDevice(imeiNo, nearByVins);
			System.out.println(nearByVins);
		}
	}
}
