package com.bitjester.apps.common.utils;

import org.apache.commons.codec.digest.DigestUtils;

public abstract class HashUtil {
	/*
	public static String calc_HashMD5(String input) throws Exception {
		if (null == input)
			return null;
		// Salt for Hash is the same input string
		return DigestUtils.md5Hex(input + input);
	}
	*/

	public static String calc_HashSHA(String input) throws Exception {
		if (null == input)
			return null;
		// Salt for Hash is the same input string
		return DigestUtils.sha256Hex(input + input);
	}
}
