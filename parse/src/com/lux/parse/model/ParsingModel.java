package com.lux.parse.model;

public class ParsingModel {
	private String path;
	private String []fileNames;
	private String []from;
	private String []to;
	
	public ParsingModel(String path, String[] fileNames, String[] from, String[] to) {
		this.path = path;
		this.fileNames = fileNames;
		this.from = from;
		this.to = to;
	}
	
	public String getPath() {
		return path;
	}
	
	public void setPath(String path) {
		this.path = path;
	}
	
	public String[] getFileNames() {
		return fileNames;
	}
	
	public void setFileNames(String[] fileNames) {
		this.fileNames = fileNames;
	}
	
	public String[] getFrom() {
		return from;
	}
	
	public void setFrom(String[] from) {
		this.from = from;
	}
	
	public String[] getTo() {
		return to;
	}
	
	public void setTo(String[] to) {
		this.to = to;
	}
}
