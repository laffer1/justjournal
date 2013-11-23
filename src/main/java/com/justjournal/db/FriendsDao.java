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

package com.justjournal.db;

import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;


/**
 * View Friends
 * @author Lucas Holt
 */
public final class FriendsDao {
    private static final Logger log = Logger.getLogger(FriendsDao.class.getName());

    public static Collection<FriendTo> view(final int userId) {
        ArrayList<FriendTo> friends = new ArrayList<FriendTo>(10);
        ResultSet RS;
        FriendTo fr;
        final String sqlStatement = "SELECT friends.friendid, user.username FROM friends, user WHERE friends.id='"
                + userId + "' AND friends.friendid=user.id;";

        try {
            RS = SQLHelper.executeResultSet(sqlStatement);

            while (RS.next()) {
                fr = new FriendTo();

                fr.setId(RS.getInt("friendid"));
                fr.setUserName(RS.getString("username"));
                fr.setOwnerId(userId);
                //fr.setOwnerUserName( RS.getString("owneruname"));

                friends.add(fr);
            }
            RS.close();
        } catch (Exception e1) {
            log.error(e1);
        }

        return friends;
    }

    public static boolean delete(FriendTo friend) {
        if (friend == null)
            throw new IllegalArgumentException("friend is invalid");
        if (friend.getOwnerId() < 1 || friend.getUserName() == null || friend.getUserName().isEmpty())
            throw new IllegalArgumentException("owner id not found");

        UserTo friendUser = UserDao.view(friend.getUserName());
        if (friendUser == null)
            return false;

        try {
            String sqlStatement = "Delete FROM friends where id ='" + friend.getOwnerId() + "' and friendid='" + friendUser.getId() + "' LIMIT 1;";
            int rowsAffected = SQLHelper.executeNonQuery(sqlStatement);
            if (rowsAffected == 1)
                return true;
        } catch (Exception e) {
            log.error(e);
        }
        return false;
    }
}
