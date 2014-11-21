package com.bitjester.apps.common;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

@Stateless
public class DataManager implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager em;

	public int executeUpdate(String query, List<Object> params) throws Exception {
		if (null == query)
			throw new Exception("Method trying to execute update with null query string.");

		int i = 0;
		Query q = em.createQuery(query);
		// Process parameters if any.
		if (null != params) {
			Iterator<Object> ite = params.iterator();
			while (ite.hasNext()) {
				q.setParameter(i++, ite.next());
			}
		}
		return q.executeUpdate();
	}

	public BaseEntity store(BaseEntity entity) throws Exception {
		if (null == entity)
			throw new Exception("Method trying to store null on Persistence Context.");

		BaseEntity be = null;
		if (null == entity.getId()) {
			em.persist(entity);
		} else {
			be = em.merge(entity);
		}
		em.flush();
		return be;
	}

	public void remove(BaseEntity entity) throws Exception {
		if (null == entity)
			throw new Exception("Method trying to remove null from Persistence Context.");

		try {
			entity = em.find(entity.getClass(), entity.getId());
			em.remove(entity);
			em.flush();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
}
