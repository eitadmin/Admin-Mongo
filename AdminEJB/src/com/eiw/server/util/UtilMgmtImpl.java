package com.eiw.server.util;

import java.text.SimpleDateFormat;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class UtilMgmtImpl implements UtilMgmt {

	@PersistenceContext(unitName = "ltmsfleettrackingpu")
	private EntityManager em;

	SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public List<String> getAdminNotificationEmails() {
		try {
			Query qry = em.createQuery(
					"select g.id.paramValue from GeneralParam g where g.id.paramName = 'ADMIN_MAIL_NOTIFICATION'");
			return qry.getResultList();
		} catch (Exception e) {
			return null;
		}
	}
}
