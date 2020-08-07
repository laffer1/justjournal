package com.justjournal.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.ArrayList;
import java.util.Collection;

@JacksonXmlRootElement(localName = "urlset", namespace = "http://www.sitemaps.org/schemas/sitemap/0.9")
public class UrlSet {
    
    @JacksonXmlElementWrapper(useWrapping = false, namespace = "http://www.sitemaps.org/schemas/sitemap/0.9")
    @JacksonXmlProperty(localName = "url", namespace = "http://www.sitemaps.org/schemas/sitemap/0.9")
    private Collection<Url> url = new ArrayList<>();

    public void addUrl(final Url u) {
        url.add(u);
    }

    public Collection<Url> getUrl() {
        return url;
    }
}