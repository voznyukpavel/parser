package com.lux.parse.model;

public class ParsingModel {
    private String path="";
    private String skip="";
    private String fileNames="";
    private String from="";
    private String to="";

    public ParsingModel(String path,String skip,String fileNames, String from, String to) {
        this.skip = skip;
        this.path = path;
        this.fileNames = fileNames;
        this.from = from;
        this.to = to;
    }

    public String getPath() {
        return path;
    }
    
    public String getSkip() {
        return skip;
    }

    public String getFileNames() {
        return fileNames;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

}
