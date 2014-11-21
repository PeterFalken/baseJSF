package com.bitjester.apps.common.entities;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.bitjester.apps.common.BaseEntity;

@Cacheable
@Entity
@Table(name = "app_roles", uniqueConstraints = @UniqueConstraint(columnNames = { "system_user", "application" }), indexes = {
		@Index(columnList = "system_user"), @Index(columnList = "application") })
public class AppRole extends BaseEntity {
	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name = "system_user")
	private AppUser system_user;
	@Column(nullable = false, length = 20)
	private String application;
	@Column(nullable = false, length = 20)
	private String role;

	// --- Getters & Setters

	public AppUser getSystem_user() {
		return system_user;
	}

	public void setSystem_user(AppUser system_user) {
		this.system_user = system_user;
	}

	public String getApplication() {
		return application;
	}

	public void setApplication(String application) {
		this.application = application;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
}
