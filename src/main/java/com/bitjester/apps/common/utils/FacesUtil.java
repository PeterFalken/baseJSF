package com.bitjester.apps.common.utils;

import java.io.IOException;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;

public abstract class FacesUtil {
	// Misc. methods
	public static void addMessage(String message) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(message));
	}
	
	private static ExternalContext getEC() {
		return FacesContext.getCurrentInstance().getExternalContext();
	}
	
	public static Flash getFlash(){
		return getEC().getFlash();
	}

	public static void invalidateSession() {
		getEC().invalidateSession();
	}
	
	// Navigation methods
	private static String getCPath() {
		// Returns Context's ROOT URL.
		return getEC().getRequestContextPath() + "/";
	}

	public static void navTo(String url) {
		try {
			getEC().redirect(getCPath() + url);
		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	public static void navToHome() {
		navTo("");
	}
}
