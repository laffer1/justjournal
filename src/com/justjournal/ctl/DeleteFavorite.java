package com.justjournal.ctl;

import com.justjournal.SQLHelper;
import org.apache.log4j.Category;

/**
 * Delete a favorite journal entry reference.
 * @author Lucas Holt
 * @version 1.0
 * @since 1.0
 * User: laffer1
 * Date: Dec 15, 2005
 * Time: 10:08:31 PM
 */
public class DeleteFavorite extends Protected {
    private static Category log = Category.getInstance(DeleteFavorite.class.getName());
    protected int entryId;

    /**
     * Retrieves the current logged in user.
     *
     * @return username as string
     */
    public String getMyLogin() {
        return this.currentLoginName();
    }

    /**
     * Retrieves the current entry id we are adding as a favorite
     *
     * @return favorite entry id
     */
    public int getEntryId() {
        return entryId;
    }

    /**
     * Sets the entry id we wish to make a favorite.
     *
     * @param entryId id to add to our favorites list
     */
    public void setEntryId(int entryId) {
        this.entryId = entryId;
    }

    protected String insidePerform() throws Exception {

        if (log.isDebugEnabled())
            log.debug("insidePerform(): Attempting to delete favorite");

        if (this.currentLoginId() < 1)
            addError("login", "The login timed out or is invalid.");

        if (!this.hasErrors()) {
            try {
                String sql = "call deletefavorite( " + this.currentLoginId() + "," + entryId + ");";
                int result = SQLHelper.executeNonQuery(sql);

                if (result != 1)
                    addError("Delete Favorite", "Error deleting your favorite.");
            }
            catch (Exception e) {
                addError("Delete Favorite", "Could not delete the favorite.");
                if (log.isDebugEnabled())
                    log.debug("insidePerform(): " + e.getMessage());
            }
        }

        if (this.hasErrors())
            return ERROR;
        else
            return SUCCESS;
    }
}
