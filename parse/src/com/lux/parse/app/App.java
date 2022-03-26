package com.lux.parse.app;

import com.lux.parse.ui.MainWindow;

/**
 * Main class, application starts from this class. This application updates data in "Files list" via creation new
 * solution branch, changes "From" -&#62; "To", path to repositories to change must be defined in "Path", if some
 * repositories must be skipped(not changed) they must be defined in "Skip" window and divided by: |
 * 
 * @author Pavel
 * 
 */
public class App {
    /**
     * Starts application
     * 
     * @param args in this application does not used
     */
    public static void main(String[] args) {
        MainWindow view = new MainWindow();
        view.open();
    }
}
