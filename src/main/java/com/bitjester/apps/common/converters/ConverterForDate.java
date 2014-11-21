package com.bitjester.apps.common.converters;

import java.util.TimeZone;

import javax.enterprise.context.RequestScoped;
import javax.faces.convert.DateTimeConverter;
import javax.inject.Named;

@Named
@RequestScoped
public class ConverterForDate extends DateTimeConverter {
	public ConverterForDate() {
		setPattern("yyyy/MM/dd HH:mm");
		setTimeZone(TimeZone.getTimeZone("America/El_Salvador"));
	}
}
