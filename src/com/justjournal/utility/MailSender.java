/*
Copyright (c) 2006, 2009 Lucas Holt
All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are
permitted provided that the following conditions are met:

  Redistributions of source code must retain the above copyright notice, this list of
  conditions and the following disclaimer.

  Redistributions in binary form must reproduce the above copyright notice, this
  list of conditions and the following disclaimer in the documentation and/or other
  materials provided with the distribution.

  Neither the name of the Just Journal nor the names of its contributors
  may be used to endorse or promote products derived from this software without
  specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND
CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR
BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY
OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
POSSIBILITY OF SUCH DAMAGE.
*/

package com.justjournal.utility;

import com.justjournal.core.Settings;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.Properties;

/**
 * User: laffer1
 * Date: Mar 11, 2006
 * Time: 11:37:53 AM
 */
public class MailSender extends Thread {

    // TODO: Consider ServletContextListener
 /*   public MailSender() {
        setDaemon( true );
        //start();
    } */

    public void run() {
        System.out.println("MailSender: Init");

        String DbEnv = "java:comp/env/jdbc/jjDB";
        String sqlSelect = "SELECT * FROM queue_mail;";

        try {
            System.out.println("MailSender: Lookup context");
            Context ctx = new InitialContext();
            DataSource ds = (DataSource) ctx.lookup(DbEnv);

            Connection conn = ds.getConnection();
            System.out.println("MailSender: DB Connection up");

            Settings set = new Settings();
            Properties props = new Properties();
            props.put("mail.smtp.host", set.getMailHost());
            props.put("mail.smtp.user", set.getMailUser());
            props.put("mail.smtp.auth", "true");

            props.put("mail.smtp.port", set.getMailPort());
            Session s = Session.getInstance(props, new ForcedAuthenticator());

            System.out.println("MailSender: " + set.getMailUser() + "@" +
                    set.getMailHost() + ":" + set.getMailPort());

            while (true) {
                try {
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(sqlSelect);
                    System.out.println("MailSender: Recordset loaded.");

                    while (rs.next()) {
                        boolean sentok = true;

                        try {
                            InternetAddress from = new InternetAddress(rs.getString("from"));
                            InternetAddress to = new InternetAddress(rs.getString("to"));

                            MimeMessage message = new MimeMessage(s);
                            message.setFrom(from);
                            message.setRecipient(Message.RecipientType.TO, to);
                            message.setSubject(rs.getString("subject"));
                            message.setText(rs.getString("body"));
                            message.setSentDate(new Date());
                            message.saveChanges();

                            Address [] a = {to};
                            Transport t = s.getTransport("smtp");
                            t.connect();
                            t.sendMessage(message, a);
                            t.close();
                            //Transport.send(message);
                        } catch (AddressException e) {
                            sentok = false;
                            System.out.println("MailSender: Invalid address. " + e.getMessage());
                        } catch (javax.mail.MessagingException me) {
                            sentok = false;
                            System.out.println("MailSender: Send failed." + me.getMessage() + " : " + me.toString());
                        }

                        if (sentok) {
                            Statement stmt2 = conn.createStatement();
                            stmt2.execute("DELETE FROM queue_mail WHERE id=" + rs.getString("id") + ";");
                            stmt2.close();
                            System.out.println("MailSender: item " + rs.getString("id") + " deleted");
                        }

                        yield();  // be nice to others... we are in a servlet container...
                    }

                    rs.close();
                    stmt.close();
                    sleep(1000 * 60 * 15); // 15 minutes?
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (Exception e) {
                    System.out.println("MailSender: Exception - " + e.getMessage());
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

        System.out.println("MailSender: Quit");
    }

    class ForcedAuthenticator extends Authenticator {
        public PasswordAuthentication getPasswordAuthentication() {
            Settings set = new Settings();
            //System.out.println("ForcedAuthenticator: " + set.getMailUser() + " " + set.getMailPass());
            return new PasswordAuthentication(set.getMailUser(), set.getMailPass());
        }
    }
}

