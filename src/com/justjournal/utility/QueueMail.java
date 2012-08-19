/*
Copyright (c) 2004-2006, Lucas Holt
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

import com.justjournal.db.SQLHelper;

/**
 * Represent an email message in the queue
 * @author Lucas Holt
 */
public final class QueueMail {
    // e-mail properties
    private String Subject = "";
    private String Body = "";
    private String toAddress = "";
    private String fromAddress = "";
    private String purpose = "";

    public QueueMail() {

    }

    public QueueMail(String subject, String body, String toAddress, String fromAddress, String purpose) {
        Subject = subject;
        Body = body;
        this.toAddress = toAddress;
        this.fromAddress = fromAddress;
        this.purpose = purpose;
    }

    public static void main(String[] args) {
        // an instance must be created to use this for now.
        System.out.println("can not run this from command line");
    }

    public void setSubject(String mailSubject) {
        Subject = mailSubject;
    }

    public void setBody(String mailBody) {
        Body = mailBody;
    }

    public void setToAddress(String mailToAddress) {
        toAddress = mailToAddress;
    }

    public void setFromAddress(String mailFromAddress) {
        fromAddress = mailFromAddress;
    }

    public void setPurpose(String mailPurpose) {
        purpose = mailPurpose;
    }

    public String getSubject() {
        return Subject;
    }

    public String getBody() {
        return Body;
    }

    public String getToAddress() {
        return toAddress;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public String getPurpose() {
        return purpose;
    }

    public void send()
            throws Exception {

        String sqlstmt = "Insert INTO `queue_mail` ( `to` , `from` , `subject` , `body` , `purpose` ) VALUES( '" + getToAddress() + "' , '" + getFromAddress() + "' , '" + getSubject() + "' , '" + getBody() + "' , '" + getPurpose() + "' );";
        int result = SQLHelper.executeNonQuery(sqlstmt);

        if (result != 1)
            throw new Exception("Couldn't add mail to queue");
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final QueueMail queueMail = (QueueMail) o;

        if (!getBody().equals(queueMail.getBody())) return false;
        if (!getSubject().equals(queueMail.getSubject())) return false;
        if (!getFromAddress().equals(queueMail.getFromAddress())) return false;
        if (!getPurpose().equals(queueMail.getPurpose())) return false;
        if (!getToAddress().equals(queueMail.getToAddress())) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = getSubject().hashCode();
        result = 29 * result + getBody().hashCode();
        result = 29 * result + getToAddress().hashCode();
        result = 29 * result + getFromAddress().hashCode();
        result = 29 * result + getPurpose().hashCode();
        return result;
    }
}

