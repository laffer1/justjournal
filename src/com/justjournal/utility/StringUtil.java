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
//  stringUtil.java
//
//
//  Created by Lucas Holt on Sun Jun 01 2003.
//  Copyright (c) 2003 __MyCompanyName__. All rights reserved.
//

package com.justjournal.utility;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * A generalized string manipulation library.
 *
 * @author Lucas Holt
 * @version 1.1
 * @since 1.0
 */
public final class StringUtil {

    /**
     * Deletes multiple instances of a specific character in a string
     *
     * @param origin  Original string to modify
     * @param delChar Character to remove from the string
     * @return Modified string
     */
    public static String deleteChar(String origin, char delChar) {
        final int len = origin.length();
        char[] val = origin.toCharArray();
        char[] buf = new char[len];
        int j = 0;

        for (int i = 0; i < len; ++i) {
            if (val[i] != delChar) {
                buf[j++] = val[i];
            }
        }

        return new String(buf, 0, j);
    }


    /**
     * Replace a certain character in a string with a new substring.
     *
     * @param base Original string to modify
     * @param ch   Character to replace
     * @param str  Modified string after completion.
     */
    public static String replace(String base, char ch, String str) {
        return (base.indexOf(ch) < 0) ? base :
                replace(base, String.valueOf(ch), new String[]{str});
    }


    /**
     * Replace certain characters in a string with a new substring.
     *
     * @param base
     * @param delim
     * @param str
     * @return Modified string after operations.
     */
    public static String replace(String base, String delim, String[] str) {
        final int len = base.length();
        final StringBuffer result = new StringBuffer();

        for (int i = 0; i < len; i++) {
            final char ch = base.charAt(i);
            final int k = delim.indexOf(ch);

            if (k >= 0) {
                result.append(str[k]);
            } else {
                result.append(ch);
            }
        }
        return result.toString();
    }

    /**
     * Uses split to break up a string of input separated by
     * commas and/or whitespace.
     */

    /*
    public static String[] splitter( String[] args ) throws Exception
    {
        // Create a pattern to match breaks
        Pattern p = Pattern.compile( "[,\\s]+" );
        // Split input with the pattern
        String[] result = p.split( "one,two, three   four ,  five" );
        return result;
    }
    */

    /**
     * Checks e-mail addresses for invalid characters.
     * If the address is invalid, false is returned.
     * <p/>
     * This might be too restrictive
     * TODO: look at .NET version or regex.com
     * Email address generally contain more characters
     * than this.  (flawed)
     *
     * @param address
     * @return true if the addres is valid.
     */
    public static boolean isEmailValid(String address) {
        final Pattern p = Pattern.compile("[^A-Za-z0-9\\.\\@_\\-~#]+");
        final Matcher m = p.matcher(address);
        boolean invalidEmail = m.find();

        return !invalidEmail;
    }

    /**
     * Checks a string to find non alpha numeric characters.
     * If found, it returns false.
     *
     * @param input A string to check for alphanumeric characters.
     * @return boolean indicating alphanumeric status
     */
    public static boolean isAlphaNumeric(String input) {
        final Pattern p = Pattern.compile("[\\W]+");
        final Matcher m = p.matcher(input);
        boolean invalidInput = m.find();

        return !invalidInput;
    }

}
