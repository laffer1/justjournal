
package com.justjournal.db;

/**
 * Emoticon resource data transfer object.  Basic properites
 * including the filename, height, width, mood and theme ids.
 *
 * @author Lucas Holt
 * @version 1.0
 * @since 1.0
 * User: laffer1
 * Date: Sep 22, 2003
 * Time: 11:01:45 PM
 */
public class EmoticonTo
{
    private String filename;
    private int moodId;
    private int moodTheme;
    private int width;
    private int height;

    public String getFileName()
    {
        return filename;
    }

    public void setFileName( String value )
    {
        filename = value;
    }

    public int getMoodId()
    {
        return moodId;
    }

    public void setMoodId( int value )
    {
        moodId = value;
    }

    public int getMoodTheme()
    {
        return moodTheme;
    }

    public void setMoodTheme( int value )
    {
        moodTheme = value;
    }

    public int getWidth()
    {
        return width;
    }

    public void setWidth( int value )
    {
        width = value;
    }

    public int getHeight()
    {
        return height;
    }

    public void setHeight( int value )
    {
        height = value;
    }

    public String toString()
    {
        StringBuffer output = new StringBuffer();

        output.append("mood id: ");
        output.append(moodId);
        output.append('\n');

        output.append("theme id: ");
        output.append(moodTheme);
        output.append('\n');

        output.append("filename: ");
        output.append(filename);
        output.append('\n');

        output.append("width: ");
        output.append(width);
        output.append('\n');

        output.append("height: ");
        output.append(height);
        output.append('\n');

        return output.toString();
    }
}
