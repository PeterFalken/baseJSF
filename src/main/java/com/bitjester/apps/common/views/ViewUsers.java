package com.bitjester.apps.common.views;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.List;

import com.bitjester.apps.common.BaseView;
import com.bitjester.apps.common.entities.AppUser;
import com.bitjester.apps.common.utils.FacesUtil;
import com.bitjester.apps.common.utils.HashUtil;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@ViewScoped
public class ViewUsers extends BaseView {
    @Inject
    private String appName;
    private AppUser managedUser;
    private Map<String, String> roles;

    // ======= Init Method ======================

    @PostConstruct
    public void init() {
        roles = new LinkedHashMap<>();
        roles.put("Admin", "admin");
        roles.put("System Admin", "sadmin");
        roles.put("User", "user");
    }

    // ======= User List Methods ================
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

    // ======= Property methods =======
    @Named
    @Produces
    public AppUser getManagedUser() {
        return managedUser;
    }

    public void setManagedUser(AppUser managedUser) {
        this.managedUser = managedUser;
    }

    public Map<String, String> getRoles() {
        return roles;
    }

    // ======= Persistence methods ==============
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
            log.severe(e.getMessage());
            FacesUtil.addMessage("An error occurred, please try again.");
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
            log.severe(e.getMessage());
            FacesUtil.addMessage("An error occurred, please try again.");
        }
    }
}
