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
import com.justjournal.model.QueueMail;
import com.justjournal.repository.QueueMailRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

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
@Component
public class MailSender extends Thread {

    public boolean process = true;
    private Logger log = LoggerFactory.getLogger(MailSender.class);

    @Autowired
    private QueueMailRepository queueMailRepository;

    @Autowired
    private Settings set;

    @Override
    @SuppressWarnings("unchecked")
    public void run() {
        log.trace("MailSender: Init");

        try {
            Properties props = new Properties();
            props.put("mail.smtp.host", set.getMailHost());
            props.put("mail.smtp.user", set.getMailUser());
            props.put("mail.smtp.auth", "true");

            props.put("mail.smtp.port", set.getMailPort());
            Session s = Session.getInstance(props, new ForcedAuthenticator());

            log.trace("MailSender: " + set.getMailUser() + "@" +
                    set.getMailHost() + ":" + set.getMailPort());

            while (process) {
                try {
                    Iterable<QueueMail> items = queueMailRepository.findAll();

                    log.trace("MailSender: Recordset loaded.");

                    for (QueueMail item : items) {
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
                            log.error("MailSender: Invalid address. " + e.getMessage());
                        } catch (MessagingException me) {
                            sentok = false;
                            log.error("MailSender: Send failed." + me.getMessage() + " : " + me.toString());
                        }

                        if (sentok) {
                            queueMailRepository.delete(item.getId());
                        }

                        yield();  // be nice to others... we are in a servlet container...
                    }
                    sleep(1000 * 60 * 15); // 15 minutes?
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (Exception e) {
                    log.error("MailSender: Exception - " + e.getMessage());
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        log.trace("MailSender: Quit");
    }
}

