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
			checkForUser("admin", "Administrator User","sadmin");

			// Remove for production use.
			checkForUser("admin2", "Luis Garcia", "user");
			checkForUser("admon", "Luis Garcia", "user");
			checkForUser("luis", "Luis Garcia", "user");
			checkForUser("test", "Test User", "user");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	private void checkForUser(String userName, String nameOfUser, String userRole) throws Exception {
		logger.info("App_StartUp: Looking for user: " + userName + ".");

		String qString = "SELECT u FROM AppUser u WHERE u.username=:username";
		TypedQuery<AppUser> tQuery = em.createQuery(qString, AppUser.class);
		tQuery.setParameter("username", userName);
		List<AppUser> results = tQuery.getResultList();

		if (results.isEmpty()) {
			logger.info("App_StartUp: Injecting user '" + userName + "' into database.");
			injectUser(userName, nameOfUser, userRole);
		} else
			logger.info("App_StartUp: User named '" + userName + "' was found.");
	}

	private void injectUser(String userName, String nameOfUser, String userRole) throws Exception {
		if (null != userName) {
			AppUser user = new AppUser();

			user.setUsername(userName);
			user.setName(nameOfUser);
			user.setPassword(HashUtil.calc_HashSHA("123456"));
			user.setMustChangePassword(Boolean.FALSE);
			user.setAppRole(appName, userRole);

			BookKeeper.create(user, "0 - System");
			em.persist(user);
			em.flush();
		}
	}
}
