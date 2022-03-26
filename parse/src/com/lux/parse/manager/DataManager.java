package com.lux.parse.manager;

import java.io.File;
import java.io.IOException;

import org.json.simple.parser.ParseException;

import com.lux.parse.exceptions.FromToParseException;
import com.lux.parse.util.FileManager;

/**
 * 
 * Data model class
 */
public class DataManager {

    private String path = "";
    private String skip = "";
    private String fileNames = "";
    private String from = "";
    private String to = "";

    /**
     * Takes data from window
     * 
     * @param path path to repositories
     * @param skip list of repositories which must be skipped
     * @param fileNames list of files which must be changed
     * @param from data which must be changed
     * @param to data which replace old data
     */
    public DataManager(String path, String skip, String fileNames, String from, String to) {
        this.path = path;
        this.skip = skip;
        this.fileNames = fileNames;
        this.from = from;
        this.to = to;
    }

    /**
     * It needs to load data from file
     */
    public DataManager() {
    }

    /**
     * Starts replacing process
     * 
     * @throws FromToParseException @see FromToParseException
     * @throws IOException input output exception which can occur during writing to files which must be changed
     */
    public void parse() throws FromToParseException, IOException {
        AddressesGetter addresses = new AddressesGetter();
        new ParseProcess(addresses.getAdresses(path, skip, fileNames), addresses.getDirectories(), from, to);
    }

    /**
     * Saves data to file
     * 
     * @param file target file to save
     * @throws IOException input output exception which can occur during writing to file
     * @throws FromToParseException can occur during parsing and comparing "From" and "To" windows data
     */
    public void saveToFile(File file) throws IOException, FromToParseException {
        FileManager.saveToFile(file, new DataManager(path, skip, fileNames, from, to));
    }

    /**
     * Loads data from file
     * 
     * @param file json file which must be loaded
     * @throws IOException input output exception which can occur during reading from file
     * @throws ParseException can occur during parsing json data file
     */
    public void loadFromFile(File file) throws IOException, ParseException {
        DataManager dataManager;
        dataManager = FileManager.loadFromFile(file);
        this.path = dataManager.path;
        this.skip = dataManager.skip;
        this.fileNames = dataManager.fileNames;
        this.from = dataManager.from;
        this.to = dataManager.to;
    }

    /**
     * Gets path to repos
     * 
     * @return path to repos`s folder
     */
    public String getPath() {
        return path;
    }

    /**
     * Gets repositories list to skip
     * 
     * @return repos to skip
     */
    public String getSkip() {
        return skip;
    }

    /**
     * Gets filenames which must be changed
     * 
     * @return file names
     */
    public String getFileNames() {
        return fileNames;
    }

    /**
     * Get data which must be changed
     * 
     * @return change from
     */
    public String getFrom() {
        return from;
    }

    /**
     * Get new data which replace "From" data
     * 
     * @return change to
     */
    public String getTo() {
        return to;
    }
}
