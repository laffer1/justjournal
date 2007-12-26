/*
Copyright (c) 2006, 2007  Lucas Holt
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

package com.justjournal.db;

import java.util.Date;

/**
 * An interface for just journal's sql friendly dates.
 * User: laffer1
 * Date: Mar 24, 2006
 * Time: 11:22:14 PM
 *
 * @author Lucas Holt
 * @version $Id: DateTime.java,v 1.4 2007/12/26 06:02:35 laffer1 Exp $
 */
public interface DateTime {
    int getDay();

    void setDay(int day);

    int getMonth();

    void setMonth(int month);

    int getYear();

    void setYear(int year);

    int getHour();

    void setHour(int hour);

    int getMinutes();

    void setMinutes(int minutes);

    void set(String mysqlDate)
            throws java.text.ParseException;

    void set(java.util.Date date);

    Date toDate();

    String toPubDate();

    String toRFC3339();

    String toString();
}
