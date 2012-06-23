package com.justjournal.model.auto;

/** Class _Country was generated by Cayenne.
  * It is probably a good idea to avoid changing this class manually, 
  * since it may be overwritten next time code is regenerated. 
  * If you need to make any customizations, please use subclass. 
  */
public class _Country extends org.apache.cayenne.CayenneDataObject {

    public static final String ISO_PROPERTY = "iso";
    public static final String ISO3_PROPERTY = "iso3";
    public static final String ISO_TITLE_PROPERTY = "isoTitle";
    public static final String NUMCODE_PROPERTY = "numcode";
    public static final String TITLE_PROPERTY = "title";

    public static final String ID_PK_COLUMN = "id";

    public void setIso(String iso) {
        writeProperty("iso", iso);
    }
    public String getIso() {
        return (String)readProperty("iso");
    }
    
    
    public void setIso3(String iso3) {
        writeProperty("iso3", iso3);
    }
    public String getIso3() {
        return (String)readProperty("iso3");
    }
    
    
    public void setIsoTitle(String isoTitle) {
        writeProperty("isoTitle", isoTitle);
    }
    public String getIsoTitle() {
        return (String)readProperty("isoTitle");
    }
    
    
    public void setNumcode(Short numcode) {
        writeProperty("numcode", numcode);
    }
    public Short getNumcode() {
        return (Short)readProperty("numcode");
    }
    
    
    public void setTitle(String title) {
        writeProperty("title", title);
    }
    public String getTitle() {
        return (String)readProperty("title");
    }
    
    
}