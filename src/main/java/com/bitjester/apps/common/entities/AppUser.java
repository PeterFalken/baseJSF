package com.bitjester.apps.common.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import com.bitjester.apps.common.BaseEntity;
import com.bitjester.apps.common.utils.BookKeeper;
import com.bitjester.apps.common.utils.CodeUtil;

@Cacheable
@Entity
@Table(name = "app_users", uniqueConstraints = @UniqueConstraint(columnNames = { "username" }), indexes = {
		@Index(columnList = "active"), @Index(columnList = "username"), @Index(columnList = "name") })
public class AppUser extends BaseEntity {
	private static final long serialVersionUID = 1L;

	private Boolean active;
	private Boolean mustChangePassword;
	private int attempts;
	private Date lastLogin;
	private Date lastLogout;
	private String name;
	@Column(length = 100)
	private String username;
	private String password;

	@Transient
	private String activeRole;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "system_user", orphanRemoval = true, fetch = FetchType.EAGER)
	private List<AppRole> roles;

	// Constructor
	public AppUser() {
		super();
		active = Boolean.TRUE;
		mustChangePassword = Boolean.TRUE;
		roles = new ArrayList<>(0);
	}

	// Role methods
	public String getAppRole(String app) {
		String returnRole = null;
		for(AppRole role : roles){
			if (role.getApplication().equals(app))
				returnRole = role.getRole();
		}
		return returnRole;
	}

	public void setAppRole(String application, String newRole) {
		if (null == getAppRole(application)) {
			AppRole role = new AppRole();

			role.setApplication(application);
			role.setRole(newRole);
			role.setSystem_user(this);

			BookKeeper.create(role, "0 - System");
			roles.add(role);
		} else {
			for(AppRole role : roles){
				if (role.getApplication().equals(application)) {
					BookKeeper.update(role, "0 - System");
					role.setRole(newRole);
				}
			}
		}
	}

	// --- Getters & Setters

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Boolean getMustChangePassword() {
		return mustChangePassword;
	}

	public void setMustChangePassword(Boolean mustChangePassword) {
		this.mustChangePassword = mustChangePassword;
	}
	
	public int getAttempts() {
		return attempts;
	}

	public void setAttempts(int attempts) {
		this.attempts = attempts;
	}

	public Date getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}

	public Date getLastLogout() {
		return lastLogout;
	}

	public void setLastLogout(Date lastLogout) {
		this.lastLogout = lastLogout;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getActiveRole() {
		return activeRole;
	}

	public void setActiveRole(String activeRole) {
		this.activeRole = activeRole;
	}

	public List<AppRole> getRoles() {
		return roles;
	}

	public void setRoles(List<AppRole> roles) {
		this.roles = roles;
	}
}
