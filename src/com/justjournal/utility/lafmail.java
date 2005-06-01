/*
Copyright (c) 2005, Lucas Holt
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

//
//  lafmail.java
//  
//
//  Created by laffer1 on Sun May 26 2002.
//  Copyright (c) 2002 Lucas Holt. All rights reserved.
//

package com.justjournal.utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public final class lafmail {

    private static int port = 25;
    private static String NewLine = "\r\n";

    public static void main(String[] args) throws IOException {

        String host = "";
        String FromEmail = "";
        String FromName = "";
        String ToName = "";
        String ToEmail = "";
        String Subject = "";
        String Body = "";
        String Origin = "";
        int Priority = 3;

        PrintHeader();

        if (args.length > 4) {
            host = args[0];  // get the host to connect to
            FromEmail = args[1];
            ToEmail = args[2];
            Subject = args[3];
            Body = args[4];
            Origin = args[5];
        } else {
            System.out.println("Error: Please pass parameters.");
            System.exit(1);
        }


        Socket echoSocket = null;
        PrintWriter out = null;
        BufferedReader in = null;

        try {
            echoSocket = new Socket(host, port);
            out = new PrintWriter(echoSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + host);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for "
                    + "the connection to " + host);
            System.exit(1);
        }

        String myResponse;
        String ServerResponse;
        String ServerResponseCode;

        // Start negotiations....
        // this is not system.out
        out.println("Helo" + " " + Origin);

        ServerResponse = in.readLine();
        myResponse = ServerResponse + NewLine;

        if (ServerResponse.length() > 3) {
            ServerResponseCode = ServerResponse.substring(0, 3);
            System.out.println(ServerResponseCode);

            if (ServerResponseCode.equalsIgnoreCase("220")) {
                System.out.println("Connected.");
            } else {
                HandleError("Server Status Code is not 220");
            }
        }

        if (ServerResponse.length() > 3) {
            // Mail From email address
            out.println("MAIL FROM:" + " " + FromEmail);
            myResponse += in.readLine() + NewLine;
        }

        if (ServerResponse.length() > 3) {
            // Mail To email address
            out.println("RCPT TO:" + " " + ToEmail);
            myResponse += in.readLine() + NewLine;
        }

        if (ServerResponse.length() > 3) {
            // Begin Actual message.
            out.println("DATA");
            myResponse += in.readLine() + NewLine;
        }

        if (ServerResponse.length() > 3) {
            out.println("To:" + " " + ToEmail);
            out.println("From:" + " " + FromEmail);
            out.println("Subject:" + " " + Subject);
            out.println("Content-Type: text/plain; charset=us-ascii");
            out.println("X-Priority:" + " " + Priority);
            out.println("X-Mailer:" + " " + "lafmail/1.0");

            // Need a crlf in between header stuff and body.
            out.println();
            out.println(Body);
            out.println(".");
            myResponse += in.readLine() + NewLine;
        }

        if (ServerResponse.length() > 3) {
            out.println("QUIT");
            myResponse += in.readLine() + NewLine;

            System.out.println("Transaction Log:");
            System.out.println(myResponse);
            //java.io.FileWriter("lafmaillog.txt", true);
        }

        // Clean Up
        out.close();
        in.close();
        echoSocket.close();

    }

    private static void PrintHeader() {
        // Write Program Header
        System.out.println("************************************************************");
        System.out.println();
        System.out.println("Laf Mail");
        System.out.println("Version 1.0, by Lucas Holt");
        System.out.println();
        System.out.println("************************************************************");
        System.out.println();

    }

    private static void HandleError(String ErrorMsg) {
        System.out.println("An Error has occured: " + ErrorMsg);
        System.exit(1);
    }

}
