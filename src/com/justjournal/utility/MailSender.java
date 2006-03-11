package com.justjournal.utility;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

/**
 * User: laffer1
 * Date: Mar 11, 2006
 * Time: 11:37:53 AM
 */
public class MailSender extends Thread {

    public void run() {
        setDaemon(true); // don't keep app alive

        String DbEnv = "java:comp/env/jdbc/jjDB";
        String sqlSelect = "SELECT id, to, from, subject, body " +
                "FROM queue_mail;";

        try {
            Context ctx = new InitialContext();
            DataSource ds = (DataSource) ctx.lookup(DbEnv);

            Connection conn = ds.getConnection();

            Properties props = new Properties();
            props.put("mail.smtp.host", "localhost");
            Session s = Session.getInstance(props, null);


            while (true) {
                try {
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(sqlSelect);

                    while (rs.next()) {
                        boolean sentok = true;

                        InternetAddress from = new InternetAddress(rs.getString("from"));
                        InternetAddress to = new InternetAddress(rs.getString("to"));
                        MimeMessage message = new MimeMessage(s);
                        message.setFrom(from);
                        message.addRecipient(Message.RecipientType.TO, to);
                        message.setSubject(rs.getString("subject"));
                        message.setText(rs.getString("body"));

                        try {
                            Transport.send(message);
                        } catch (javax.mail.MessagingException me) {
                            sentok = false;
                        }

                        if (sentok) {
                            Statement stmt2 = conn.createStatement();
                            stmt2.execute("DELETE FROM queue_mail WHERE id=" + rs.getString("id") + ";");
                            stmt2.close();
                        }

                        yield();  // be nice to others... we are in a servlet container...
                    }

                    rs.close();
                    stmt.close();
                    sleep(1000 * 60 * 15); // 15 minutes?
                } catch (Exception e) {

                }
            }

            /*  try {
              conn.close();
         } catch (Exception e2) {
             e2.printStackTrace();
         }   */

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

