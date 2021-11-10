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
package com.justjournal.services;


import com.justjournal.exception.ServiceException;
import com.justjournal.model.AvatarSource;
import com.justjournal.model.PrefBool;
import com.justjournal.model.UserPic;
import com.justjournal.model.UserPref;
import com.justjournal.repository.UserPicRepository;
import com.justjournal.repository.UserPrefRepository;
import io.minio.*;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Optional;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/** @author Lucas Holt */
@Slf4j
@Service
public class ImageStorageService {

    @Setter
    @Value("${bucket.avatar:jjavatar}")
    private String avatarBucket;

    @Setter
    @Value("${bucket.image:jjimages}")
    private String imageBucket;

    @Autowired private MinioClient minioClient;

    @Autowired private UserImageService userImageService;

    @Autowired private UserPicRepository userPicRepository;

    @Autowired private UserPrefRepository userPrefRepository;

    private static final String USER_ID_ARGUMENT = "userId";

    public void deleteAvatar(final int userId) throws ServiceException {
        final Optional<UserPref> pref = userPrefRepository.findById(userId);
        if (pref.isEmpty()) {
            throw new IllegalArgumentException(USER_ID_ARGUMENT);
        }

        final Optional<UserPic> userPic = userPicRepository.findById(userId);
        if (userPic.isPresent()) {

            String filename = userPic.get().getFilename();
            if (filename == null) filename = getAvatarFileName(userId, userPic.get().getMimeType());

            try {
                deleteFile(avatarBucket, filename);
                userPicRepository.deleteById(userId);

                pref.get().setShowAvatar(PrefBool.N);
                userPrefRepository.save(pref.get());
            } catch (final Exception e) {
                log.error("Unable to delete avatar file {} for user {}", filename, userId, e);
                throw new ServiceException("Unable to delete avatar");
            }
        }
    }

    public void uploadAvatar(
            final int userId,
            @NonNull final String mimeType,
            @NonNull final AvatarSource source,
            @NonNull final InputStream is)
            throws ServiceException {

        if (userId < 1) throw new IllegalArgumentException(USER_ID_ARGUMENT);

        try {
            Optional<UserPic> userPic = userPicRepository.findById(userId);
            if (userPic.isEmpty()) {
                userPic = Optional.of(new UserPic());
            }

            final UserPic entity = userPic.get();
            entity.setId(userId);
            entity.setMimeType(mimeType);
            entity.setModified(Calendar.getInstance().getTime());
            entity.setSource(source);
            entity.setFilename(getAvatarFileName(userId, mimeType));

            userPicRepository.save(entity);

            uploadFile(avatarBucket, entity.getFilename(), mimeType, is);

            final Optional<UserPref> pref = userPrefRepository.findById(userId);
            if (pref.isEmpty()) {
                throw new IllegalArgumentException(USER_ID_ARGUMENT);
            }
            pref.get().setShowAvatar(PrefBool.N);
            userPrefRepository.save(pref.get());
        } catch (final Exception e) {
            log.error("Could not upload avatar", e);
            throw new ServiceException("Unable to save avatar");
        }
    }

    public InputStream downloadAvatar(final int userId) throws ServiceException {
        try {
            final Optional<UserPic> userPic = userPicRepository.findById(userId);
            if (userPic.isEmpty()) {
                throw new IllegalArgumentException(USER_ID_ARGUMENT);
            }
            return downloadFile(avatarBucket, userPic.get().getFilename());
        } catch (final Exception e) {
            log.error("Could not fetch avatar", e);
            throw new ServiceException("Unable to download avatar");
        }
    }

    protected String getAvatarFileName(final int id, @NonNull final String mimeType) {
        final String name = "avatar_" + id;

        if (mimeType.contains("jpeg") || mimeType.contains("jpg")) {
            return name + ".jpg";
        } else if (mimeType.contains("png") || mimeType.contains("ping")) {
            return name + ".png";
        } else if (mimeType.contains("gif")) {
            return name + ".gif";
        }

        return name;
    }

    /**
     * upload a file and will close inputstream upon completion.
     *
     * @param bucketName bucket to store images in
     * @param objectName image name or file name
     * @param mimeType image mime type
     * @param is input stream (preferably a ByteArrayInputStream)
     * @throws IOException
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     * @throws InsufficientDataException
     * @throws ErrorResponseException
     * @throws InvalidResponseException
     */
    public void uploadFile(
            @NonNull final String bucketName,
            @NonNull final String objectName,
            @NonNull final String mimeType,
            @NonNull final InputStream is)
            throws IOException, InvalidKeyException, NoSuchAlgorithmException,
                    InsufficientDataException, ErrorResponseException, InvalidResponseException,
                    InternalException, XmlParserException, ServerException {

        if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        }

        if (is instanceof ByteArrayInputStream)
            minioClient.putObject(
                    PutObjectArgs.builder().bucket(bucketName).object(objectName).stream(
                                    is, is.available(), -1)
                            .contentType(mimeType)
                            .build());
        else
            minioClient.putObject(
                    PutObjectArgs.builder().bucket(bucketName).object(objectName).stream(
                                    is, -1, 10485760)
                            .contentType(mimeType)
                            .build());
        is.close();
    }

    public InputStream downloadFile(
            @NonNull final String bucketName, @NonNull final String objectName)
            throws IOException, InvalidKeyException, NoSuchAlgorithmException,
                    InsufficientDataException, ErrorResponseException, InvalidResponseException,
                    InternalException, XmlParserException, ServerException {

        return minioClient.getObject(
                GetObjectArgs.builder().bucket(bucketName).object(objectName).build());
    }

    public void deleteFile(@NonNull final String bucketName, @NonNull final String objectName)
            throws IOException, InvalidKeyException, NoSuchAlgorithmException,
                    InsufficientDataException, InvalidResponseException, ErrorResponseException,
                    InternalException, XmlParserException, ServerException {
        minioClient.removeObject(
                RemoveObjectArgs.builder().bucket(bucketName).object(objectName).build());
    }
}
