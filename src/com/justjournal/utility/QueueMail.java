
package com.justjournal.utility;

import com.justjournal.SQLHelper;

/**
 * Created by IntelliJ IDEA.
 * User: laffer1
 * Date: Jan 16, 2004
 * Time: 12:54:58 PM
 * To change this template use Options | File Templates.
 */
public final class QueueMail
{
    // e-mail properties
    private String Subject = "";
    private String Body = "";
    private String toAddress = "";
    private String fromAddress = "";
    private String purpose = "";

    public static void main( String[] args )
    {
        // an instance must be created to use this for now.
        System.out.println( "can not run this from command line" );
        return;
    }

    public void setSubject( String mailSubject )
    {
        Subject = mailSubject;
        return;
    }

    public void setBody( String mailBody )
    {
        Body = mailBody;
        return;
    }

    public void setToAddress( String mailToAddress )
    {
        toAddress = mailToAddress;
        return;
    }

    public void setFromAddress( String mailFromAddress )
    {
        fromAddress = mailFromAddress;
        return;
    }

    public void setPurpose( String mailPurpose )
    {
        purpose = mailPurpose;
        return;
    }

    public void send()
            throws Exception
    {

        String sqlstmt = "Insert INTO `queue_mail` ( `to` , `from` , `subject` , `body` , `purpose` ) VALUES( '" + toAddress + "' , '" + fromAddress + "' , '" + Subject + "' , '" + Body + "' , '" + purpose + "' );";
        int result = SQLHelper.executeNonQuery(sqlstmt);

        if ( result != 1)
            throw new Exception("Couldn't add mail to queue");

        return;
    }

}

