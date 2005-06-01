package com.justjournal.db;

/**
 * Created by IntelliJ IDEA.
 * User: laffer1
 * Date: Dec 24, 2003
 * Time: 4:53:47 AM
 */
public class CommentTo {

    //id,uid,eid,date,subject,body
    private int id;
    private int eid;
    private int userId;

    private DateTimeBean date;
    private String subject;
    private String body;
    private String userName;

    public int getId() {
        return id;
    }

    public void setId(int id)
            throws IllegalArgumentException {
        if (id < 0)
            throw new IllegalArgumentException("Illegal id: " +
                    id);

        this.id = id;
    }

    public int getEid() {
        return eid;
    }

    public void setEid(int eid)
            throws IllegalArgumentException {
        if (eid < 0)
            throw new IllegalArgumentException("Illegal eid: " +
                    eid);

        this.eid = eid;
    }

    public DateTimeBean getDate() {
        return date;
    }

    public void setDate(String date)
            throws IllegalArgumentException {
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

    public void setDate( DateTimeBean date)
    {
        this.date = date;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject)
            throws IllegalArgumentException {

        /* Allow open subjects
        if (subject.length() < 2)
            throw new IllegalArgumentException("Illegal subject: " +
                    subject); */

        if ( subject.length() == 0)
            this.subject = "(no subject)";
        else
            this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body)
            throws IllegalArgumentException {
        if (body.length() < 2)
            throw new IllegalArgumentException("Illegal body: " +
                    body);

        this.body = body;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int uid)
            throws IllegalArgumentException {
        if (uid < 0)
            throw new IllegalArgumentException("Illegal user id: " +
                    uid);
        userId = uid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String user) {
        userName = user;
    }

    public String toString() {
        StringBuffer output = new StringBuffer();

        output.append("comment id: ");
        output.append(id);
        output.append('\n');

        output.append("entry id: ");
        output.append(eid);
        output.append('\n');

        output.append("date: ");
        output.append(date);
        output.append('\n');

        output.append("subject: ");
        output.append(subject);
        output.append('\n');

        output.append("body: ");
        output.append(body);
        output.append('\n');

        output.append("user id: ");
        output.append(userId);
        output.append('\n');

        return output.toString();
    }

}
