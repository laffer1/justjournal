/*
Copyright (c) 2003-2008, 2014 Lucas Holt
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

import com.justjournal.model.Entry;
import com.justjournal.model.Friends;
import com.justjournal.model.User;
import com.justjournal.utility.StringUtil;
import com.sun.istack.internal.NotNull;
import org.apache.cayenne.Cayenne;
import org.apache.cayenne.CayenneRuntimeException;
import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.access.DataContext;
import org.apache.cayenne.exp.Expression;
import org.apache.cayenne.query.Ordering;
import org.apache.cayenne.query.Query;
import org.apache.cayenne.query.SelectQuery;
import org.apache.cayenne.query.SortOrder;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.*;

import static java.util.Collections.singletonMap;


/**
 * Provides access to journal entries in the data tier.
 *
 * @author Lucas Holt
 * @see EntryTo
 */
@Component
public final class EntryDaoImpl implements EntryDao {
    private final static int MAX_ENTRIES = 20;
    private final static int MAX_FRIENDS = 15;
    private final static int TAG_MAX_LENGTH = 30;

    private static final Logger log = Logger.getLogger(EntryDaoImpl.class.getName());

    /**
     * Adds a journal entry to the data store.
     *
     * @param et journal entry object to be added
     * @return true if no error occured.
     */
    public static boolean add(final EntryTo et) {
        boolean noError = true;
        int records = 0;
        String allowComments = "Y";
        String emailComments = "Y";
        String autoFormat = "Y";

        if (!et.getAllowComments())
            allowComments = "N";

        if (!et.getEmailComments())
            emailComments = "N";

        if (!et.getAutoFormat())
            autoFormat = "N";

        final String sqlStmt =
                "INSERT INTO entry (id,uid,date,subject,mood,music,location,body,security,allow_comments,email_comments,autoformat) values(NULL,'" + et.getUserId() + "','" + et.getDateTime().toString() + "','" + et.getSubject() + "','" + et.getMoodId() + "','" + et.getMusic() + "','" + et.getLocationId() + "','" + et.getBody() + "','" + et.getSecurityLevel() + "','" + allowComments + "','" + emailComments + "','" + autoFormat + "');";

        try {
            records = SQLHelper.executeNonQuery(sqlStmt);
        } catch (Exception e) {
            noError = false;
            log.error(e);
            log.trace(sqlStmt);
        }

        if (records != 1)
            noError = false;

        return noError;
    }

    /**
     * Update a journal entry with different information.  User can alter date, subject, body, security, mood, music,
     * and location.
     *
     * @param et journal entry that needs to be altered.
     * @return true if no error occured.
     */
    public static boolean update(final EntryTo et) {
        boolean noError = true;
        int records = 0;
        String allowComments = "Y";
        String emailComments = "Y";
        String autoFormat = "Y";

        if (et.getId() > 0) {
            if (!et.getAllowComments())
                allowComments = "N";

            if (!et.getEmailComments())
                emailComments = "N";

            if (!et.getAutoFormat())
                autoFormat = "N";

            final String sqlStmt =
                    "Update entry SET date='" + et.getDate().toString() + "', subject='" + et.getSubject() + "', body='" + et.getBody() + "', security='" + et.getSecurityLevel() + "', location='" + et.getLocationId() + "', mood='" + et.getMoodId() + "', music='" + et.getMusic() + "', allow_comments='" + allowComments + "', email_comments='" + emailComments + "', autoformat='" + autoFormat + "' WHERE id='" + et.getId() + "' LIMIT 1;";

            try {
                records = SQLHelper.executeNonQuery(sqlStmt);
            } catch (Exception e) {
                noError = false;
                log.error(e);
                log.trace(sqlStmt);
            }

            if (records != 1)
                noError = false;
        } else {
            noError = false;
        }

        return noError;
    }

