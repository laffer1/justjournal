/*
Copyright (c) 2005, Lucas Holt
All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are
permitted provided that the following conditions are met:

  Redistributions of source code must retain the above copyright notice, this list of
  conditions and the following disclaimer.

  Redistributions in binary form must reproduce the above copyright notice, this
  list of conditions and the following disclaimer in the documentation and/or other
  materials provided with the distribution.

  Neither the name of the Just Journal nor the names of its contributors
  may be used to endorse or promote products derived from this software without
  specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND
CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR
BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY
OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
POSSIBILITY OF SUCH DAMAGE.
*/

package com.justjournal.ctl;

import org.infohazard.maverick.ctl.ThrowawayBean2;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Base controller.  Uses beans to define the model.  The default
 * is the controller that inherits this will be the bean unless
 * otherwise set.
 *
 * @author Lucas Holt
 * @version 1.0
 * @since 1.0
 *        Created by IntelliJ IDEA.
 *        User: laffer1
 *        Date: Dec 29, 2003
 *        Time: 6:51:38 PM
 *        To change this template use Options | File Templates.
 */
public class ControllerErrorable extends ThrowawayBean2 {
    /**
     */
    protected Map errors;

    /**
     */
    public boolean hasErrors() {
        return (this.errors != null);
    }

    /**
     * @return a map of String field name to String message, which
     *         will be empty if no errors have been reported.
     */
    public Map getErrors() {
        if (this.errors == null)
            return Collections.EMPTY_MAP;
        else
            return this.errors;
    }

    /**
     */
    protected void addError(String field, String message) {
        if (this.errors == null)
            this.errors = new HashMap();

        this.errors.put(field, message);
    }

}
