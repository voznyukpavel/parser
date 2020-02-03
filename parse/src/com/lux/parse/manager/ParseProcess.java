package com.lux.parse.manager;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import com.lux.parse.exceptions.FromToParseException;
import com.lux.parse.model.ParsingModel;
import com.lux.parse.util.ParserExprassionConstants;

class ParseProcess {

	private static final String NAME = "\"name\"";
	private String[] directories;
	private String[] subDirectories;
	private ArrayList<LinkedHashMap<Integer, File>> ways;
	private final Charset charset = StandardCharsets.UTF_8;

	public void start(ParsingModel parsingModel) throws FromToParseException, IOException {
		String path = parsingModel.getPath();
		String from = parsingModel.getFrom();
		String to = parsingModel.getTo();
		getDirectories(path);
		getSubDirectories(parsingModel.getFileNames());
		prepareWays(path);
		validationFromTo(from, to);
		String[][] splitedFrom = splite(from);
		String[][] splitedTo = splite(to);
		parse(splitedFrom, splitedTo);
	}

	private String[][] splite(String from) {
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
				replace(path, from[j], to[j]);
			}
		}
	}

	private void replace(Path path, String[] from, String[] to) throws IOException {
		String content = new String(Files.readAllBytes(path), charset);
		for (int i = 0; i < from.length; i++) {
			from[i] = from[i].replaceAll("(\r\n|\n|\r)", "");
			//from[i] = from[i].replaceAll("(\r\n|\n|\r)+$", "");
			if (to[i].startsWith("\r\n") || to[i].startsWith("\r") || to[i].startsWith("\n")) {
				to[i] = to[i].replaceFirst("(\r\n|\n|\r)", "");
			}
			to[i] = to[i].replaceAll("(\r\n|\n|\r)+$", "");
			content = content.replaceAll(from[i], to[i]);
			System.out.println("");
		}
		Files.write(path, content.getBytes(charset));
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

	private void getDirectories(String path) {
		File file = new File(path);
		directories = file.list(new FilenameFilter() {
			@Override
			public boolean accept(File current, String name) {
				return new File(current, name).isDirectory();
			}
		});
	}

	private void getSubDirectories(String fileNames) {
		String lineSeparator = System.lineSeparator();
		subDirectories = fileNames.split(lineSeparator);
	}

	private void prepareWays(String path) {
		ways = new ArrayList<LinkedHashMap<Integer, File>>();
		String backSlash = "\\";
		if (!path.endsWith(backSlash)) {
			path = path + backSlash;
		}
		for (int i = 0; i < directories.length; i++) {
			LinkedHashMap<Integer, File> waysLocal = new LinkedHashMap<Integer, File>();
			for (int j = 0; j < subDirectories.length; j++) {
				String temp = subDirectories[j].replaceAll(NAME, directories[i]);
				File file = new File(path + directories[i] + backSlash + temp);
				if (file.exists()) {
					waysLocal.put(j, file);
				}
			}
			ways.add(waysLocal);
		}
	}
}
