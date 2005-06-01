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

/**
 * Use this controller to protect content.
 *
 * @author Lucas Holt
 * @version 1.0
 * @see Gatekeeper
 *      Created by IntelliJ IDEA.
 *      User: laffer1
 *      Date: Dec 29, 2003
 *      Time: 7:21:10 PM
 * @since 1.0
 */
public class Protected extends Gatekeeper {

    // loginRequired page
    protected static final String LOGIN_REQUIRED_VIEW_NAME = "loginRequired";

    // destination setup: where do you want to go today?
    protected String dest;

    public String getDest() {
        return this.dest;
    }

    public void setDest(String value) {
        this.dest = value;
    }

    /**
     * Kicks us out to the login required view.
     */
    protected final String outsidePerform() throws Exception {
        this.dest = this.getCtx().getRequest().getRequestURI();

        if (this.getCtx().getRequest().getQueryString() != null)
            this.dest += "?" + this.getCtx().getRequest().getQueryString();

        return LOGIN_REQUIRED_VIEW_NAME;
    }

    /**
     * This method should be overriden to perform application logic
     * which requires authentication.
     */
    protected String insidePerform() throws Exception {
        return SUCCESS;
    }

}