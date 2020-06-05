package com.lux.parse.manager;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.StringTokenizer;

import com.lux.parse.util.ParserExpressionConstants;
import org.apache.commons.lang3.ArrayUtils;

class AdressesGetter {
    
    private String[] directories;
    private String[] subDirectories;
    
    public ArrayList<LinkedHashMap<Integer, File>> getAdresses(String path,String skip,String fileNames) {
        LinkedList<String> skipList= getSkip(skip);
        String[] directories=getDirectories(path);
        this.directories=excludes(directories,skipList);
        this.subDirectories=getSubDirectories(fileNames);
        return prepareWays(path);
    }

    private LinkedList<String> getSkip(String skip) {
        LinkedList<String> skipList= new LinkedList<String>();
        StringTokenizer st = new StringTokenizer(skip, "|");
        while(st.hasMoreTokens()) {
            String name=st.nextToken();
            skipList.add(name);
            //System.out.println(name);
        }
        return skipList;
    }

    public String[]getDirectories(){
        return directories;
    }
    
    private String[] getDirectories(String path) {
        File file = new File(path);
        String[]directories = file.list(new FilenameFilter() {
            @Override
            public boolean accept(File current, String name) {
                return new File(current, name).isDirectory();
            }
        });
        return directories;
    }

    private String[] excludes(String[] directories, LinkedList<String> skip) {
       for(int i=0;i<skip.size();i++) {
           for(int j=0;j<directories.length;j++) {
               if(skip.get(i).trim().equals(directories[j].trim())) {
                   directories= ArrayUtils.remove(directories, j);
               }
           }
       }
      return directories;
      // show(this.directories);
    }

    private void show(String[] directories2) {
        for(int j=0;j<directories2.length;j++) {
            System.out.println(directories2[j]);
        }
        
    }

    private String[] getSubDirectories(String fileNames) {
        String lineSeparator = System.lineSeparator();
        return fileNames.split(lineSeparator);
    }

    private ArrayList<LinkedHashMap<Integer, File>> prepareWays(String path) {
        ArrayList<LinkedHashMap<Integer, File>> ways;
        ways = new ArrayList<LinkedHashMap<Integer, File>>();
        String backSlash = "\\";
        if (!path.endsWith(backSlash)) {
            path = path + backSlash;
        }
        for (int i = 0; i < directories.length; i++) {
            LinkedHashMap<Integer, File> waysLocal = new LinkedHashMap<Integer, File>();
            for (int j = 0; j < subDirectories.length; j++) {
                String temp = subDirectories[j].replaceAll(ParserExpressionConstants.NAME, directories[i]);
                File file = new File(path + directories[i] + backSlash + temp);
                if (file.exists()) {
                    waysLocal.put(j, file);
                }
                //System.out.println(file.getPath());
            }
            ways.add(waysLocal);
        }
        
        return ways;
    }
}
