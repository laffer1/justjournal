package com.justjournal.ctl;

import org.infohazard.maverick.ctl.ThrowawayBean2;
import java.util.*;

/** Base controller.  Uses beans to define the model.  The default
 * is the controller that inherits this will be the bean unless
 * otherwise set.
 *
 * @author Lucas Holt
 * @version 1.0
 * @since 1.0
 * Created by IntelliJ IDEA.
 * User: laffer1
 * Date: Dec 29, 2003
 * Time: 6:51:38 PM
 * To change this template use Options | File Templates.
 */
public class ControllerErrorable extends ThrowawayBean2 {
    /**
         */
        protected Map errors;

        /**
         */
        public boolean hasErrors()
        {
            return (this.errors != null);
        }

        /**
         * @return a map of String field name to String message, which
         *  will be empty if no errors have been reported.
         */
        public Map getErrors()
        {
            if (this.errors == null)
                return Collections.EMPTY_MAP;
            else
                return this.errors;
        }

        /**
         */
        protected void addError(String field, String message)
        {
            if (this.errors == null)
                this.errors = new HashMap();

            this.errors.put(field, message);
        }

}
