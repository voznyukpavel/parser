package com.lux.parse.util;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.lux.parse.model.ParsingModel;

public class FileManager {

    @SuppressWarnings("unchecked")
    public static void saveToFile(File file, ParsingModel model) throws IOException {
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

    public static ParsingModel loadFromFile(File file) throws IOException, ParseException {
        JSONParser jsonParser = new JSONParser();
        FileReader reader = new FileReader(file);
        Object obj = jsonParser.parse(reader);
        JSONObject jsonObj = (JSONObject) obj;
        ParsingModel model = new ParsingModel((String) jsonObj.get(ParserConstants.PATH),(String) jsonObj.get(ParserConstants.SKIP),
                (String) jsonObj.get(ParserConstants.FILES), (String) jsonObj.get(ParserConstants.FROM),
                (String) jsonObj.get(ParserConstants.TO));
        reader.close();
        return model;
    }

}
