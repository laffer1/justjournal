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
package com.justjournal.ctl;

import static com.justjournal.core.Constants.LOGIN_ATTRID;
import static com.justjournal.core.Constants.PARAM_ID;

import com.justjournal.exception.ServiceException;
import com.justjournal.model.AvatarSource;
import com.justjournal.services.ImageService;
import com.justjournal.services.ImageStorageService;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/** @author Lucas Holt */
@Slf4j
@Controller
@RequestMapping("/Avatar")
public class AvatarController {

  @Autowired private ImageStorageService imageStorageService;

  @Autowired private ImageService imageService;

  @GetMapping("/{id}")
  public ResponseEntity<Resource> getByPath(@PathVariable(PARAM_ID) final int id) {
    try {
      return ResponseEntity.ok()
          .contentType(MediaType.IMAGE_JPEG)
          .header(HttpHeaders.EXPIRES, "180")
          .body(new InputStreamResource(imageStorageService.downloadAvatar(id)));
    } catch (ServiceException se) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PostMapping(value = "")
  public ResponseEntity processUpload(
      @RequestParam MultipartFile file, HttpSession session, HttpRequest request) {

    final Integer userIDasi = (Integer) session.getAttribute(LOGIN_ATTRID);
    int userID = 0;
    if (userIDasi != null) {
      userID = userIDasi;
    }

    /* Make sure we are logged in */
    if (userID < 1) {
      return new ResponseEntity(HttpStatus.FORBIDDEN);
    }

    final String contentType = file.getContentType();
    final long sizeInBytes = file.getSize();

    // must be large enough
    if (file.isEmpty() || sizeInBytes < 500) {
      return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    try {
      BufferedImage avatar = imageService.resizeAvatar(file.getBytes());

      byte[] buffer = ((DataBufferByte) (avatar).getRaster().getDataBuffer()).getData();
      imageStorageService.uploadAvatar(
          userID,
          MediaType.IMAGE_JPEG_VALUE,
          AvatarSource.UPLOAD,
          new ByteArrayInputStream(buffer));

    } catch (final IllegalStateException e) {
      log.error(e.getMessage(), e);
      return new ResponseEntity(HttpStatus.BAD_REQUEST);
    } catch (final IOException | ServiceException e) {
      log.error(e.getMessage(), e);
      return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    return ResponseEntity.created(URI.create(request.getURI().toString() + "/" + userID)).build();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity delete(@PathVariable(PARAM_ID) final int id, final HttpSession session) {

    // Retreive user id
    final Integer userIDasi = (Integer) session.getAttribute(LOGIN_ATTRID);
    // convert Integer to int type
    int userID = 0;
    if (userIDasi != null) {
      userID = userIDasi;
    }

    /* Make sure we are logged in */
    if (userID < 1) {
      return new ResponseEntity(HttpStatus.FORBIDDEN);
    }

    try {
      imageStorageService.deleteAvatar(userID);
    } catch (final Exception dae) {
      log.error(dae.getMessage(), dae);
      return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    return new ResponseEntity(HttpStatus.OK);
  }
}
