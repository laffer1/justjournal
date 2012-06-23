package com.justjournal.model.auto;

/** Class _Content was generated by Cayenne.
  * It is probably a good idea to avoid changing this class manually, 
  * since it may be overwritten next time code is regenerated. 
  * If you need to make any customizations, please use subclass. 
  */
public class _Content extends org.apache.cayenne.CayenneDataObject {

    public static final String DATA_PROPERTY = "data";
    public static final String DATASIZE_PROPERTY = "datasize";
    public static final String METADATA_PROPERTY = "metadata";
    public static final String MIME_TYPE_PROPERTY = "mimeType";
    public static final String PREFERRED_PROPERTY = "preferred";
    public static final String URI_ID_PROPERTY = "uriId";
    public static final String USERID_PROPERTY = "userid";

    public static final String ID_PK_COLUMN = "id";

    public void setData(byte[] data) {
        writeProperty("data", data);
    }
    public byte[] getData() {
        return (byte[])readProperty("data");
    }
    
    
    public void setDatasize(Integer datasize) {
        writeProperty("datasize", datasize);
    }
    public Integer getDatasize() {
        return (Integer)readProperty("datasize");
    }
    
    
    public void setMetadata(String metadata) {
        writeProperty("metadata", metadata);
    }
    public String getMetadata() {
        return (String)readProperty("metadata");
    }
    
    
    public void setMimeType(String mimeType) {
        writeProperty("mimeType", mimeType);
    }
    public String getMimeType() {
        return (String)readProperty("mimeType");
    }
    
    
    public void setPreferred(String preferred) {
        writeProperty("preferred", preferred);
    }
    public String getPreferred() {
        return (String)readProperty("preferred");
    }
    
    
    public void setUriId(Long uriId) {
        writeProperty("uriId", uriId);
    }
    public Long getUriId() {
        return (Long)readProperty("uriId");
    }
    
    
    public void setUserid(Long userid) {
        writeProperty("userid", userid);
    }
    public Long getUserid() {
        return (Long)readProperty("userid");
    }
    
    
}