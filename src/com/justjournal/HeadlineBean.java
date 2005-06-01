
package com.justjournal;

import com.justjournal.utility.Xml;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.net.URL;

/**
 * Retrieves a RSS document using HTTP, parses the document, and
 * converts it to HTML.
 * @author Lucas Holt
 * @version 1.3
 * @since 1.0
 * User: laffer1
 * Date: Jul 22, 2003
 * Time: 12:19:17
 *
 * 1.3 now supports several RSS 2 features (non rdf format)
 * 1.2 added several properties to the output including
 *     the published date, and description.
 * 1.1 optimized code
 * 1.0 Initial release
 */

public class HeadlineBean
{

    // constants
    private static final char endl = '\n';

    // vars
    protected URL u;
    protected InputStream inputXML;
    protected DocumentBuilder builder;
    protected Document document;
    private StringBuffer sb;

    final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

    private void getRssDocument( final String uri )
            throws Exception
    {
        //Open the file for reading:
        u = new URL( uri );
        inputXML = u.openStream();
        //Build document:
        builder = factory.newDocumentBuilder();
        document = builder.parse( inputXML );
    }

    public String parse( final String url )
    {
        try {
            getRssDocument( url );

            // output variable
            sb = new StringBuffer();

            String contentTitle = "";
            String contentLink = "";
            String contentDescription = "";
            String contentLastBuildDate = "";
            String contentGenerator = "";
            org.w3c.dom.NodeList channelList = document.getElementsByTagName( "channel" );
            org.w3c.dom.NodeList chnodes = channelList.item( 0 ).getChildNodes();

            String imageUrl = null;
            String imageTitle = null;
            String imageLink = "";
            String imageWidth = null;
            String imageHeight = null;

            for ( int k = 0; k < chnodes.getLength(); k++ ) {
                org.w3c.dom.Node curNode = chnodes.item( k );

                if ( curNode.getNodeName().equals( "title" ) ) {
                    contentTitle = curNode.getChildNodes().item( 0 ).getNodeValue();
                } else if ( curNode.getNodeName().equals( "link" ) ) {
                    contentLink = curNode.getChildNodes().item( 0 ).getNodeValue();
                } else if ( curNode.getNodeName().equals( "description" ) ) {
                    contentDescription = curNode.getChildNodes().item( 0 ).getNodeValue();
                } else if ( curNode.getNodeName().equals( "lastBuildDate" ) ) {
                    contentLastBuildDate = curNode.getChildNodes().item( 0 ).getNodeValue();
                } else if ( curNode.getNodeName().equals( "generator" ) ) {
                    contentGenerator = curNode.getChildNodes().item( 0 ).getNodeValue();
                } else if ( curNode.getNodeName().equals( "image" ) ) {

                    org.w3c.dom.NodeList imageNodes = curNode.getChildNodes();

                    for ( int z = 0; z < imageNodes.getLength(); z++ ) {
                        if ( imageNodes.item( z ).getNodeName().equals( "url" ) ) {
                            imageUrl = imageNodes.item( z ).getChildNodes().item( 0 ).getNodeValue();
                        } else if ( imageNodes.item( z ).getNodeName().equals( "height" ) ) {
                            imageHeight = imageNodes.item( z ).getChildNodes().item( 0 ).getNodeValue();
                        } else if ( imageNodes.item( z ).getNodeName().equals( "width" ) ) {
                            imageWidth = imageNodes.item( z ).getChildNodes().item( 0 ).getNodeValue();
                        } else if ( imageNodes.item( z ).getNodeName().equals( "title" ) ) {
                            imageTitle = imageNodes.item( z ).getChildNodes().item( 0 ).getNodeValue();
                        } else if ( imageNodes.item( z ).getNodeName().equals( "link" ) ) {
                            imageLink = imageNodes.item( z ).getChildNodes().item( 0 ).getNodeValue();
                        }
                    }
                }
            }

            // create header!
            sb.append( "<div style=\"width: 100%; padding: .1in; background: #F2F2F2;\" class=\"ljfhead\">" );

            sb.append( "<!-- Generator: " );
            sb.append( contentGenerator );
            sb.append( " -->" );

            if ( imageUrl != null ) {
                sb.append( "<span style=\"padding: 5px; float:left; width:" );
                sb.append( imageWidth );
                sb.append( "px; height:" );
                sb.append( imageHeight );
                sb.append( "px; position: relative;\">" );

                sb.append( "<a href=\"" );
                sb.append( imageLink );
                sb.append( "\">" );

                sb.append( "<img src=\"" );
                sb.append( imageUrl );
                sb.append( "\" height=\"" );
                sb.append( imageHeight );
                sb.append( "\" width=\"" );
                sb.append( imageWidth );
                sb.append( "\" alt=\"" );
                sb.append( imageTitle );
                sb.append( "\" /></a></span>" );
            }

            sb.append( "<h3>" );
            sb.append( contentTitle );
            sb.append( "</h3>" );

            sb.append( "<p>last build date: " );
            sb.append( contentLastBuildDate );

            sb.append("<br /><a href=\"" + contentLink + "\">source</a></p>");

            sb.append( "<div style=\"clear: both;\">&nbsp;</div>" );

            sb.append( "</div>" );

            //Generate the NodeList;
            org.w3c.dom.NodeList nodeList = document.getElementsByTagName( "item" );

            sb.append( "<ul>" );
            sb.append( endl );

            for ( int i = 0; i < nodeList.getLength(); i++ ) {
                org.w3c.dom.NodeList childList = nodeList.item( i ).getChildNodes();
                // some of the properties of <items>
                String link = null;
                String title = null;
                String pubDate = null;
                String description = null;
                String guid = null;

                for ( int j = 0; j < childList.getLength(); j++ ) {
                    if ( childList.item( j ).getNodeName().equals( "link" ) ) {
                        link = childList.item( j ).getChildNodes().item( 0 ).getNodeValue();
                    } else if ( childList.item( j ).getNodeName().equals( "title" ) ) {
                        title = childList.item( j ).getChildNodes().item( 0 ).getNodeValue();

                    } else if ( childList.item( j ).getNodeName().equals( "pubDate" ) ) {
                        pubDate = childList.item( j ).getChildNodes().item( 0 ).getNodeValue();

                    } else if ( childList.item( j ).getNodeName().equals( "description" ) ) {
                        description = childList.item( j ).getChildNodes().item( 0 ).getNodeValue();

                    } else if ( childList.item( j ).getNodeName().equals( "guid" ) ) {
                        guid = childList.item( j ).getChildNodes().item( 0 ).getNodeValue();
                    }
                }

                // assert the basic properties are there.
                if ( link != null && title != null ) {
                    sb.append( "<li>" );

                    sb.append( pubDate );
                    sb.append( " - " );

                    sb.append( "<a href=\"" );
                    sb.append( link );
                    sb.append( "\" title=\"" );
                    sb.append( Xml.cleanString( title ) );
                    sb.append( "\" >" );
                    sb.append( Xml.cleanString( title ) );
                    sb.append( "</a>" );

                    sb.append( "<br />" );
                    sb.append( description );

                    sb.append( "</li>" );
                    sb.append( endl );
                }
            }

            sb.append( "</ul>" );

            return sb.toString();
        } catch ( Exception e ) {
            return "Error, could not process request: " + e.toString();
        }
    }

}
