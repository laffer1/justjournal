package com.justjournal.content;

import java.io.Serializable;
import java.util.Map;

import javax.servlet.ServletContext;

/**
 * @author Lucas Holt
 * @version $Id$
 */
public interface WebControl extends Serializable {

    //public Context getContext();
    //public void setContext(Context context);

    public String getId();

    public String getName();
    public void setName(String name);

         /**
       * Return the parent of the Control.
       *
       * @return the parent of the Control
       */
      public Object getParent();

       /**
        * Set the parent of the  Control.
        *
        * @param parent the parent of the Control
        */
       public void setParent(Object parent);

    public boolean onProcess();


}
