package com.lux.parse.util;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.lux.parse.manager.DataManager;

/**
 * Contents methods to write and read json files
 *
 */
public class FileManager {
	/**
	 * Writes data from app window to file
	 * 
	 * @param file  data must be written to this file (json)
	 * @param model data which must be written to file (json data model)
	 * @throws IOException input output exception which can occur during writing to
	 *                     file
	 */
	@SuppressWarnings("unchecked")
	public static void saveToFile(File file, DataManager model) throws IOException {
		JSONObject obj = new JSONObject();
		obj.put(ParserConstants.PATH, model.getPath());
		obj.put(ParserConstants.SKIP, model.getSkip());
		obj.put(ParserConstants.FILES, model.getFileNames());
		obj.put(ParserConstants.FROM, model.getFrom());
		obj.put(ParserConstants.TO, model.getTo());
		FileWriter writer = new FileWriter(file);
		writer.write(obj.toJSONString());
		writer.flush();
		writer.close();
	}

	/**
	 * Reads data from file to app window
	 * 
	 * @param file - data target file
	 * @return parsed json model data
	 * @throws IOException    input output exception which can occur during reading
	 *                        from file
	 * @throws ParseException can occur during parsing json data file
	 */
	public static DataManager loadFromFile(File file) throws IOException, ParseException {
		JSONParser jsonParser = new JSONParser();
		FileReader reader = new FileReader(file);
		Object obj = jsonParser.parse(reader);
		JSONObject jsonObj = (JSONObject) obj;
		DataManager model = new DataManager((String) jsonObj.get(ParserConstants.PATH),
				(String) jsonObj.get(ParserConstants.SKIP), (String) jsonObj.get(ParserConstants.FILES),
				(String) jsonObj.get(ParserConstants.FROM), (String) jsonObj.get(ParserConstants.TO));
		reader.close();
		return model;
	}

}
