/*
 * Copyright (c) 2003-2021 Lucas Holt
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR AND CONTRIBUTORS ``AS IS'' AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED.  IN NO EVENT SHALL THE AUTHOR OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
 * OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 */
package com.justjournal.utility;


import java.io.*;

/**
 * File IO utility classes
 *
 * @author Lucas Holt
 */
public final class FileIO {

  public static String readTextFile(final String filePath) throws IOException {
    int myC;
    final StringWriter myInput = new StringWriter();
    try (FileReader myFR = new FileReader(filePath)) {
      myC = myFR.read();

      while (myC != -1) {
        myInput.write(myC);
        // System.out.print( myC );  // debug
        myC = myFR.read();
      }
    }

    return myInput.toString();
  }

  public static void writeTextFile(final String filePath, final String dataToWrite)
      throws IOException {
    try (FileWriter myFW = new FileWriter(filePath, false)) {
      myFW.write(dataToWrite);
    }
  }

  public boolean makeDirectory(final String path) {
    final File dir = new File(path);
    return dir.mkdir();
  }

  public boolean deleteDirectory(final String path) {
    final File dir = new File(path);
    return dir.isDirectory() && dir.delete();
  }

  public boolean deleteFile(final String path) {
    final File f = new File(path);
    return f.isFile() && f.delete();
  }

  public boolean touchFile(final String path) {
    final File f = new File(path);

    try {
      return f.createNewFile();
    } catch (final IOException ef) {
      return false;
    }
  }

  public long fileLength(final String path) {
    final File f = new File(path);
    return f.length();
  }

  public long fileLastModified(final String path) {
    final File f = new File(path);
    return f.lastModified();
  }

  public boolean renameFile(final String source, final String destination) {
    final File s = new File(source);
    final File d = new File(destination);

    return s.renameTo(d);
  }

  public String[] listFiles(final String path) {
    final File dir = new File(path);
    return dir.list();
  }
}
