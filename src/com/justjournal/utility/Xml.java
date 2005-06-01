
package com.justjournal.utility;

import com.justjournal.StringUtil;

/**
 * @author Lucas Holt
 * User: laffer1
 * Date: Sep 24, 2003
 * Time: 11:39:50 AM
 */
public final class Xml
{
    /**
     * converts characters that are special in xml
     * to their equivalents.
     * @param input
     * @return
     */
    public static String cleanString( String input )
    {
        String work = input;

        work = StringUtil.replace(work,'"', "&quot;");
        work = StringUtil.replace(work,'<',"&lt;");
        work = StringUtil.replace(work,'>', "&gt;");

        return work;
    }
}
