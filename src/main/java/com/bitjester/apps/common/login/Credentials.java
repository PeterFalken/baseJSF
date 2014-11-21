package com.bitjester.apps.common.login;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@Named
@RequestScoped
public class Credentials {

	private String username;
	private String password;
	// Used when the user want to change the password for the account.
	private String newPass1;
	private String newPass2;

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

	public String getNewPass1() {
		return newPass1;
	}

	public void setNewPass1(String newPass1) {
		this.newPass1 = newPass1;
	}

	public String getNewPass2() {
		return newPass2;
	}

	public void setNewPass2(String newPass2) {
		this.newPass2 = newPass2;
	}
}