    public static boolean delete(final int entryId, final int userId) {
        boolean noError = true;
        int records = 0;

        final String sqlStmt = "DELETE FROM entry WHERE id='" + entryId + "' AND uid='" + userId + "' LIMIT 1;";

        if (entryId > 0 && userId > 0) {
            try {
                records = SQLHelper.executeNonQuery(sqlStmt);
            } catch (Exception e) {
                noError = false;
                log.error(e);
                log.trace(sqlStmt);
            }
        } else {
            noError = false;
        }

        if (records != 1)
            noError = false;

        return noError;
    }

    /**
     * Get single entry using userId as the only security check
     *
     * @param entryId unique entry id
     * @param userId  unique user id
     * @return Entry Transfer Object
     */
    @NotNull
    public EntryTo viewSingle(final int entryId, final int userId) {
        EntryTo et = viewSingle(entryId);

        if (et != null && et.getUserId() == userId)
            return et;
        else
            return new EntryImpl();
    }

    /**
     * Create an EntryTo object from the contents of a database entry
     *
     * @param entry Cayenne Data Object for an entry
     * @return EntryTo
     */
    private EntryTo populateEntryTo(com.justjournal.model.Entry entry) {
        EntryTo et = new EntryImpl();

        if (entry != null) {
            com.justjournal.model.User user = entry.getEntryToUser();
            com.justjournal.model.Mood mood = entry.getEntryToMood();
            com.justjournal.model.EntrySecurity security = entry.getEntryToSecurity();
            com.justjournal.model.Location location = entry.getEntryToLocation();
            int entryId = Cayenne.intPKForObject(entry);

            et.setUserName(user.getUsername());

            et.setId(entryId);
            et.setUserId(Cayenne.intPKForObject(user));
            et.setDate(new DateTimeBean(entry.getDate()));
            et.setSubject(entry.getSubject());
            et.setBody(entry.getBody());
            et.setLocationId(Cayenne.intPKForObject(location));
            et.setMoodId(Cayenne.intPKForObject(mood));
            et.setMusic(entry.getMusic());
            et.setSecurityLevel(Cayenne.intPKForObject(security));
            et.setMoodName(mood.getTitle());
            et.setLocationName(location.getTitle());

            if (entry.getEmailComments().compareTo("Y") == 0)
                et.setEmailComments(true);
            else
                et.setEmailComments(false);

            if (entry.getAllowComments().compareTo("Y") == 0)
                et.setAllowComments(true);
            else
                et.setAllowComments(false);

            if (entry.getAutoformat().compareTo("Y") == 0)
                et.setAutoFormat(true);
            else
                et.setAutoFormat(false);

            et.setTags(getTags(entryId));
            et.setCommentCount(entry.getEntryToComments().size());

        }
        return et;
    }

    public boolean exists(final int entryId) {
        try {
            ObjectContext dataContext = DataContext.getThreadObjectContext();

            com.justjournal.model.Entry entry =
                    Cayenne.objectForPK(dataContext, com.justjournal.model.Entry.class, entryId);
            if (entry != null) return true;
        } catch (Exception e1) {
            log.error(e1);
        }
        return false;
    }

    /**
     * Get single entry with no security in place
     *
     * @param entryId unique id for an entry
     * @return Entry Transfer Object
     */
    public EntryTo viewSingle(final int entryId) {
        EntryTo et;

        try {
            ObjectContext dataContext = DataContext.getThreadObjectContext();

            com.justjournal.model.Entry entry =
                    Cayenne.objectForPK(dataContext, com.justjournal.model.Entry.class, entryId);
            et = populateEntryTo(entry);

        } catch (Exception e1) {
            et = new EntryImpl();
            log.error(e1);
        }

        return et;
    }

    /**
     * Get single entry if it is public
     *
     * @param entryId unique id for an entry
     * @return Entry Transfer Object
     */
    public EntryTo viewSinglePublic(final int entryId) {
        EntryTo et = viewSingle(entryId);
        if (et.getSecurityLevel() == 2)
            return et;
        else
            return new EntryImpl();
    }

