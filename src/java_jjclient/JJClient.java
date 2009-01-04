package java_jjclient;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * Created by IntelliJ IDEA.
 * User: caryn
 * Date: Oct 17, 2005
 * Time: 7:15:24 PM
 */
public class JJClient {

    /**
     * Main function
     *
     * @param args
     */
    public static void main(String args[]) {

        // Mac OS X specific extensions
        if (System.getProperty("mrj.version") != null) {
            com.apple.eawt.Application a = com.apple.eawt.Application.getApplication();
            a.setEnabledAboutMenu(false);
            a.setEnabledPreferencesMenu(false);
        }
        jjGUI gui = new jjGUI();
    }

}


