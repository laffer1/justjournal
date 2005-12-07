package com.justjournal.db;

/**
 * User: laffer1
 * Date: Aug 15, 2005
 * Time: 9:39:50 PM
 */
public class ContentTo {

    private int id;
    private int uriId;
    private int userId;
    private String mimeType;
    private boolean preferred;
    private int dataSize;
    private byte[] data;
    private String metaData;

     /**
     * Retrieves entry id as an int >0
     *
     * @return entry id
     */
    public int getId() {
        return id;
    }


    /**
     * Set the entry id to an int >0
     *
     * @param id entry id to set
     * @throws IllegalArgumentException id < 0
     */
    public void setId(int id)
            throws IllegalArgumentException {
        if (id < 0)
            throw new IllegalArgumentException("Illegal id: " +
                    id);

        this.id = id;
    }

    public int getUriId() {
        return uriId;
    }

    public void setUriId(int uriId)
            throws IllegalArgumentException {
        if (uriId < 0)
            throw new IllegalArgumentException("Illegal uriId: " + uriId);
        this.uriId = uriId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId)
        throws IllegalArgumentException {
        if (userId < 0)
            throw new IllegalArgumentException("Illegal userId: " + userId);
        this.userId = userId;
    }

    public String getMimeType()
    {
        return mimeType;
    }

    public void setMimeType(String mimeType)
        throws IllegalArgumentException
    {
        this.mimeType = mimeType;
    }

    public boolean getPreferred()
    {
        return preferred;
    }

    public void setPreferred(boolean preferred)
    {
        this.preferred = preferred;
    }

    public int getDataSize()
    {
        return dataSize;
    }

    public void setDataSize(int dataSize)
        throws IllegalArgumentException
    {
        if (dataSize < 0)
            throw new IllegalArgumentException("Illegal dataSize: " + dataSize);
        this.dataSize = dataSize;
    }

    /**
     * Retrieves the body or "meat" of the content entity.
     * (should have named this differently!)
     *
     * @return body of content entity
     */
    public byte[] getData() {
        return data;
    }

    /**
     * Sets the body of the content entity.
     *
     * @param data body of entry.
     */
    public void setData(byte[] data) {
        this.data = data;
    }

    public String getMetaData()
    {
        return metaData;
    }

    public void setMetaData(String metaData) {
        this.metaData = metaData;
    }
}
