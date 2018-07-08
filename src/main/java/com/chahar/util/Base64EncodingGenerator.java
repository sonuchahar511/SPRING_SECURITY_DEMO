package com.chahar.util;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

public class Base64EncodingGenerator {
	
	public static void main(String[] args) throws UnsupportedEncodingException {
		String username="yash";
		String password="123456";
		
		String credential = username + ":" + password;
		String base64EncodedString = Base64.getEncoder().encodeToString(credential.getBytes("utf-8"));
		System.out.println("Encoded credential="+base64EncodedString);
		
		
		byte[] base64EncodedBytes = Base64.getDecoder().decode(base64EncodedString);
		String base64DecodedString = new String(base64EncodedBytes,"utf-8");
		System.out.println("Decoded credential="+base64DecodedString);
		
	}
	
}
