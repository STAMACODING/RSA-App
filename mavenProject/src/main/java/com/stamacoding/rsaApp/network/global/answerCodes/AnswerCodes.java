package com.stamacoding.rsaApp.network.global.answerCodes;

public class AnswerCodes {
	public static class SignUp{
		public final static int SIGNED_UP = 0;
		public final static int USERNAME_UNAVAILABLE = -1;
		public final static int INVALID_DATA_FROM_CLIENT = -2;
	}
	public static class LogIn{
		public final static int WRONG_USERNAME_PASSWORD = -1;
		public final static int INVALID_DATA_FROM_CLIENT = -2;
	}
}
