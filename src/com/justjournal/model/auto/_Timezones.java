package com.justjournal.model.auto;

import org.apache.cayenne.CayenneDataObject;

/**
 * Class _Timezones was generated by Cayenne.
 * It is probably a good idea to avoid changing this class manually,
 * since it may be overwritten next time code is regenerated.
 * If you need to make any customizations, please use subclass.
 */
public abstract class _Timezones extends CayenneDataObject {

    public static final String NAME_PROPERTY = "name";

    public static final String ID_PK_COLUMN = "id";

    public void setName(String name) {
        writeProperty(NAME_PROPERTY, name);
    }
    public String getName() {
        return (String)readProperty(NAME_PROPERTY);
    }

}
