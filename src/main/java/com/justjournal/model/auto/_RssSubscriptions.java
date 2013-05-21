package com.justjournal.model.auto;

import org.apache.cayenne.CayenneDataObject;

/**
 * Class _RssSubscriptions was generated by Cayenne.
 * It is probably a good idea to avoid changing this class manually,
 * since it may be overwritten next time code is regenerated.
 * If you need to make any customizations, please use subclass.
 */
public abstract class _RssSubscriptions extends CayenneDataObject {

    public static final String ID_PROPERTY = "id";
    public static final String URI_PROPERTY = "uri";

    public static final String SUBID_PK_COLUMN = "subid";

    public void setId(Long id) {
        writeProperty(ID_PROPERTY, id);
    }
    public Long getId() {
        return (Long)readProperty(ID_PROPERTY);
    }

    public void setUri(String uri) {
        writeProperty(URI_PROPERTY, uri);
    }
    public String getUri() {
        return (String)readProperty(URI_PROPERTY);
    }

}