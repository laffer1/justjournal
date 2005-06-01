
package com.justjournal.db;

import sun.jdbc.rowset.CachedRowSet;
import com.justjournal.SQLHelper;

/**
 * @author Lucas Holt
 * User: laffer1
 * Date: Sep 22, 2003
 * Time: 11:01:27 PM
 */
public class EmoticonDao
{

    public EmoticonTo view( int themeId, int moodId )
    {
        int id = moodId;  // start at mood id but change as neccessary

        String sqlStatement;

        EmoticonTo et = new EmoticonTo();
        CachedRowSet rs = null;
        CachedRowSet rs2;
        boolean icon = false;
        String sqlStatement2 = "Select parentmood from mood WHERE id=\"";

           try {

                while ( !icon )
                {
                    sqlStatement = "SELECT picurl, width, height from mood_theme_data where moodid="
                        + id + " AND moodthemeid=" + themeId + ";";

                    rs = SQLHelper.executeResultSet( sqlStatement );

                    if ( rs.next() )
                    {
                        et.setMoodId(moodId);
                        et.setFileName(rs.getString("picurl") );
                        et.setMoodTheme(themeId);
                        et.setWidth( rs.getInt("width"));
                        et.setHeight( rs.getInt("height"));

                        icon = true;
                    } else {

                        if ( id == 0)
                            break;

                        rs2 = SQLHelper.executeResultSet( sqlStatement2 + id + "\";");

                        if ( rs2.next() )
                            id = rs2.getInt("parentmood");
                        else
                            break;

                        try {
                            rs2.close();
                        } catch (Exception e) {
                            //nothing to do.
                        }
                    }
                }

               rs.close();

            } catch ( Exception e1 ) {
                 if ( rs != null )
                 {
                    try {
                        rs.close();
                    } catch ( Exception e ) {
                    // NOTHING TO DO
                    }
                }
            }


        return et;
    }

}
