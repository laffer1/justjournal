/*
 * Copyright (c) 2014 Lucas Holt
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

package com.justjournal;

import com.justjournal.db.model.DateTime;

/**
 * @author Lucas Holt
 */
public interface User {

    public String getBiography();

    public void setBiography(final String biography);

    public String getJournalName();

    public boolean getPingServices();

    public void setPingServices(final boolean pingServices);

    public boolean getShowAvatar();

    public void showAvatar(final boolean showAvatar);

    public boolean showAvatar();

    public String getEmailAddress();

    public void setEmailAddress(final String emailAddress);

    public String getStyleUrl();

    public void setStyleUrl(final String url);

    public String getStyleDoc();

    public void setStyleDoc(final String doc);

    public int getStyleId();

    public void setStyleId(final int styleId);

    public int getEmoticon();

    public void setEmoticon(final int value);

    public boolean isPrivateJournal(final boolean privateJournal);

    public boolean isPrivateJournal();

    public boolean isSpiderAllowed(final boolean allowSpider);

    public boolean isSpiderAllowed();

    public int getStartYear();

    public void setStartYear(final int since);

    public DateTime getLastLogin();

    public String getFirstName();

    public void setFirstName(final String firstName);

    public int getType();

    public void setType(final int type);

    public void setUserId(final Integer userId);

    public int getUserId();

    public void setUserId(final int userId);

    public String getUserName();

    public void setUserName(final String userName);
}
