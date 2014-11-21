package com.bitjester.apps.common.views;

import java.util.List;

import com.bitjester.apps.common.BaseView;
import com.bitjester.apps.common.entities.AppUser;

public class TestNewView extends BaseView {
	private static final long serialVersionUID = 1L;

	public void callLog(String message){
		log.info(message);
	}
	
	public List<AppUser> getUsers(){
		em.createQuery("", AppUser.class);
		return null;
	}

}
