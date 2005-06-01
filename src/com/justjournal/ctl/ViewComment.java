package com.justjournal.ctl;

import com.justjournal.db.CommentDao;
import com.justjournal.db.EntryDAO;
import com.justjournal.db.EntryTo;

import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: laffer1
 * Date: Dec 31, 2003
 * Time: 3:25:21 PM
 * To change this template use Options | File Templates.
 */
public class ViewComment extends ControllerAuth {

    protected EntryTo entry;
    protected Collection comments;
    protected int entryId;


    public EntryTo getEntry()
    {
        return this.entry;
    }

    public Collection getComments() {
        return this.comments;
    }


    public String getMyLogin() {
        return this.currentLoginName();
    }

    public int getEntryId() {
        return this.entryId;
    }

    public void setEntryId(int entryId) {
        this.entryId = entryId;
    }


    public String perform() throws Exception {
        CommentDao cdao = new CommentDao();
        EntryDAO edao = new EntryDAO();

        this.comments = cdao.view(entryId);
        this.entry = edao.viewSingle(entryId,false);

        return SUCCESS;
    }
}
