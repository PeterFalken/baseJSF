package com.bitjester.apps.common;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

@MappedSuperclass
public abstract class BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected Long id;
	@Version
	protected Long version;
	@Column(name = "create_User")
	protected String createUser;
	@Column(name = "create_Time")
	protected Date createTime;
	@Column(name = "update_User")
	protected String updateUser;
	@Column(name = "update_Time")
	protected Date updateTime;
	
	// Check if the Object has been persisted.
	public boolean isNew() {
		if (null == id)
			return true;
		return false;
	}

	// --- Overrides
	// Check this video: https://www.youtube.com/watch?v=E-LG5DlOKBw
	// Check & Override Hash method.
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		if (this == obj)
			return true;
		final BaseEntity other = (BaseEntity) obj;
		if ((null != id) && (null == other.getId()))
			return false;
		if ((null == id) && (null != other.getId()))
			return false;
		if (id.longValue() != other.getId().longValue())
			return false;
		return true;
	}

	// --- Getters & Setters
	// --- Accessor methods

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

}
