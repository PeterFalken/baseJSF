package com.bitjester.apps.common;

import java.io.Serializable;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.bitjester.apps.common.utils.BookKeeper;

public abstract class BaseView implements Serializable {
	protected static final long serialVersionUID = 1L;
	@Inject
	protected BookKeeper bk;
	@Inject
	protected EntityManager em;
	@Inject
	protected Logger log;
	
}
