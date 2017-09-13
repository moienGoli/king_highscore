package com.king.service.logger;

/**
 * This class is just printing to terminal.
 * But It is possible to change to a more convenient logging tool later
 * <p>
 * Created by moien on 9/11/17.
 */
public class Logger {

    private Logger() {
    }

    @SuppressWarnings("all")
    public static void log(String msg) {
        System.out.println(msg);
    }
}
