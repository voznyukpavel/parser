package com.lux.parse.manager;

import java.io.File;
import java.io.IOException;

import org.json.simple.parser.ParseException;

import com.lux.parse.exceptions.FromToParseException;
import com.lux.parse.model.ParsingModel;
import com.lux.parse.util.FileManager;

public class DataManager {
    private ParsingModel parsingModel;

    public void getParsingModel(String path, String fileNames, String from, String to)
            throws FromToParseException, IOException {
        ParsingModel model = new ParsingModel(path, fileNames, from, to);
        AdressesGetter adresses = new AdressesGetter();
        ParseProcess parsing = new ParseProcess(adresses.getAdresses(model.getPath(), model.getFileNames()),
                adresses.getDirectories());
        parsing.start(model.getFrom(), model.getTo());
    }

    public void saveToFile(File file, String path, String fileNames, String from, String to) throws IOException {
        FileManager.saveToFile(file, new ParsingModel(path, fileNames, from, to));
    }

    public void loadFromFile(File file) throws IOException, ParseException {
        parsingModel = FileManager.loadFromFile(file);
    }

    public String getPath() {
        return parsingModel.getPath();
    }

    public String getFileNames() {
        return parsingModel.getFileNames();
    }

    public String getFrom() {
        return parsingModel.getFrom();
    }

    public String getTo() {
        return parsingModel.getTo();
    }
}
