package com.lux.parse.util;

/**
 * Contents constants - keywords
 *
 */
public class ParserExpressionConstants {
    /**
     * Switch to the next file to replace data must be present in the same order and count in "From" , "To" windows
     */
    public static final String FILE_SEPARATOR = "<<<NEXT_FILE>>>";
    /**
     * Switch to the next string (strings) to replace must be present in the same order and count in "From" , "To"
     * windows
     */
    public static final String EXPRESSION_SEPARATOR = "<<<NEXT>>>";
    /**
     * Can be using in "Files list:", "From" , "To" windows, will be replaced by repo name
     */
    public static final String NAME = "\"name\"";
}
