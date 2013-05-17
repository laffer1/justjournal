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

package com.justjournal.content;

/**
 * A piece of meta data associated with a Content entity.  Possible uses
 * include music selection, mood, submitter or other fields. Represented
 * in name/value pairs.
 * <p/>
 * User: laffer1
 * Date: Jun 2, 2005
 * Time: 10:15:21 PM
 *
 * @author Lucas Holt
 * @version 1.0
 */
public interface MetaData {

    /**
     * Name of the meta data property
     *
     * @return String version of name of the property
     */
    public String getName();

    /**
     * Sets the name of the property.
     *
     * @param name Property Name
     */
    public void setName(String name);

    /**
     * Retrieves the value of the meta property.
     *
     * @return String representation of the meta data property.
     */
    public String getValue();

    /**
     * Sets the value of the meta property.
     *
     * @param value Meta data Property's value.
     */
    public void setValue(String value);

}
