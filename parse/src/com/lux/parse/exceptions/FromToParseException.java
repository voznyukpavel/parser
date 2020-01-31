package com.lux.parse.exceptions;

public class FromToParseException extends Exception {

    private static final long serialVersionUID = -1071988522789786500L;

    public FromToParseException(String msg) {
        super(msg);
    }
    public FromToParseException() {
        super("parsing error");
    }
}
