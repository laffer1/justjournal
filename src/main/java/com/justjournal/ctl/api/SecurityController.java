/*
Copyright (c) 2003-2021, Lucas Holt
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
package com.justjournal.ctl.api;

import static com.justjournal.core.Constants.PARAM_ID;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.justjournal.core.Constants;
import com.justjournal.model.Security;
import com.justjournal.repository.SecurityRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/** @author Lucas Holt */
@RestController
@RequestMapping("/api/security")
public class SecurityController {

  private final SecurityRepository securityDao;

  @Autowired
  public SecurityController(final SecurityRepository securityDao) {
    this.securityDao = securityDao;
  }

  // @Cacheable(value = "security", key = "id")
  @GetMapping(value = "{id}", produces = APPLICATION_JSON_VALUE)
  @ResponseBody
  public ResponseEntity<Security> getById(@PathVariable(PARAM_ID) final Integer id) {
    final Security s = securityDao.findById(id).orElse(null);
    if (s == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    return ResponseEntity.ok().eTag(Integer.toString(s.hashCode())).body(s);
  }

  //   @Cacheable("security")
  @GetMapping(headers = Constants.HEADER_ACCEPT_ALL, produces = APPLICATION_JSON_VALUE)
  @ResponseBody
  public ResponseEntity<List<Security>> getSecurityList() {
    final List<Security> securities = securityDao.findAll();

    return ResponseEntity.ok().eTag(Integer.toString(securities.hashCode())).body(securities);
  }
}
