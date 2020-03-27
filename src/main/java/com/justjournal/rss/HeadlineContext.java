package com.justjournal.rss;

import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.net.URL;

/**
 * @author Lucas Holt
 */
public class HeadlineContext {
    protected URL u;
    protected InputStream inputXML;
    protected DocumentBuilder builder;
    protected Document document;
    final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
}
