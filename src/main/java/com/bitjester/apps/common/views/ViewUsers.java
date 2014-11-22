package com.bitjester.apps.common.views;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import com.bitjester.apps.common.entities.AppUser;
import com.bitjester.apps.common.utils.BookKeeper;
import com.bitjester.apps.common.utils.FacesUtil;
import com.bitjester.apps.common.utils.HashUtil;

@Named
@ViewScoped
public class ViewUsers implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject
	private String appName;

	@Inject
	private BookKeeper bk;

	@Inject
	private EntityManager em;

	private AppUser managedUser;

	// ================================
	// ======= Users Methods ==========
	// ================================

	@Named
	@Produces
	@RequestScoped
	public List<String> getUserStartLetters() throws Exception {
		String query = "SELECT DISTINCT SUBSTRING(username,1,1) AS letter FROM AppUser";
		query += " ORDER BY letter";
		return em.createQuery(query, String.class).getResultList();
	}

	@RequestScoped
	public List<AppUser> usersForLetter(String letter) throws Exception {
		String query = "FROM AppUser WHERE username LIKE '" + letter + "%'";
		query += " ORDER BY username";
		return em.createQuery(query, AppUser.class).getResultList();
	}

	// -- Persistence & form methods
	@Named
	@Produces
	public AppUser getManagedUser() {
		return managedUser;
	}

	public void setManagedUser(AppUser managedUser) {
		this.managedUser = managedUser;
	}

	public void load(Long id) throws Exception {
		managedUser = em.find(AppUser.class, id);
		managedUser.setActiveRole(managedUser.getAppRole(appName));
	}

	public void newInstance() {
		managedUser = new AppUser();
	}

	public void refresh() throws Exception {
		managedUser = null;
	}

	public void remove(Long id) {
		try {
			bk.remove(em.find(AppUser.class, id));
		} catch (Exception e) {
			FacesUtil.addMessage("Error ocurred, please reload page and try again.");
		}
	}

	public void store() {
		try {
			if (null == managedUser.getId()) {
				// New user will be assigned a 'user' role and default password.
				managedUser.setAppRole(appName, "user");
				managedUser.setPassword(HashUtil.calc_HashSHA("123456"));
			} else {
				// For existing users we update their role if necessary.
				managedUser.setAppRole(appName, managedUser.getActiveRole());
			}
			bk.store(managedUser);
			managedUser = null;
		} catch (Exception e) {
			FacesUtil.addMessage("Error ocurred, please reload page and try again.");
		}
	}
}
