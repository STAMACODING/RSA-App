package com.stamacoding.rsaApp.network.global;

/**
 * Contains some utility functions to write fancy log messages
 * and to interact with the user in a more user-friendly way!
 * <p>
 * Just test the functions to see what they do ;)
 * </p>
 */
public class TextUtils {
	public static final int DEFAULT_WIDTH = 80;
	
	public static String heading(String heading) {
		return heading(heading, '#', DEFAULT_WIDTH, true);
	}
	
	public static String heading(String heading, char c, int width, boolean fancy) {
		width -= 0;
		int lineLength = width / 2 - heading.length() / 2;
		int rest = width - 2 * lineLength - heading.length();
		
		if(fancy) {
			lineLength -= 4;
		}
		
		StringBuilder sb = new StringBuilder();
		if(fancy) sb.append("O");
		sb.append(computeLine(c, lineLength));
		if(fancy) sb.append(" [ ");
		sb.append(heading);
		if(fancy) sb.append(" ] ");
		sb.append(computeLine(c, lineLength + rest));
		if(fancy) sb.append("O");
		sb.append('\n');
		
		return sb.toString();
	}
	
	public static String computeLine() {
		return computeLine('-', DEFAULT_WIDTH);
	}
	
	public static String computeLine(char c, int width) {
		StringBuilder sb = new StringBuilder();
		for(int i=0; i<width; i++) sb.append(c);
		return sb.toString();
	}
	
	public static String center(String s, char c, int width) {
		int emptyLength = width / 2 - s.length() / 2;
		int rest = width - 2 * emptyLength - s.length();
		
		StringBuilder sb = new StringBuilder();
		for(int i=0; i<emptyLength; i++) sb.append(c);
		sb.append(s);
		for(int i=0; i<emptyLength + rest; i++) sb.append(c);
		
		return sb.toString();
	}
	
	public static String center(String s, int width) {
		return center(s, ' ', width);
	}
	
	public static String centerBox(String message) {
		return centerBox(message, '-', DEFAULT_WIDTH);
	}
	
	public static String centerBox(String message, char c, int width) {

		StringBuilder sb = new StringBuilder();
		sb.append('|');
		sb.append(center(message, c, width - 2));
		sb.append('|');
		sb.append('\n');
		return sb.toString();
	}
	
	public static String fancyParameter(String parameter, String value) {
		if(value == null) value = "null";
		return TextUtils.centerBox(String.format("{ %s } => { %s }", TextUtils.center(parameter, 25), TextUtils.center(value, 15)));
	}
	
	public static String box(String message) {
		return box(message, '#', '-', DEFAULT_WIDTH, 4);
	}
	
	public static String box(String message, char a, char c, int width, int padding) {
		StringBuilder sb = new StringBuilder();
		sb.append(a);
		for(int i=0; i<padding; i++) sb.append(c);
		sb.append(message);
		for(int i=0; i<(width - message.length() - 2*padding - 2); i++) sb.append(c);
		for(int i=0; i<padding; i++) sb.append(c);
		sb.append(a);
		sb.append('\n');
		return sb.toString();
	}
	
	public static String inputBefore(String message) {
		StringBuilder sb = new StringBuilder();
		sb.append(box("{ " + message + " }", '#', '-', DEFAULT_WIDTH, 4));
		sb.append(box("", 'O', ' ', DEFAULT_WIDTH, 0));
		sb.append("\tuser@input ~ $ ");
		return sb.toString();
	}
	
	public static String inputAfter() {
		StringBuilder sb = new StringBuilder();
		sb.append(box("", 'O', ' ', DEFAULT_WIDTH, 0));
		return sb.toString();
	}
}
