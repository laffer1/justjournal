
package com.justjournal.db;

/**
 * Represents 1 mood
 *
 * @author Lucas Holt
 * Created by IntelliJ IDEA.
 * User: laffer1
 * Date: Jan 9, 2004
 * Time: 1:55:31 PM
 */
public final class MoodTo
{
    private int id;
    private int parent;  // parent mood
    private String name;

    public int getId()
    {
        return this.id;
    }

    public void setId( final int id )
    {
        this.id = id;
    }

    public int getParent()
    {
        return this.parent;
    }

    public void setParent( final int parent )
    {
        this.parent = parent;
    }

    public String getName()
    {
        return this.name;
    }

    public void setName( final String name )
    {
        this.name = name;
    }
}
