
package com.justjournal.utility;

import com.swabunga.spell.event.*;
import com.swabunga.spell.engine.*;

import java.io.File;
import java.util.List;
import java.util.Iterator;

/**
 * Frontend to Jazzy Spell Check engine.
 * Generates XHTML 1.0 output to spell check queries.
 *
 * @author Lucas Holt
 * @version 1.1
 * @since 1.0
 * User: laffer1
 * Date: Sep 19, 2003
 * Time: 6:11:18 PM
 */
public final class Spelling
        implements SpellCheckListener
{

    private static String dictFile = "/usr/local/dict/english.0";
    private SpellChecker spellCheck;
    private StringBuffer sb; // output variable

    /**
     * Creates an instance of Spell dictionary and reads
     * the dictionary file from disk.
     */
    public Spelling()
    {
        try {
            SpellDictionary dictionary = new SpellDictionary( new File( dictFile ) );

            spellCheck = new SpellChecker( dictionary );
            spellCheck.addSpellCheckListener( this );

        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }


    /**
     * Action to perform when a spelling error occurs. In this
     * case we output HTML markup to display the error.
     * @param event A reference to the spell check event (word)
     */
    public void spellingError( SpellCheckEvent event )
    {
        final List suggestions = event.getSuggestions();
        if ( suggestions.size() > 0 )
        {
            sb.append( "<p><span style=\"color:red\">");
            sb.append( event.getInvalidWord() );
            sb.append( "</span> " );

            // The counter fixes a bug where the , appears in the
            // output after the last word or after the only word.
            // this looks like crap.
            int i = 0;
            for ( Iterator suggestedWord = suggestions.iterator();
                  suggestedWord.hasNext(); i++ )
            {
                if ( i > 0 )
                    sb.append( ",&nbsp;" );

                sb.append( suggestedWord.next() );
            }

            sb.append( "</p>\n" );

        }
        // Null actions since this is event based we probably
        // dont want to do anything if a word is "OK"
    }

    /**
     * Used to check the spelling of the given string.
     * @param inText  Text to check
     * @return String containing errors found in document.
     */
    public String checkSpelling( String inText )
    {
        sb = new StringBuffer();

        try {

            spellCheck.checkSpelling( new StringWordTokenizer( inText ) );

        } catch ( Exception e ) {
            e.printStackTrace();
        }

        return sb.toString();
    }

}
