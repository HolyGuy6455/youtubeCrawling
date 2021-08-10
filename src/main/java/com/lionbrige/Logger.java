package com.lionbrige;

import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * Logger
 */
public class Logger {

    JFrame frame;
    JLabel jLabel;
    private final static Logger singleton;
    static{
        singleton = new Logger();
    }

    Logger(){
        frame = new JFrame();
        jLabel = new JLabel();
        frame.add(jLabel);
        frame.setSize(300,100);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    /**
     * @return the singleton
     */
    public Logger getInstance() {
        return singleton;
    }

    public static void log(String str){
        System.out.println(str);
        singleton.jLabel.setText(str);
    }

    public static void titleLog(String str){
        singleton.frame.setTitle(str);
    }
}