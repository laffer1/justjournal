/*
 * Copyright (c) 2003-2021 Lucas Holt
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR AND CONTRIBUTORS ``AS IS'' AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED.  IN NO EVENT SHALL THE AUTHOR OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
 * OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 */
package com.justjournal.core;


import com.justjournal.exception.ServiceException;
import com.justjournal.model.AvatarSource;
import com.justjournal.model.User;
import com.justjournal.model.UserPic;
import com.justjournal.repository.UserPicRepository;
import com.justjournal.repository.UserRepository;
import com.justjournal.services.ImageService;
import com.justjournal.services.ImageStorageService;
import com.timgroup.jgravatar.Gravatar;
import com.timgroup.jgravatar.GravatarDefaultImage;
import com.timgroup.jgravatar.GravatarRating;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/** @author Lucas Holt */
@Slf4j
@Component
public class GravatarFetcher {

  private static final int GRAVATAR_FETCH_PAUSE_MS = 10000;

  @Autowired ImageStorageService imageStorageService;

  @Autowired private ImageService imageService;

  @Autowired UserPicRepository userPicRepository;

  @Autowired private UserRepository userRepository;

  private byte[] getGravatar(final String email) {
    log.info("Attempting fetch of gravatar for email " + email);
    final Gravatar gravatar =
        new Gravatar(100, GravatarRating.RESTRICTED, GravatarDefaultImage.HTTP_404);
    return gravatar.download(email);
  }

  private void upload(
      final int userId, final String type, final AvatarSource source, final byte[] image) {
    if (image == null) return;

    try {
      imageStorageService.uploadAvatar(userId, type, source, new ByteArrayInputStream(image));
    } catch (final Exception e) {
      log.warn("Could not upload avatar for user id: {}", userId, e);
    }
  }

  @Scheduled(fixedDelay = 1000 * 60 * 60 * 24, initialDelay = 30000)
  public void run() throws ServiceException, IOException {
    for (final User user : userRepository.findAll()) {
      try {
        if (!userPicRepository.existsById(user.getId())) {
          upload(
              user.getId(),
              MediaType.IMAGE_JPEG_VALUE,
              AvatarSource.GRAVATAR,
              getGravatar(user.getUserContact().getEmail()));

          Thread.sleep(GRAVATAR_FETCH_PAUSE_MS);
        }
      } catch (final Exception e) {
        log.warn("Could not find for user {}", user.getUsername(), e);
      }

      /*   convert existing db files   */
      for (final UserPic userPic : userPicRepository.findAll()) {
        if (userPic.getFilename() != null || userPic.getSource() != AvatarSource.UPLOAD) continue;

        upload(userPic.getId(), userPic.getMimeType(), AvatarSource.UPLOAD, userPic.getImage());
      }
    }
  }
}
