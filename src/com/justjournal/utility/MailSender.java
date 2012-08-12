/*
Copyright (c) 2006, 2009, 2012 Lucas Holt
All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are
permitted provided that the following conditions are met:

  Redistributions of source code must retain the above copyright notice, this list of
  conditions and the following disclaimer.

  Redistributions in binary form must reproduce the above copyright notice, this
  list of conditions and the following disclaimer in the documentation and/or other
  materials provided with the distribution.

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
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.access.DataContext;
import org.apache.cayenne.configuration.server.ServerRuntime;
import org.apache.cayenne.query.SelectQuery;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

/**
 * Send e-mail notifications for just journal from the mail queue.
 *
 * @author Lucas Holt
 * @version $Id: MailSender.java,v 1.8 2009/03/16 22:10:31 laffer1 Exp $
 */
public class MailSender extends Thread {

    // TODO: Consider ServletContextListener
    /*   public MailSender() {
       setDaemon( true );
       //start();
   } */

    public void run() {
        System.out.println("MailSender: Init");
        try {
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
                    ObjectContext dataContext = DataContext.getThreadObjectContext();
                    if (dataContext == null)      {
                        ServerRuntime cayenneRuntime = new ServerRuntime("cayenne-JustJournalDomain.xml");
                       dataContext = cayenneRuntime.getContext();
                    }

                    SelectQuery query = new SelectQuery(com.justjournal.model.QueueMail.class);
                    java.util.List<com.justjournal.model.QueueMail> items = dataContext.performQuery(query);

                    System.out.println("MailSender: Recordset loaded.");

                    for (com.justjournal.model.QueueMail item : items) {
                        boolean sentok = true;

                        try {
                            InternetAddress from = new InternetAddress(item.getFrom());
                            InternetAddress to = new InternetAddress(item.getTo());

                            MimeMessage message = new MimeMessage(s);
                            message.setFrom(from);
                            message.setRecipient(Message.RecipientType.TO, to);
                            message.setSubject(item.getSubject());
                            message.setText(item.getBody());
                            message.setSentDate(new Date());
                            message.saveChanges();

                            Address[] a = {to};
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
                            dataContext.deleteObject(item);
                            dataContext.commitChanges();
                        }

                        yield();  // be nice to others... we are in a servlet container...
                    }
                    sleep(1000 * 60 * 15); // 15 minutes?
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (Exception e) {
                    System.out.println("MailSender: Exception - " + e.getMessage());
                }
            }
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

