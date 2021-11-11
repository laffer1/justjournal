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
package com.justjournal.services;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.justjournal.exception.ServiceException;
import com.justjournal.model.AvatarSource;
import com.justjournal.model.PrefBool;
import com.justjournal.model.UserPic;
import com.justjournal.model.UserPref;
import com.justjournal.repository.UserPicRepository;
import com.justjournal.repository.UserPrefRepository;
import io.minio.MinioClient;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

/** @author Lucas Holt */
@RunWith(SpringRunner.class)
public class ImageStorageServiceTests {

  @Mock private MinioClient minioClient;

  @Mock private UserImageService userImageService;

  @Mock private UserPicRepository userPicRepository;

  @Mock private UserPrefRepository userPrefRepository;

  @InjectMocks private ImageStorageService imageStorageService;

  @Before
  public void setup() {
    imageStorageService.setAvatarBucket("testa");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testDeleteNoUser() throws ServiceException {
    when(userPrefRepository.findById(anyInt())).thenReturn(Optional.empty());
    imageStorageService.deleteAvatar(1);
  }

  @Test
  public void testDeleteAvatar() throws ServiceException {
    UserPref userPref = new UserPref();
    userPref.setShowAvatar(PrefBool.Y);

    UserPic userPic = new UserPic();
    userPic.setFilename("test");
    userPic.setId(1);
    userPic.setMimeType("image/jpeg");

    when(userPrefRepository.findById(anyInt())).thenReturn(Optional.of(userPref));
    when(userPicRepository.findById(anyInt())).thenReturn(Optional.of(userPic));

    imageStorageService.deleteAvatar(1);

    verify(userPrefRepository, times(1)).save(any());
  }

  @Test
  public void testUploadAvatar() throws ServiceException {
    UserPref userPref = new UserPref();
    userPref.setShowAvatar(PrefBool.Y);

    UserPic userPic = new UserPic();
    userPic.setFilename("test");
    userPic.setId(1);
    userPic.setMimeType("image/jpeg");

    when(userPrefRepository.findById(anyInt())).thenReturn(Optional.of(userPref));
    when(userPicRepository.findById(anyInt())).thenReturn(Optional.of(userPic));

    byte[] arr = new byte[] {1, 2, 3, 4, 5, 0};

    imageStorageService.uploadAvatar(
        1, "image/jpeg", AvatarSource.UPLOAD, new ByteArrayInputStream(arr));
    verify(userPrefRepository, times(1)).save(any());
  }

  @Test
  public void testDownloadAvatar()
      throws ServiceException, IOException, InvalidKeyException, NoSuchAlgorithmException,
          InsufficientDataException, InvalidResponseException, InternalException,
          ErrorResponseException, ServerException, XmlParserException {
    UserPic userPic = new UserPic();
    userPic.setFilename("test");
    userPic.setId(1);
    userPic.setMimeType("image/jpeg");
    when(userPicRepository.findById(anyInt())).thenReturn(Optional.of(userPic));
    imageStorageService.downloadAvatar(1);

    verify(minioClient, times(1)).getObject(any());
  }

  @Test
  public void testGetAvatarFileNameJpeg() {
    String result = imageStorageService.getAvatarFileName(1, "image/jpeg");
    assertEquals("avatar_1.jpg", result);
  }

  @Test
  public void testGetAvatarFileNameJpg() {
    String result = imageStorageService.getAvatarFileName(1, "image/jpg");
    assertEquals("avatar_1.jpg", result);
  }

  @Test
  public void testGetAvatarFileNameGif() {
    String result = imageStorageService.getAvatarFileName(1, "image/gif");
    assertEquals("avatar_1.gif", result);
  }

  @Test
  public void testGetAvatarFileNamePng() {
    String result = imageStorageService.getAvatarFileName(1, "image/png");
    assertEquals("avatar_1.png", result);
  }

  @Test
  public void testGetAvatarFileNamePing() {
    String result = imageStorageService.getAvatarFileName(1, "image/ping");
    assertEquals("avatar_1.png", result);
  }
}
