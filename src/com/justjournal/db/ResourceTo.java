package com.justjournal.db;

/**
 * User: laffer1
 * Date: Jul 24, 2005
 * Time: 10:48:55 AM
 */
public class ResourceTo {
    private int id;
    private String name;
    private boolean active;
    private int securityLevel;

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

    public int getSecurityLevel() {
        return securityLevel;
    }

    public void setSecurityLevel(int sec)
            throws IllegalArgumentException {
        if (sec < 0)
            throw new IllegalArgumentException("Illegal security level: " +
                    sec);
        securityLevel = sec;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getName() {
        return name;
    }

    public void setName(String name)
            throws IllegalArgumentException {
        if (name == null)
            throw new IllegalArgumentException("name can not be null");

        if (name.length() <= 200 && name.length() >= 1) {
            this.name = name;
        } else {
            throw new IllegalArgumentException("Illegal name length");
        }
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final ResourceTo that = (ResourceTo) o;

        if (active != that.active) return false;
        if (id != that.id) return false;
        if (securityLevel != that.securityLevel) return false;
        if (!name.equals(that.name)) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = id;
        result = 29 * result + name.hashCode();
        result = 29 * result + (active ? 1 : 0);
        result = 29 * result + securityLevel;
        return result;
    }

}
