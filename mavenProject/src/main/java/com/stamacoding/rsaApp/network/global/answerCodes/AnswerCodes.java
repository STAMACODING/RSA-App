package com.stamacoding.rsaApp.network.global.answerCodes;

public class AnswerCodes {
	public static class SignUp{
		public final static int SIGNED_UP = 0;
		public final static int USERNAME_UNAVAILABLE = -1;
		public final static int INVALID_DATA_FROM_CLIENT = -2;
		public final static int FAILED_TO_STORE = -3;
	}
	public static class LogIn{
		public static final int LOGGED_IN = 0;
		public final static int WRONG_USERNAME_PASSWORD = -1;
		public final static int INVALID_DATA_FROM_CLIENT = -2;
		public static final int ALREADY_LOGGED_IN = -3;
	}
	
	public static class SendMessageToServer{
		public final static int RECEIVED_VALID_MESSAGE = 0;
		public final static int RECEIVED_INVALID_MESSAGE = -1;
		public final static int RECEIVED_INVALID_DATA = -2;
	}
	public static class Ping{
		public final static int STILL_LOGGED_IN = 0;
		public final static int LOGGED_OUT = -1;
	}
}
