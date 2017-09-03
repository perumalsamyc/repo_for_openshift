package com.BjpTracking.Util;

import org.apache.commons.codec.binary.Base64;

public final class Util {

	public static String base64Conv(String str,int flag){
		
		// encode data on your side using BASE64
		
		String convStr= flag == 0 ?
				new String( Base64.encodeBase64(str.getBytes()))
				:
		// 	Decode data on other side, by processing encoded data
				new String(Base64.decodeBase64(str));

		return convStr; 
	}
	
	
	 
	
}
