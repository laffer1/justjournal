/*
 * jj_auth.java
 *
 * Created on October 10, 2005, 11:38 AM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package java_jjclient;

import javax.net.ssl.HttpsURLConnection;
import java.net.*;
//import javax.net.ssl.*;
import java.io.*;

/**
 *
 * @author caryn
 */
public class jj_auth {
    
    private String userName;
    private String passWord;

    /**
     * Creates instance of jj_auth
     * @param username
     * @param password
     */
    public jj_auth(String username, String password) {
        userName = username;
        passWord = password;
    }

    /**
     * Checks account over unsecured channel
     * @return true if login was successful
     */
    public boolean checkAccount () {
        // check the  username and password against
        //  servlet
        try {
            // sending the post request
            String data = URLEncoder.encode("username", "US-ASCII") + "=" +
                    URLEncoder.encode(userName, "US-ASCII");
            data += "&" + URLEncoder.encode("password", "US-ASCII") + "=" +
                    URLEncoder.encode(passWord, "US-ASCII");
            data += "&" + URLEncoder.encode("password_hash", "US-ASCII") + "=" +
                    URLEncoder.encode("", "US-ASCII");
            URL url = new URL("http://www.justjournal.com/loginAccount");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            
            // set user-agent header in POST request
            conn.setRequestProperty("User-Agent","JustJournal/Java");
                        
            conn.setRequestMethod ("POST");
            conn.setDoOutput(true);
            OutputStreamWriter writer = 
                    new OutputStreamWriter(conn.getOutputStream());

            writer.write(data);
            writer.flush();
            writer.close();
            // getting the response
            BufferedReader input = new BufferedReader (new InputStreamReader
                    (conn.getInputStream()));
            char [] returnCode = new char [50];
            int i = 0;
            int tempChar = input.read();
            while (tempChar != -1) {
                returnCode[i] = (char) tempChar;
                tempChar = input.read();
                i++;
            }

            input.close();
            String code = new String (returnCode);
            code = code.trim();

            if (code.equals("JJ.LOGIN.OK"))
                return true;
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
        // if the function gets this far, an error resulted
        // for GUI testing return true
        return false;
    }

    /**
     * Checks account over secure channel
     * @return true if account is valid
     */
    public boolean SecureCheckAccount () {
        try {
            // sending the post request
            userName = userName.trim();
            String data = "username=" + userName;
            data += "&password=" + passWord;
            data += "&password_hash=";
            URL jj = new URL ("https://www.justjournal.com/loginAccount");
            HttpsURLConnection sslConn = (HttpsURLConnection) jj.openConnection();
            sslConn.setRequestProperty("User-Agent","JustJournal");

            sslConn.setRequestMethod ("POST");
            sslConn.setDoOutput(true);
            sslConn.setDoInput (true);
            OutputStreamWriter writer =
                    new OutputStreamWriter(sslConn.getOutputStream());

            writer.write(data);
            writer.flush();
            writer.close();
            // getting the response
            BufferedReader input = new BufferedReader (new InputStreamReader
                    (sslConn.getInputStream()));
            char [] returnCode = new char [50];
            int i = 0;
            int tempChar = input.read();
            while (tempChar != -1) {
                returnCode[i] = (char) tempChar;
                tempChar = input.read();
                i++;
            }

            String code = new String (returnCode);
            code = code.trim();
            input.close();

            if (code.equals("JJ.LOGIN.OK"))
                return true;

        }
        catch (Exception e) {
            System.err.println (e.getMessage());
        }
        return false;
    }
    
}
