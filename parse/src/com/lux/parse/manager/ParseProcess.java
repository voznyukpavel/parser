package com.lux.parse.manager;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import com.lux.parse.exceptions.FromToParseException;
import com.lux.parse.util.ParserExprassionConstants;

class ParseProcess {
    
    private static final String LINESEPARATOR = System.lineSeparator();
    private static final Charset CHARSET = StandardCharsets.UTF_8;
    private static final String NEXT_LINE = "(\r\n|\n|\r)";
    private static final String NEXT_LINE_LAST = "(\r\n|\n|\r)+$";

    private ArrayList<LinkedHashMap<Integer, File>> ways;
    private String[] directories;

    public ParseProcess(ArrayList<LinkedHashMap<Integer, File>> ways, String[] directories) {
        this.ways = ways;
        this.directories = directories;
    }

    public void start(String from, String to) throws FromToParseException, IOException {
        validationFromTo(from, to);
        from = deleteSpaces(from);
        to = deleteSpaces(to);
        String[][] splitedFrom = split(from);
        String[][] splitedTo = split(to);
        parse(splitedFrom, splitedTo);
    }

    private String deleteSpaces(String string) {
        String result = "";
        String stringArr[] = string.split(NEXT_LINE);
        for (int i = 0; i < stringArr.length; i++) {
            if (stringArr[i].contains(ParserExprassionConstants.FILE_SEPARATOR)
                    || stringArr[i].contains(ParserExprassionConstants.EXPRESSION_SEPARATOR)) {
                stringArr[i] = stringArr[i].strip();
                stringArr[i] = stringArr[i].replaceAll("\t", "");
            }
        }
        for (int i = 0; i < stringArr.length; i++) {
            result += stringArr[i] + LINESEPARATOR;
        }
        return result;
    }

    private String[][] split(String from) {
        String[][] result;
        String[] filesCont = from.split(ParserExprassionConstants.FILE_SEPARATOR);
        result = new String[filesCont.length][];
        for (int i = 0; i < filesCont.length; i++) {
            result[i] = filesCont[i].split(ParserExprassionConstants.EXPRESSION_SEPARATOR);
        }
        return result;
    }

    private void parse(String[][] from, String[][] to) throws IOException {
        int size = ways.size();
        for (int i = 0; i < size; i++) {
            LinkedHashMap<Integer, File> waysLocal = ways.get(i);
            int lsize = waysLocal.size();
            for (int j = 0; j < lsize; j++) {
                Path path = waysLocal.get(j).toPath();
                replace(path, from[j], to[j],directories[i]);
                System.out.println();
            }
        }
    }

    private void replace(Path path, String[] from, String[] to,String directories) throws IOException {
        String content = new String(Files.readAllBytes(path), CHARSET);
        for (int i = 0; i < from.length; i++) {
            from[i] = from[i].replaceAll(NEXT_LINE_LAST, "");
            from[i] = from[i].replaceAll(ParserExprassionConstants.NAME, directories);
            to[i] = to[i].replaceAll(ParserExprassionConstants.NAME, directories);
            if (identEndOfString(to[i])) {
                to[i] = to[i].replaceFirst(NEXT_LINE, "");
            }
            to[i] = to[i].replaceAll(NEXT_LINE_LAST, "");
            to[i] = to[i].strip();
            System.out.println();
            content = replace(from[i], to[i], content);
        }
        Files.write(path, content.getBytes(CHARSET));
    }

    private String replace(String from, String to, String content) {
        ArrayList<String> valideFrom = splitByNextString(from);
        ArrayList<String> valideTo = splitByNextString(to);
        int sizeFrom = valideFrom.size();
        int sizeTo = valideTo.size();
        if (sizeFrom > 1) {
            int counterMutches = 0;
            String contentArr[] = content.split(NEXT_LINE);
            for (int i = 0; i < contentArr.length; i++) {
                for (int j = 0; j < sizeFrom; j++) {
                    String tempTrimmed = contentArr[i].strip();
                    tempTrimmed = tempTrimmed.replaceAll("\t", "");
                    if (tempTrimmed.equals(valideFrom.get(j)) && counterMutches == j) {
                        counterMutches++;
                        i++;
                        if (counterMutches < sizeFrom) {
                            continue;
                        }
                    } else {
                        counterMutches = 0;
                    }
                    if (counterMutches == sizeFrom) {
                        int changeFrom = i - counterMutches;
                        for (int k = changeFrom, replace = 0; k < i; k++, replace++) {
                            contentArr[k] = contentArr[k].replace(valideFrom.get(replace), valideTo.get(replace));
                            System.out.println(contentArr[k]);
                        }
                        if (sizeFrom < sizeTo) {
                            i--;
                            for (int n = sizeFrom; n < sizeTo; n++) {
                                contentArr[i] += LINESEPARATOR + valideTo.get(n);
                            }
                        }
                    }
                    counterMutches = 0;
                }
            }
            content = getContent(contentArr);
        } else {
            from = from.replaceAll(NEXT_LINE, "");
            if (!from.isEmpty()) {
                content = content.replaceAll(from, to);
            }
        }
        return content;
    }

    private boolean identEndOfString(String string) {
        return string.startsWith(LINESEPARATOR) || string.startsWith("\r") || string.startsWith("\n");
    }

    private String getContent(String[] contentArr) {
        String content;
        content = "";
        for (int i = 0; i < contentArr.length; i++) {
            if (!contentArr[i].isEmpty()) {
                content += contentArr[i] + LINESEPARATOR;
            }
        }
        return content;
    }

    private ArrayList<String> splitByNextString(String string) {
        ArrayList<String> valideFrom = new ArrayList<String>();
        String stringArr[] = string.split(NEXT_LINE);
        for (int i = 0; i < stringArr.length; i++) {
            stringArr[i] = stringArr[i].replaceAll("\t", "");
            stringArr[i] = stringArr[i].strip();
            if (!stringArr[i].isEmpty()) {
                valideFrom.add(stringArr[i]);
            }
        }
        return valideFrom;
    }

    private void validationFromTo(String from, String to) throws FromToParseException {
        if (!getToken(from).equals(getToken(to))) {
            throw new FromToParseException();
        }
    }

    private String getToken(String check) {
        String result = "";
        while (true) {
            int next = check.indexOf(ParserExprassionConstants.EXPRESSION_SEPARATOR);
            int end = check.indexOf(ParserExprassionConstants.FILE_SEPARATOR);
            if (next == -1 && end == -1) {
                break;
            }
            if ((next < end || end == -1) && next != -1) {
                result += check.substring(next, next + ParserExprassionConstants.EXPRESSION_SEPARATOR.length());
                check = check.replaceFirst(ParserExprassionConstants.EXPRESSION_SEPARATOR, "");
            } else if (end != -1) {
                result += check.substring(end, end + ParserExprassionConstants.FILE_SEPARATOR.length());
                check = check.replaceFirst(ParserExprassionConstants.FILE_SEPARATOR, "");
            }
        }
        return result;
    }
}