package com.fvthree.blogpost.util;

import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
public class PasswordUtil {

	public String hashPassword(String password) throws NoSuchAlgorithmException {
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		byte[] hashBytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));

		// Convert the byte array to a hexadecimal string representation
		StringBuilder hexString = new StringBuilder();
		for (byte hashByte : hashBytes) {
			String hex = Integer.toHexString(0xff & hashByte);
			if (hex.length() == 1) {
				hexString.append('0');
			}
			hexString.append(hex);
		}

		return hexString.toString();
	}

}
