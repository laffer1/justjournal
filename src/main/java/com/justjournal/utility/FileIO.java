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

package com.justjournal.utility;

import com.sun.istack.internal.NotNull;

import java.io.*;

/**
 * File IO utility classes
 * @author Lucas Holt
 */
public final class FileIO {

    public @NotNull
    static String ReadTextFile(String FilePath)
            throws IOException {
        int myC;
        StringWriter myInput = new StringWriter();
        FileReader myFR = new FileReader(FilePath);
        myC = myFR.read();

        while (myC != -1) {
            myInput.write(myC);
            // System.out.print( myC );  // debug
            myC = myFR.read();
        }

        myFR.close();

        return myInput.toString();
    }

    public static void writeTextFile(String FilePath, String DataToWrite)
            throws IOException {
        FileWriter myFW = new FileWriter(FilePath, false);
        myFW.write(DataToWrite);
        myFW.close();
    }

    public boolean makeDirectory(String path) {
        File dir = new File(path);
        return dir.mkdir();
    }

    public boolean deleteDirectory(String path) {
        File dir = new File(path);
        return dir.isDirectory() && dir.delete();
    }

    public boolean deleteFile(String path) {
        File f = new File(path);
        return f.isFile() && f.delete();
    }

    public boolean touchFile(String path) {
        File f = new File(path);

        try {
            return f.createNewFile();
        } catch (IOException ef) {
            return false;
        }
    }

    public long fileLength(String path) {
        File f = new File(path);
        return f.length();
    }

    public long fileLastModified(String path) {
        File f = new File(path);
        return f.lastModified();
    }

    public boolean renameFile(String source, String destination) {
        File s = new File(source);
        File d = new File(destination);

        return s.renameTo(d);
    }

    public String[] listFiles(String path) {
        File dir = new File(path);
        return dir.list();
    }
}
