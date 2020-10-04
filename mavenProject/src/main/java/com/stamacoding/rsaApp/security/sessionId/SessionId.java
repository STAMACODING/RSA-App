package com.stamacoding.rsaApp.security.sessionId;

import java.util.UUID;

import com.stamacoding.rsaApp.logger.L;

public class SessionId {
     
     public static String generateSessionId() {
    	 L.t("SESSION", SessionId.class, "Generating session id...");
         UUID sessionId = UUID.randomUUID();
         L.t("SESSION", SessionId.class, "Generated session id: " + sessionId.toString());
         return sessionId.toString();
     }
     
    public static void main(String[] args) {
		System.out.println(generateSessionId());
	}

 }