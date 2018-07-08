package com.chahar.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BcryptPasswordEncodeGenerator {

	public static void main(String[] args) {
		String password = "123456";
		
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
		
		for(int i=0;i<5;i++){
			//every time BCryptPasswordEncoder.encode(String) generate different string.
			String hashedPassword = passwordEncoder.encode(password);
			System.out.print(hashedPassword +",length="+hashedPassword.length());
			
			System.out.println(" >> " + (passwordEncoder.matches(password,hashedPassword) ? "Matched" : "Not Matched") );
		}
		
		System.out.println("1E2F8883A83D4120DB7862C97AC44EED".length());
		System.out.println("eWFzaDoxNDczODMzMDQzMTcyOmJlZTBkNjAxNjJmNzJmN2U3ZGE2NjgxZjJmNmJjNzU5".length());
		System.out.println("eWFzaDoxNDcyNjU0OTU1NjUxOmJlNTU0MjRlZDVjOTA0YmIwYTg1ODJhMGExMjA1ZTc4".length());
	}

}
