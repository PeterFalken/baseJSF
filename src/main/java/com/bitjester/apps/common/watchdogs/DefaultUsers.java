package com.bitjester.apps.common.watchdogs;

import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import com.bitjester.apps.common.entities.AppUser;
import com.bitjester.apps.common.utils.BookKeeper;
import com.bitjester.apps.common.utils.HashUtil;

@Singleton
@Startup
public class DefaultUsers {
	@Inject
	private String appName;

	@Inject
	private Logger logger;

	@Inject
	EntityManager em;

	@PostConstruct
	private void checkForUsers() {
		logger.info("Starting application: " + appName + ".");
		try {
			checkForUser("admin", "Administrator User");
			checkForUser("luis", "Luis Garcia");
			checkForUser("test", "Test User");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	private void checkForUser(String username, String nameOfUser) throws Exception {
		logger.info("App_StartUp: Looking for user: " + username + ".");

		String qString = "SELECT u FROM AppUser u WHERE u.username=:username";
		TypedQuery<AppUser> tQuery = em.createQuery(qString, AppUser.class);
		tQuery.setParameter("username", username);
		List<AppUser> results = tQuery.getResultList();

		if (results.isEmpty()) {
			logger.info("App_StartUp: Injecting user '" + username + "' into database.");
			injectUser(username, nameOfUser);
		} else
			logger.info("App_StartUp: User named '" + username + "' was found.");
	}

	private void injectUser(String username, String nameOfUser) throws Exception {
		if (null != username) {
			AppUser user = new AppUser();

			user.setUsername(username);
			user.setName(nameOfUser);
			user.setPassword(HashUtil.calc_HashSHA("123456"));
			user.setMustChangePassword(Boolean.FALSE);
			user.setAppRole(appName, "sadmin");

			BookKeeper.create(user, "0 - System");
			em.persist(user);
			em.flush();
		}
	}
}
