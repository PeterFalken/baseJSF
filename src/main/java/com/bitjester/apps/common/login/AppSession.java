package com.bitjester.apps.common.login;

import java.io.Serializable;

import javax.annotation.PreDestroy;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import com.bitjester.apps.common.annotations.ActiveUser;
import com.bitjester.apps.common.annotations.SystemUser;
import com.bitjester.apps.common.entities.AppUser;
import com.bitjester.apps.common.utils.FacesUtil;
import com.bitjester.apps.common.utils.HashUtil;

@Named
@SessionScoped
public class AppSession implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject
	private String appName;

	@Inject
	private Credentials credentials;

	@Inject
	LoginManager lm;

	private AppUser activeUser;
	private AppUser systemUser;

	// -- System User and Active User.
	// -- Useful for Impersonation
	@SystemUser
	@Named
	@Produces
	public AppUser getSystemUser() {
		return systemUser;
	}

	@ActiveUser
	@Named
	@Produces
	public AppUser getActiveUser() {
		if (null == activeUser)
			return systemUser;
		return activeUser;
	}

	// Impersonate user identified by Long id - logs current user name.
	public void impersonate(Long id) throws Exception {
		if (systemUser.getId().equals(id))
			// A systemUser is always impersonating himself.
			return;
		// For every other user id, we set the activeUser.
		AppUser user = lm.getUserForImpersonation(id);
		if (null != user) {
			activeUser = user;
			activeUser.setActiveRole(activeUser.getAppRole(appName));
		}
	}

	@Named
	@Produces
	public boolean isLoggedIn() {
		return (null != systemUser) && !(systemUser.getMustChangePassword());
	}

	public void checkRole(String role) throws Exception {
		if (isLoggedIn()) {
			if (null != role && !systemUser.getActiveRole().contains(role))
				FacesUtil.navToHome();
		} else
			FacesUtil.navToHome();
	}

	// -- Login + Credentials methods
	public void checkCredentials() throws Exception {
		AppUser user = lm.checkCredentials(credentials.getUsername(), credentials.getPassword());
		if (user != null) {
			systemUser = user;
			// Check if user must change the password.
			if (systemUser.getMustChangePassword())
				FacesUtil.navTo("aforms/password.xhtml");
			// If user has already changed the password.
			systemUser.setActiveRole(systemUser.getAppRole(appName));
			logger.info("ActiveRole: " + systemUser.getActiveRole());
			FacesUtil.addMessage("Welcome, " + systemUser.getName());
		} else
			FacesUtil.addMessage("Wrong credentials.");
	}

	public void changePassword() throws Exception {
		// Verify if current password matches the one on the database
		if (!systemUser.getPassword().equals(HashUtil.calc_HashSHA(credentials.getPassword()))) {
			FacesUtil.addMessage("Current password is incorrect.");
			return;
		}

		// Verify if newPassword1 is the same as newPassword2
		if (!credentials.getNewPass1().equals(credentials.getNewPass2())) {
			FacesUtil.addMessage("New password fields must match.");
			return;
		}

		// Verify if new password is different from current password
		if (credentials.getPassword().equals(credentials.getNewPass1())) {
			FacesUtil.addMessage("New password must be different from old password.");
			return;
		}

		lm.changePassword(systemUser, credentials.getNewPass1());
		systemUser = null;
		// FacesUtil.invalidateSession();
		FacesUtil.navTo("error/pchanged.xhtml");
	}

	public void logout() throws Exception {
		// If impersonating - we just go back to our systemUser.
		if (null != activeUser) {
			activeUser = null;
			FacesUtil.navToHome();
			return;
		}

		// If not impersonating - we end the session.
		if (null != systemUser) {
			lm.logOutUser(systemUser);
			FacesUtil.addMessage("Good bye, " + systemUser.getName());
		}
		systemUser = null;
		activeUser = null;
		FacesUtil.invalidateSession();
		FacesUtil.navToHome();
	}

	@PreDestroy
	public void cleanUp() {
		try {
			if (null != systemUser)
				lm.logOutUser(systemUser);
			systemUser = null;
			activeUser = null;
			if (null != FacesContext.getCurrentInstance())
				FacesUtil.invalidateSession();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
