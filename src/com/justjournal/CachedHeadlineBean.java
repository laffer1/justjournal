package com.justjournal;

import com.justjournal.db.RssCacheTo;
import com.justjournal.db.RssCacheDao;
import com.justjournal.db.DateTimeBean;

import java.net.URL;


/**
 * Stores RSS content collected from the internet into a datastore,
 * retrieves stored versions, and spits out HTML to render them.
 *
 * Created by IntelliJ IDEA.
 * User: laffer1
 * Date: Apr 27, 2005
 * Time: 8:15:45 PM
 */
public class CachedHeadlineBean
    extends HeadlineBean
{

    protected void getRssDocument( final String uri )
            throws Exception

    {

        RssCacheDao dao = new RssCacheDao();
        RssCacheTo rss;
        final java.util.GregorianCalendar calendarg = new java.util.GregorianCalendar();


        rss = dao.view( uri );

        if ( rss != null )
        {
            document = builder.parse( rss.getContent() );

            DateTimeBean dt = rss.getLastUpdated();

            if ( dt.getYear() != calendarg.get( java.util.Calendar.YEAR ) )
            {
                u = new URL( uri );
                inputXML = u.openStream();

                rss.setContent( inputXML.toString() );
                dao.update(rss);
            }

            if ( dt.getMonth() != calendarg.get( java.util.Calendar.MONTH) )
            {
                u = new URL( uri );
                inputXML = u.openStream();

                rss.setContent( inputXML.toString() );
                dao.update(rss);
            }

            if ( dt.getYear() != calendarg.get( java.util.Calendar.DATE ) )
            {
                u = new URL( uri );
                inputXML = u.openStream();

                rss.setContent( inputXML.toString() );
                dao.update(rss);
            }

        }
        else
        {
            String rssDoc;
            //Open the file for reading:
            u = new URL( uri );
            inputXML = u.openStream();
            rssDoc = inputXML.toString();

            builder = factory.newDocumentBuilder();
            document = builder.parse(rssDoc);

            try {
                rss.setUri( uri );
                rss.setInterval(24);
                dao.add(rss);
            } catch ( java.lang.NullPointerException n)
            {

            }

        }
    }


}
