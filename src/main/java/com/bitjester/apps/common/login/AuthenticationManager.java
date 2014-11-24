package com.bitjester.apps.common.login;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import com.bitjester.apps.common.entities.AppUser;
import com.bitjester.apps.common.utils.BookKeeper;
import com.bitjester.apps.common.utils.HashUtil;

@Named
@Stateless
public class AuthenticationManager implements Serializable {
    private static final long serialVersionUID = 1L;

    @Inject
    EntityManager em;

    @Inject
    Integer login_limit;

    public void changePassword(AppUser user, String newPassword) throws Exception {
        user = em.merge(user);
        user.setPassword(HashUtil.calc_HashSHA(newPassword));
        if (user.getMustChangePassword())
            user.setMustChangePassword(Boolean.FALSE);
        logOutUser(user);
    }

    public AppUser checkCredentials(String user, String password) throws Exception {
        Boolean failedAttempt = Boolean.FALSE;
        String query = "SELECT u FROM AppUser u WHERE u.active=TRUE AND u.username=:user_name";
        TypedQuery<AppUser> tQuery = em.createQuery(query, AppUser.class);
        tQuery.setParameter("user_name", user.trim());
        List<AppUser> results = tQuery.getResultList();

        if (results.isEmpty()) {
            // User not found.
            return null;
        } else {
            // User found - we need to check if password matches.
            AppUser app_user = results.get(0);
            BookKeeper.update(app_user, "0 - System");

            if (app_user.getPassword().equals(HashUtil.calc_HashSHA(password.trim()))) {
                // Password match.
                app_user.setLastLogin(new Date(System.currentTimeMillis()));
                app_user.setAttempts(0);
            } else {
                // Password doesn't match - we log a new login attempt.
                failedAttempt = Boolean.TRUE;
                app_user.setAttempts(app_user.getAttempts() + 1);
                if (login_limit.intValue() <= app_user.getAttempts())
                    // After too many failed attempts user is deactivated.
                    app_user.setActive(Boolean.FALSE);
            }
            em.flush();
            if(failedAttempt)
                // Failed authentication
                return null;
            // Successful authentication
            return app_user;
        }
    }

    public AppUser getUserForImpersonation(Long id) throws Exception {
        return em.find(AppUser.class, id);
    }

    public void logOutUser(AppUser user) throws Exception {
        user = em.find(AppUser.class, user.getId());
        BookKeeper.update(user, "0 - System");
        user.setLastLogout(new Date(System.currentTimeMillis()));
        em.flush();
    }

    public void resetPassword(Long userID) throws Exception {
        AppUser user = em.find(AppUser.class, userID);
        BookKeeper.update(user, "0 - System");
        user.setPassword(HashUtil.calc_HashSHA("123456"));
        user.setActive(Boolean.TRUE);
        user.setMustChangePassword(Boolean.TRUE);
        em.merge(user);
        em.flush();
    }
}
