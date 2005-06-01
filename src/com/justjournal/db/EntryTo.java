
package com.justjournal.db;

/**
 * Journal entry transfer object.  Contains one journal entry.
 * Maps relationship between table "entry" and java.
 * @version 1.0
 * @author Lucas Holt
 * @see EntryDAO
 */
public final class EntryTo
{
    private int id;
    private int locationId;
    private int moodId;
    private int commentCount;
    private int userId;
    private int securityLevel;

    private DateTimeBean date;

    private String subject;
    private String body;
    private String music;
    private String userName;
    private String moodName;
    private String locationName;

    private boolean autoFormat = true;
    private boolean allowComments = true;
    private boolean emailComments = true;


    /**
     * Retrieves entry id as an int >0
     * @return entry id
     */
    public int getId()
    {
        return id;
    }


    /**
     * Set the entry id to an int >0
     * @param id  entry id to set
     * @throws IllegalArgumentException  id < 0
     */
    public void setId( int id )
        throws IllegalArgumentException
    {
        if (id < 0)
            throw new IllegalArgumentException("Illegal id: " +
                                               id);

        this.id = id;
    }


    /**
     * Retrieve the current location id
     * @return location id
     */
    public int getLocationId()
    {
        return locationId;
    }


    /**
     * Set the location id to an int >0
     * @param loc  location id to set
     * @throws IllegalArgumentException  < 0
     */
    public void setLocationId( int loc )
    throws IllegalArgumentException
    {
         if (loc < 0)
            throw new IllegalArgumentException("Illegal location id: " +
                                               loc);
        locationId = loc;
    }

    /**
     * Retrieves the mood id
     * @return  mood id
     */
    public int getMoodId()
    {
        return moodId;
    }

    /**
     * Sets the mood to an int > 0
     * @param mood  mood to set
     * @throws IllegalArgumentException  < 0
     */
    public void setMoodId( int mood )
    throws IllegalArgumentException
    {
         if (mood < 0)
            throw new IllegalArgumentException("Illegal mood id: " +
                                               mood);
        moodId = mood;
    }

    /**
     * Retrieve date as a <code>DateTimeBean</code>
     * @see DateTimeBean
     * @return  current date in a DateTimeBean
     */
    public DateTimeBean getDate()
    {
        return date;
    }

    /**
     * Set the date using a string in the form
     * 2004-01-30 22:02
     *
     * TODO: create a parser to check the date
     * more thoroughly.  DateTimeBean will throw
     * an exception if the format is wrong though!
     *
     * @see DateTimeBean
     * @param date
     * @throws IllegalArgumentException  null or len < 6
     */
    public void setDate( String date )
    throws IllegalArgumentException
    {
        if ( date == null )
            throw new IllegalArgumentException("Illegal date: null");

        if ( date.length() < 6 )
            throw new IllegalArgumentException("Illegal date: " +
                                               date);
        DateTimeBean newDate = new DateTimeBean();

        try {
            newDate.set(date);
            this.date = newDate;
        } catch (Exception e) {
            throw new IllegalArgumentException("Illegal date");
        }
    }


    /**
     * Set the date using a <code>DateTimeBean</code>
     * @see DateTimeBean
     * @param date
     */
    public void setDate( DateTimeBean date )
    {
        this.date = date;
    }

    /**
     * Retrieve the subject
     * @return
     */
    public String getSubject()
    {
        return subject;
    }

    /**
     * Set the subject.  If the subject is null or
     * an empty string, it will be set as (no subject).
     *
     * @param subject
     * @throws IllegalArgumentException
     */
    public void setSubject( String subject )
    throws IllegalArgumentException
    {
        /* i'm going to allow blanks
        if ( subject.length() < 2 )
            throw new IllegalArgumentException("Illegal subject: " +
                                               subject);
        */

        if ( subject == null || subject.length() == 0)
            this.subject = "(no subject)";
        else
            this.subject = subject;
    }


    /**
     *
     * @return
     */
    public String getBody()
    {
        return body;
    }

    public void setBody( String body )
    throws IllegalArgumentException
    {
        if ( body == null || body.length() < 2 )
            throw new IllegalArgumentException("Illegal body: " +
                                               body);

        this.body = body;
    }

    public String getMusic()
    {
        return music;
    }

    public void setMusic( String music )
    {
        if ( music == null )
            this.music = "";
        else
            this.music = music;
    }

    public int getCommentCount()
    {
        return commentCount;
    }

    public void setCommentCount( int comment )
    throws IllegalArgumentException
    {
         if (comment < 0)
            throw new IllegalArgumentException("Illegal comment count: " +
                                               comment);
        commentCount = comment;
    }

    public int getUserId()
    {
        return userId;
    }

    public void setUserId( int uid )
    throws IllegalArgumentException
    {
         if (uid < 0)
            throw new IllegalArgumentException("Illegal user id: " +
                                               uid);
        userId = uid;
    }

    public int getSecurityLevel()
    {
        return securityLevel;
    }

    public void setSecurityLevel( int sec )
    throws IllegalArgumentException
    {
         if (sec < 0)
            throw new IllegalArgumentException("Illegal security level: " +
                                               sec);
        securityLevel = sec;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName( String user )
    {
        if ( user == null)
            throw new IllegalArgumentException("Illegal User Name");

        userName = user;
    }

    public String getLocationName()
    {
        return locationName;
    }

    public void setLocationName( String loc )
    {
        if ( loc == null )
            throw new IllegalArgumentException("Illegal Location Name");

        locationName = loc;
    }

    public String getMoodName()
    {
        return moodName;
    }

    public void setMoodName( String mood )
    {
        if ( mood == null )
            throw new IllegalArgumentException("Illegal Mood Name");

        moodName = mood;
    }

    public boolean getAllowComments()
    {
        return this.allowComments;
    }

    public void setAllowComments( boolean allowComments )
    {
        this.allowComments = allowComments;
    }

    public boolean getEmailComments()
    {
        return this.emailComments;
    }

    public void setEmailComments( boolean emailComments )
    {
        this.emailComments = emailComments;
    }

    public boolean getAutoFormat()
    {
        return this.autoFormat;
    }

    public void setAutoFormat( boolean autoFormat )
    {
        this.autoFormat = autoFormat;
    }

    public String toString()
    {
        StringBuffer output = new StringBuffer();

        output.append("entry id: ");
        output.append(id);
        output.append('\n');

        output.append("location id: ");
        output.append(locationId);
        output.append('\n');

        output.append("location name: ");
        output.append(locationName);
        output.append('\n');

        output.append("mood id: ");
        output.append(moodId);
        output.append('\n');

        output.append("mood name: ");
        output.append(moodName);
        output.append('\n');

        output.append("comment count: ");
        output.append( commentCount );
        output.append('\n');

        output.append("date: ");
        output.append(date.toString());
        output.append('\n');

        output.append("subject: ");
        output.append( subject );
        output.append('\n');

        output.append("body: ");
        output.append(body);
        output.append('\n');

        output.append("music: ");
        output.append(music);
        output.append('\n');

        output.append("security level: ");
        output.append(securityLevel);
        output.append('\n');

        output.append("user id: ");
        output.append(userId);
        output.append('\n');

        output.append("autoformat: ");
        output.append(autoFormat);
        output.append('\n');

        output.append("allowComments: ");
        output.append(allowComments);
        output.append('\n');

        output.append("emailComments: ");
        output.append(emailComments);
        output.append('\n');

        return output.toString();
    }

}