package com.lux.parse.manager;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.StringTokenizer;

import com.lux.parse.util.ParserExpressionConstants;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.lang3.ArrayUtils;

/**
 * Provides file`s addresses
 * 
 */
class AddressesGetter {

    private String[] directories;
    private String[] subDirectories;

    /**
     * Configures file`s addresses
     * 
     * @param path path to source folder which contents repositories which must be changed
     * @param skip folders(projects) which must be skipped
     * @param fileNames files names which must be changed
     * @return files addresses
     */
    ArrayList<LinkedHashMap<Integer, LinkedList<File>>> getAdresses(String path, String skip, String fileNames) {
        LinkedList<String> skipList = getSkip(skip);
        String[] directories = getDirectories(path);
        this.directories = excludes(directories, skipList);
        this.subDirectories = getSubDirectories(fileNames);
        return prepareWays(path);
    }

    private LinkedList<String> getSkip(String skip) {
        LinkedList<String> skipList = new LinkedList<String>();
        StringTokenizer st = new StringTokenizer(skip, "|");
        while (st.hasMoreTokens()) {
            String name = st.nextToken();
            skipList.add(name);
            // System.out.println(name);
        }
        return skipList;
    }

    /**
     * Gets directories (project) names
     * 
     * @return directories`s names
     */
    String[] getDirectories() {
        return directories;
    }

    private String[] getDirectories(String path) {
        File file = new File(path);
        String[] directories = file.list(new FilenameFilter() {
            @Override
            public boolean accept(File current, String name) {
                return new File(current, name).isDirectory();
            }
        });
        return directories;
    }

    private String[] excludes(String[] directories, LinkedList<String> skip) {
        for (int i = 0; i < skip.size(); i++) {
            for (int j = 0; j < directories.length; j++) {
                if (skip.get(i).trim().equals(directories[j].trim())) {
                    directories = ArrayUtils.remove(directories, j);
                }
            }
        }
        return directories;
        // show(this.directories);
    }

    private void show(String[] directories2) {
        for (int j = 0; j < directories2.length; j++) {
            System.out.println(directories2[j]);
        }

    }

    private String[] getSubDirectories(String fileNames) {
        String lineSeparator = System.lineSeparator();
        return fileNames.split(lineSeparator);
    }

    private ArrayList<LinkedHashMap<Integer, LinkedList<File>>> prepareWays(String path) {
        ArrayList<LinkedHashMap<Integer, LinkedList<File>>> ways;
        ways = new ArrayList<LinkedHashMap<Integer, LinkedList<File>>>();
        String backSlash = "\\";
        if (!path.endsWith(backSlash)) {
            path = path + backSlash;
        }
        for (int i = 0; i < directories.length; i++) {
            LinkedHashMap<Integer, LinkedList<File>> waysLocal = new LinkedHashMap<Integer, LinkedList<File>>();
            int index =0;
            for (int j = 0; j < subDirectories.length; j++) {
                String temp = subDirectories[j].replaceAll(ParserExpressionConstants.NAME, directories[i]);
                if (temp.startsWith("..")) {
                    temp = temp.replaceAll("\\.\\.", "");
                    LinkedList<File> fileAsList = findAllFilesByNameInSubDirrectories(path + directories[i], temp);
                    waysLocal.put(j, fileAsList);
                } else {
                    File file = new File(path + directories[i] + backSlash + temp);
                    //if (file.exists()) {
                        LinkedList<File> fileAsList = new LinkedList<File>();
                        fileAsList.add(file);
                        waysLocal.put(j, fileAsList);
                    //}
                }
            }
            ways.add(waysLocal);
        }

        return ways;
    }

    private LinkedList<File> findAllFilesByNameInSubDirrectories(String path, String temp) {
        LinkedList<File> files = (LinkedList<File>) FileUtils.listFiles(new File(path), new RegexFileFilter(temp),
                TrueFileFilter.INSTANCE);
        return files;
    }
}
