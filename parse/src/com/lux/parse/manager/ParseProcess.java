package com.lux.parse.manager;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import com.lux.parse.model.ParsingModel;

public class ParseProcess {

    private static final String NAME = "\"name\"";
    private String[] directories;
    private String[] subDirectories;
    private ArrayList<LinkedHashMap<Integer,File>> ways;

    public ParseProcess(ParsingModel parsingModel) {
        String path=parsingModel.getPath();
        getDirectories(path);
        getSubDirectories(parsingModel.getFileNames());
        prepareWays(path, directories, subDirectories);
        
        System.out.println(ways);
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

    private void prepareWays(String path, String[] directories, String[] subDirectories) {
        ways = new ArrayList<LinkedHashMap<Integer,File>>();
        String backSlash="\\";
        if (!path.endsWith(backSlash)) {
            path = path + backSlash;
        }
        for (int i = 0; i < directories.length; i++) {
            LinkedHashMap<Integer,File> waysLocal=new LinkedHashMap<Integer,File>();
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
