package com.lux.parse.exceptions;

/**
 * Exception which appears if count or order of tokens
 * "&#60;&#60;&#60;NEXT_FILE&#62;&#62;&#62;" and
 * "&#60;&#60;&#60;NEXT&#62;&#62;&#62;" in "From" and "To" text fields does not
 * match
 *
 */
public class FromToParseException extends Exception {

	private static final long serialVersionUID = -1071988522789786500L;

	/**
	 * Gets error message
	 * 
	 * @param msg error message
	 *
	 */
	public FromToParseException(String msg) {
		super(msg);
	}

	/**
	 * Error message by default
	 */
	public FromToParseException() {
		super("Tokens order or count in \"From\" and \"To\" windows does not mutch");
	}
}