    public EntryTo viewSingle(final EntryTo ets) {
        log.debug("viewSingle() starting with EntryTo input");

        final EntryTo et = new EntryImpl();
        ResultSet rs = null;
        ResultSet rsComment = null;
        String sqlStmt2;
        final StringBuilder sqlStatement = new StringBuilder();

        sqlStatement.append("SELECT us.id As id, us.username, eh.date As date, eh.subject As subject, eh.music, eh.body, ");
        sqlStatement.append("mood.title As moodt, location.title As location, eh.id As entryid, ");
        sqlStatement.append("eh.security as security, eh.autoformat, eh.allow_comments, eh.email_comments, location.id as locationid, mood.id as moodid ");
        sqlStatement.append("FROM user As us, entry As eh, mood, location WHERE us.id='").append(ets.getUserId()).append("' AND eh.date='");
        sqlStatement.append(ets.getDate());
        // body .append("' AND eh.body='").append(ets.getBody());
        sqlStatement.append("' AND us.id=eh.uid AND mood.id=eh.mood AND location.id=eh.location ORDER BY eh.date DESC, eh.id DESC LIMIT 1;");
        log.debug(sqlStatement.toString());

        try {
            rs = SQLHelper.executeResultSet(sqlStatement.toString());

            if (rs.next()) {

                et.setUserName(rs.getString("username"));

                et.setId(rs.getInt("entryid"));
                et.setUserId(rs.getInt("id"));
                et.setDate(rs.getString("date"));
                et.setSubject(rs.getString("subject"));
                et.setBody(rs.getString("body"));
                et.setLocationId(rs.getInt("locationid"));
                et.setMoodId(rs.getInt("moodid"));
                et.setMusic(rs.getString("music"));
                et.setSecurityLevel(rs.getInt("security"));
                et.setMoodName(rs.getString("moodt"));
                et.setLocationName(rs.getString("location"));

                if (rs.getString("email_comments").compareTo("Y") == 0)
                    et.setEmailComments(true);
                else
                    et.setEmailComments(false);

                if (rs.getString("allow_comments").compareTo("Y") == 0)
                    et.setAllowComments(true);
                else
                    et.setAllowComments(false);

                if (rs.getString("autoformat").compareTo("Y") == 0)
                    et.setAutoFormat(true);
                else
                    et.setAutoFormat(false);

                et.setTags(getTags(rs.getInt("entryid")));

                try {
                    sqlStmt2 = "SELECT count(comments.id) As comid FROM comments WHERE eid='" + rs.getString("entryid") + "';";
                    rsComment = SQLHelper.executeResultSet(sqlStmt2);
                    if (rsComment.next()) {
                        if (rsComment.getInt("comid") > 0)
                            et.setCommentCount(rsComment.getInt("comid"));
                    }

                    rsComment.close();
                } catch (Exception ex) {
                    if (rsComment != null) {
                        try {
                            rsComment.close();
                        } catch (Exception e) {
                            // NOTHING TO DO
                        }
                    }
                }

            }

            rs.close();

        } catch (Exception e1) {
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e) {
                    // NOTHING TO DO
                }
            }
        }


        return et;
    }


    /**
     * get retrieves journal entries from the database.
     *
     * @param userName user who owns the entries.
     * @param thisUser is the owner the one accessing the data?
     * @return A <code>Collection</code> of entries.
     */
    public List<EntryTo> view(final String userName, final boolean thisUser) {
        return view(userName, thisUser, 0); // don't skip any!
    }

    /**
     * get retrieves journal entries from the database.
     *
     * @param userName user who owns the entries.
     * @param thisUser is the owner the one accessing the data?
     * @param skip     number of records to skip (in the past)
     * @return A <code>Collection</code> of entries.
     */
    @SuppressWarnings("unchecked")
    public List<EntryTo> view(final String userName, final boolean thisUser, final int skip) {
        final List<EntryTo> entries = new ArrayList<EntryTo>(MAX_ENTRIES);

        ObjectContext dataContext = DataContext.getThreadObjectContext();

        EntryTo et;
        final int PAGE_SIZE = 20;

        Expression exp;
        if (thisUser) {
            exp = Expression.fromString("entryToUser.username = $user");
        } else {
            exp = Expression.fromString("entryToUser.username = $user and entryToSecurity=2");
        }

        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("user", userName);
            exp = exp.expWithParameters(map);
            SelectQuery query = new SelectQuery(com.justjournal.model.Entry.class, exp);
            query.setPageSize(PAGE_SIZE);
            List<Ordering> orderings = new ArrayList<Ordering>();
            orderings.add(new Ordering("date", SortOrder.DESCENDING));
            orderings.add(new Ordering("db:" + Entry.ID_PK_COLUMN, SortOrder.DESCENDING));
            query.addOrderings(orderings);
            List<Entry> entryList = dataContext.performQuery(query);

            int x = 0;
            for (int i = skip; i < entryList.size(); i++) {
                et = populateEntryTo(entryList.get(i));
                entries.add(et);
                x++;
                if (x == PAGE_SIZE) break;
            }
        } catch (Exception e1) {
            log.error(e1);
        }

        return entries;
    }

    /**
     * viewAll retrieves journal entries from the database.
     *
     * @param userName user who own's the entries.
     * @param thisUser is the owner the one accessing the data
     * @return A <code>Collection</code> of entries.
     */
    @SuppressWarnings("unchecked")
    public List<EntryTo> viewAll(final String userName, final boolean thisUser) {
        final List<EntryTo> entries = new ArrayList<EntryTo>(MAX_ENTRIES);

        ObjectContext dataContext = DataContext.getThreadObjectContext();

        EntryTo et;
        Expression exp;
        if (thisUser) {
            exp = Expression.fromString("entryToUser.username = $user");
        } else {
            exp = Expression.fromString("entryToUser.username = $user and entryToSecurity=2");
        }

        try {
            exp = exp.expWithParameters(singletonMap("user", userName));
            SelectQuery query = new SelectQuery(com.justjournal.model.Entry.class, exp);
            List<Ordering> orderings = new ArrayList<Ordering>();
            orderings.add(new Ordering("date", SortOrder.DESCENDING));
            orderings.add(new Ordering("db:" + Entry.ID_PK_COLUMN, SortOrder.DESCENDING));
            query.addOrderings(orderings);
            List<com.justjournal.model.Entry> entryList = dataContext.performQuery(query);

            for (com.justjournal.model.Entry e : entryList) {
                et = populateEntryTo(e);
                entries.add(et);
            }
        } catch (Exception e1) {
            log.error(e1);
        }

        return entries;
    }

    /**
     * Generate friends list entries
     *
     * @param userID  blog owner user id
     * @param aUserId authenticated user id
     * @return blog entries for friends of the specified user
     * @throws IllegalArgumentException bad input
     */
    @SuppressWarnings("unchecked")
    public List<EntryTo> viewFriends(final int userID, final int aUserId)
            throws IllegalArgumentException {

        if (userID < 1)
            throw new IllegalArgumentException("userID must be greater than zero");

        final List<EntryTo> entries = new ArrayList<EntryTo>(MAX_FRIENDS);

        ObjectContext dataContext = DataContext.getThreadObjectContext();

        EntryTo et;
        final int PAGE_SIZE = 15;
        Map<String, Object> map = new HashMap<String, Object>();

        Expression exp;
        if (aUserId > 0 && (userID == aUserId)) {
            Collection<User> myFriends = new ArrayList<com.justjournal.model.User>();
            try {
                // Get the list of friends for this user id
                Expression e = Expression.fromString("FriendsToUser = $uid");
                e.expWithParameters(Collections.singletonMap("uid", userID));
                Query fq = new SelectQuery(com.justjournal.model.Friends.class, e);
                List<com.justjournal.model.Friends> friends = dataContext.performQuery(fq);

                for (com.justjournal.model.Friends f : friends) {
                    // check if the friend has a reverse relationship friends.id = friends.friendid
                    com.justjournal.model.User user_friendid = f.getFriendsToFriendUser();
                    List<Friends> f2list = user_friendid.getUserToFriends(); // Friend's list of friends
                    for (Friends f2 : f2list) {
                        if (f2.getFriendsToFriendUser().getUsername().equals(
                                Cayenne.objectForPK(dataContext, com.justjournal.model.User.class, userID).getUsername())) {
                            myFriends.add(f.getFriendsToFriendUser());
                        }
                    }
                }
            } catch (CayenneRuntimeException ce) {
                log.error(ce);
            }
            exp = Expression.fromString("entryToUser.userToFriends = $id and (entryToSecurity=2 or (entryToSecurity=1 and entryToUser.FriendUserToFriends = $friends))");
            map.put("friends", myFriends);
        } else if (aUserId >= 0)
            // no user logged in or another user's friends page.. just spit out public entries.
            exp = Expression.fromString("entryToUser=$id and entryToUser.userToFriends.id = $id and entryToSecurity=2");
        else
            throw new IllegalArgumentException("aUserId must be greater than -1");

        try {
            map.put("id", userID);
            exp = exp.expWithParameters(map);
            SelectQuery query = new SelectQuery(com.justjournal.model.Entry.class, exp);
            query.setPageSize(PAGE_SIZE);
            List<Ordering> orderings = new ArrayList<Ordering>();
            orderings.add(new Ordering("date", SortOrder.DESCENDING));
            orderings.add(new Ordering("db:" + Entry.ID_PK_COLUMN, SortOrder.DESCENDING));
            query.addOrderings(orderings);
            List<Entry> entryList = dataContext.performQuery(query);

            int x = 0;
            for (Entry anEntryList : entryList) {
                et = populateEntryTo(anEntryList);
                entries.add(et);
                x++;
                if (x == PAGE_SIZE) break;
            }
        } catch (Exception e1) {
            log.error(e1);
        }

        return entries;

        /* First get a list of friends
        TODO: FIX INFORMATION DISCLOSURE
        instead do 1:1 on user:auth user
        */
               /*
        if (aUserId > 0 && (userID == aUserId))
            sqlStatement =
                    new StringBuilder().append("SELECT friends.friendid As id, us.username, eh.date as date, eh.subject as subject, eh.music, " +
                            "eh.body, eh.security, eh.autoformat, eh.allow_comments, eh.email_comments, mood.title as mood, " +
                            "mood.id as moodid, location.id as locid, location.title as location, eh.id As entryid" +
                            " FROM user as us, entry as eh, mood, location, friends WHERE friends.id='").append(userID).
                            append("' AND friends.friendid = eh.uid AND mood.id=eh.mood AND location.id=eh.location " +
                                    "AND friends.friendid=us.id AND " +
                                    "(eh.security=2 OR (eh.security=1 AND friends.friendid IN (SELECT f1.friendid FROM friends as f1 INNER JOIN " +
                                    "friends as f2 ON f1.id=f2.friendid AND f1.friendid=f2.id WHERE f1.id='").append(userID).append("'))) ORDER by eh.date DESC LIMIT 0,15;").toString();
           */
    }

    /**
     * Count the number of blog entries for a given year and user.
     *
     * @param year     The year
     * @param userName The user
     * @return integer count of entries.
     * @throws Exception Database exception
     */
    public int calendarCount(final int year, final String userName)
            throws Exception {

        String sqlStatement;
        ResultSet RS;
        int count = 0;

        sqlStatement = "SELECT count(*) " +
                " FROM user As us, entry As eh " +
                " WHERE us.username = '" + userName +
                "' AND YEAR(eh.date)=" + year + " AND us.id = eh.uid;";

        RS = SQLHelper.executeResultSet(sqlStatement);

        if (RS.next())
            count = RS.getInt(1);
        RS.close();

        return count;
    }

    /**
     * Count the number of blog entries this user has made.
     *
     * @param userName user to get blog entries for
     * @return number of entries
     * @throws Exception data access
     */
    @NotNull
    public int entryCount(@NotNull final String userName) throws Exception {
        String sqlStatement;
        ResultSet RS;
        int count = 0;

        if (userName == null || userName.isEmpty()) return count;

        sqlStatement = "SELECT count(*) as cnt" +
                " FROM user As us, entry As eh " +
                " WHERE us.username = '" + userName +
                "' AND us.id = eh.uid;";

        RS = SQLHelper.executeResultSet(sqlStatement);

        if (RS.next())
            count = RS.getInt("cnt");
        RS.close();

        return count;
    }

    @NotNull
    public Collection<EntryTo> viewCalendarYear(final int year,
                                                final String userName,
                                                final boolean thisUser)
            throws Exception {

        DateTimeBean dtb = new DateTimeBean();
        dtb.setYear(year);
        dtb.setMonth(1);
        dtb.setDay(1);
        dtb.setHour(0);
        dtb.setMinutes(1);
        Date startDate = dtb.toDate();

        dtb.add(Calendar.YEAR, 1);
        dtb.add(Calendar.MINUTE, -1);
        Date endDate = dtb.toDate();
        return getEntriesByDateRange(startDate, endDate, userName, thisUser);
    }

    @NotNull
    public Collection<EntryTo> viewCalendarMonth(final int year,
                                                 final int month,
                                                 final String userName,
                                                 final boolean thisUser)
            throws Exception {

        DateTimeBean dtb = new DateTimeBean();
        dtb.setYear(year);
        dtb.setMonth(month);
        dtb.setDay(1);
        dtb.setHour(0);
        dtb.setMinutes(1);
        Date startDate = dtb.toDate();

        dtb.add(Calendar.MONTH, 1);
        dtb.add(Calendar.MINUTE, -1);
        Date endDate = dtb.toDate();
        return getEntriesByDateRange(startDate, endDate, userName, thisUser);
    }

    /**
     * Get the list of all entries for a single day and user.
     *
     * @param year     year of the entry
     * @param month    month of the year
     * @param day      day of the week
     * @param userName the blog user
     * @param thisUser include private and friends entries
     * @return returns entries or an empty list
     */
    @NotNull
    @SuppressWarnings("unchecked")
    public Collection<EntryTo> viewCalendarDay(final int year,
                                               final int month,
                                               final int day,
                                               final String userName,
                                               final boolean thisUser) {

        DateTime dtb = new DateTimeBean();
        dtb.setYear(year);
        dtb.setMonth(month);
        dtb.setDay(day);
        dtb.setHour(0);
        dtb.setMinutes(1);
        Date startDate = dtb.toDate();
        dtb.setHour(23);
        dtb.setMinutes(59);
        Date endDate = dtb.toDate();

        return getEntriesByDateRange(startDate, endDate, userName, thisUser);
    }

    @NotNull
    public Collection<EntryTo> getEntriesByDateRange(Date startDate, Date endDate,
                                                     final String userName,
                                                     final boolean thisUser) {

        final Collection<EntryTo> entries = new ArrayList<EntryTo>();

        ObjectContext dataContext = DataContext.getThreadObjectContext();

        EntryTo et;
        Expression exp;
        if (thisUser) {
            exp = Expression.fromString("entryToUser.username = $user and date < $ed and date > $sd");
        } else {
            // public security
            exp = Expression.fromString("entryToUser.username = $user and entryToSecurity=2 and date < $ed and date > $sd");
        }

        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("sd", startDate);
            map.put("ed", endDate);
            map.put("user", userName);
            exp = exp.expWithParameters(map);
            SelectQuery query = new SelectQuery(com.justjournal.model.Entry.class, exp);
            List<Ordering> orderings = new ArrayList<Ordering>();
            orderings.add(new Ordering("date", SortOrder.DESCENDING));
            orderings.add(new Ordering("db:" + Entry.ID_PK_COLUMN, SortOrder.DESCENDING));
            query.addOrderings(orderings);
            @SuppressWarnings("unchecked")
            List<com.justjournal.model.Entry> entryList = dataContext.performQuery(query);

            for (com.justjournal.model.Entry e : entryList) {
                et = populateEntryTo(e);
                entries.add(et);
            }
        } catch (Exception e1) {
            log.error(e1);
        }

        return entries;
    }

    /**
     * Retrieve the 15 most recent journal entries from unique users.  So if I post 10 entries, it will only select the
     * most recent entry and the entries of 14 other users.  (another words 1 per user)
     *
     * @return Entries from 15 different users (most recent)
     */
    @SuppressWarnings("unchecked")
    public
    @NotNull
    Collection<EntryTo> viewRecentUniqueUsers() {

        final int SIZE = 15;
        final Collection<EntryTo> entries = new ArrayList<EntryTo>(SIZE);

        ObjectContext dataContext = DataContext.getThreadObjectContext();

        EntryTo et;
        Expression exp;
        exp = Expression.fromString("entryToSecurity=2");
        Map<String, Boolean> seenUser = new HashMap<String, Boolean>(20);

        try {
            SelectQuery query = new SelectQuery(com.justjournal.model.Entry.class, exp);
            List<Ordering> orderings = new ArrayList<Ordering>();
            orderings.add(new Ordering("date", SortOrder.DESCENDING));
            orderings.add(new Ordering("db:" + Entry.ID_PK_COLUMN, SortOrder.DESCENDING));
            query.addOrderings(orderings);
            query.setPageSize(SIZE * 3);
            List<com.justjournal.model.Entry> entryList = dataContext.performQuery(query);
            log.error("viewRecentUniqueUsers " + entryList.size());
            int done = 0;
            for (Entry e : entryList) {
                String userName = e.getEntryToUser().getUsername();
                if (seenUser.containsKey(userName)) {
                    log.error("seen username " + userName);
                    continue;
                }
                seenUser.put(userName, true);
                et = populateEntryTo(e);
                entries.add(et);
                done++;
                log.error("Done: " + done);
                if (done > SIZE)
                    break;
            }
        } catch (Exception e1) {
            log.error(e1);
        }

        return entries;
    }


    /**
     * Retrieve the tag names for a given blog entry
     *
     * @param entryId The unique identifier for an entry
     * @return An arraylist of tag names
     */
    public
    @NotNull
    ArrayList<String> getTags(int entryId) {
        final ArrayList<String> tags = new ArrayList<String>();

        String sqlStatement;
        ResultSet rs = null;

        // PUBLIC ONLY
        sqlStatement = " SELECT tags.name as name FROM tags, entry_tags WHERE entry_tags.entryid='" + entryId
                + "' AND tags.id = entry_tags.tagid;";

        try {
            rs = SQLHelper.executeResultSet(sqlStatement);

            while (rs.next()) {
                tags.add(rs.getString("name"));
            }

            rs.close();

        } catch (Exception e1) {
            log.error(e1.getMessage() + "\n" + e1.toString());

            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e) {
                    // NOTHING TO DO
                }
            }
        }
        return tags;
    }


    /**
     * Add and delete tags for blog entries. TODO: Finish implementation
     *
     * @param entryId The entry the tags should associate with
     * @param tags    An arralist of tags
     * @return true on success, false otherwise
     */
    public boolean setTags(int entryId, Iterable tags) {

        if (entryId < 1)
            throw new IllegalArgumentException("Entry id must be greater than 0");

        boolean noError = true;
        Iterable<String> current = getTags(entryId);
        AbstractList<String> newTags = new ArrayList<String>();
        AbstractList<String> deadTags = new ArrayList<String>();

        // delete test
        for (String curtag : current) {
            boolean inlist = false;
            // Compare the current list with the "new" list of tags.
            for (final Object tag : tags) {
                String newtag = (String) tag;
                if (curtag.equalsIgnoreCase(newtag)) {
                    inlist = true;
                    break; // in both lists so we can skip it.
                }
            }

            // The tag is in the database, but not in the new list so it must be deleted.
            if (!inlist)
                deadTags.add(curtag);
        }

        // add test
        for (final Object tag : tags) {
            String newtag = (String) tag; // get the tag
            boolean inlist = false;
            // Compare the current list with the "new" list of tags.
            for (String curtag : current) {
                if (newtag.equalsIgnoreCase(curtag)) {
                    inlist = true;
                    break; // in both lists so we can skip it.
                }
            }

            // The tag is in the new list, but not in the database so it must be added.
            if (!inlist)
                newTags.add(newtag);
        }

        try {
            // add new tags
            for (String newt : newTags) {
                int tagid = getTagId(newt);
                if (tagid < 1) {
                    String sql = "INSERT INTO tags (name) VALUES('" + newt + "');";
                    SQLHelper.executeNonQuery(sql);
                    tagid = getTagId(newt);
                }
                String sql2 = "INSERT INTO entry_tags (entryid, tagid) VALUES('" + entryId + "','" + tagid + "');";
                SQLHelper.executeNonQuery(sql2);
            }

            // delete old tags
            for (String dele : deadTags) {
                int tagid = getTagId(dele);
                String sql2 = "DELETE FROM entry_tags where entryid='" + entryId + "' and tagid='" + tagid + "' LIMIT 1;";
                SQLHelper.executeNonQuery(sql2);
            }

        } catch (Exception e) {
            noError = false;
        }

        return noError;
    }


    /**
     * Find the tag id that matches the name
     *
     * @param tagname The "name" of the tag
     * @return tag id
     */
    public int getTagId(String tagname) {
        int tagid = 0;
        String sqlStatement;
        ResultSet rs = null;

        if (tagname == null || tagname.equalsIgnoreCase(""))
            throw new IllegalArgumentException("Name must be set");
        if (tagname.length() > TAG_MAX_LENGTH)
            throw new IllegalArgumentException("Name cannot be longer than 30 characters.");
        if (!StringUtil.isAlpha(tagname))
            throw new IllegalArgumentException("Name contains invalid characters.  Must be A-Za-z");

        // PUBLIC ONLY
        sqlStatement = " SELECT id FROM tags WHERE name='" + tagname.toLowerCase() + "';";

        try {
            rs = SQLHelper.executeResultSet(sqlStatement);

            if (rs.next()) {
                tagid = rs.getInt("id");
            }

            rs.close();

        } catch (Exception e1) {
            log.error(e1.getMessage() + "\n" + e1.toString());

            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e) {
                    // NOTHING TO DO
                }
            }
        }

        return tagid;
    }

    /**
     * Retrieve the list of tags the user has used on blog entries at this point in time.
     *
     * @param userId A userid to lookup
     * @return a list of tags
     */
    public
    @NotNull
    ArrayList<Tag> getUserTags(int userId) {
        String sqlStatement;
        ResultSet rs = null;
        ResultSet rs2;
        ArrayList<Tag> tags = new ArrayList<Tag>();

        // PUBLIC ONLY
        sqlStatement = "SELECT DISTINCT tags.id As id, tags.name As name FROM tags, entry_tags, entry WHERE entry.uid='" + userId
                + "' AND tags.id = entry_tags.tagid AND entry.id = entry_tags.entryid;";

        try {
            rs = SQLHelper.executeResultSet(sqlStatement);
            while (rs.next()) {
                Tag t = new TagImpl(rs.getInt("id"), rs.getString("name"));
                try {
                    rs2 = SQLHelper.executeResultSet("SELECT count(*) FROM tags, entry_tags, entry WHERE entry.uid='" + userId + "' AND tags.id='" + rs.getInt("id") + "' AND tags.id = entry_tags.tagid AND entry.id = entry_tags.entryid;");
                    if (rs2.next())
                        t.setCount(rs2.getInt(1));
                    rs2.close();
                } catch (Exception e) {
                    log.error("Can't get tag count: " + e.getMessage());
                }
                tags.add(t);
            }

            rs.close();

        } catch (Exception e1) {
            log.error(e1.getMessage() + "\n" + e1.toString());

            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e) {
                    // NOTHING TO DO
                }
            }
        }

        return tags;
    }
}

