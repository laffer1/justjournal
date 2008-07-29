package com.justjournal.content;

import com.justjournal.HitCounter;

/**
 * User: laffer1
 * Date: Jul 27, 2008
 * Time: 7:21:29 AM
 */
public class HitCountControl {

    private String resource;

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String render() {
        StringBuilder sb = new StringBuilder();
        HitCounter hit = new HitCounter();

        sb.append("<div class=\"hitcountcontrol\">");
        sb.append(hit.increment(getResource()));
        sb.append("</div>");

        return sb.toString();
    }
}
