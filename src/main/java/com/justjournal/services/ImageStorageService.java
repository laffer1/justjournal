package com.justjournal.services;

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
import io.minio.errors.InvalidArgumentException;
import io.minio.errors.InvalidBucketNameException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.NoResponseException;
import io.minio.errors.RegionConflictException;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Optional;

/**
 * @author Lucas Holt
 */
@Slf4j
@Service
public class ImageStorageService {

    @Setter
    @Value("${bucket.avatar:jjavatar}")
    private String avatarBucket;

    @Setter
    @Value("${bucket.image:jjimages}")
    private String imageBucket;

    @Autowired
    private MinioClient minioClient;

    @Autowired
    private UserImageService userImageService;

    @Autowired
    private UserPicRepository userPicRepository;

    @Autowired
    private UserPrefRepository userPrefRepository;

    public void deleteAvatar(final int userId) throws ServiceException {
        final Optional<UserPref> pref = userPrefRepository.findById(userId);
        if (!pref.isPresent()) {
            throw new IllegalArgumentException("userId");
        }

        final Optional<UserPic> userPic = userPicRepository.findById(userId);
        if (userPic.isPresent()) {

            String filename =  userPic.get().getFilename();
            if (filename == null)
                filename = getAvatarFileName(userId, userPic.get().getMimeType());

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

    public void uploadAvatar(final int userId, @NonNull final String mimeType, @NonNull final AvatarSource source,
                             @NonNull final InputStream is)
            throws ServiceException {

        if (userId < 1)
            throw new IllegalArgumentException("userId");

        try {
            Optional<UserPic> userPic = userPicRepository.findById(userId);
            if (!userPic.isPresent()) {
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
            if (!pref.isPresent()) {
                throw new IllegalArgumentException("userId");
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
            if (!userPic.isPresent()) {
                throw new IllegalArgumentException("userId");
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
     * @param is  input stream (preferably a ByteArrayInputStream)
     * @throws IOException
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     * @throws InsufficientDataException
     * @throws InvalidArgumentException
     * @throws InternalException
     * @throws NoResponseException
     * @throws InvalidBucketNameException
     * @throws XmlPullParserException
     * @throws ErrorResponseException
     * @throws RegionConflictException
     * @throws InvalidResponseException
     */
    public void uploadFile(@NonNull String bucketName,
                           @NonNull String objectName,
                           @NonNull String mimeType,
                           @NonNull InputStream is) throws IOException, InvalidKeyException,
            NoSuchAlgorithmException, InsufficientDataException, InvalidArgumentException, InternalException,
            NoResponseException, InvalidBucketNameException, XmlPullParserException, ErrorResponseException,
            RegionConflictException, InvalidResponseException {

        if (!minioClient.bucketExists(bucketName)) {
            minioClient.makeBucket(bucketName);
        }

        if (is instanceof ByteArrayInputStream)
            minioClient.putObject(bucketName, objectName, is, is.available(), mimeType);
        else
            minioClient.putObject(bucketName, objectName, is, mimeType);
        is.close();
    }

    public InputStream downloadFile(@NonNull String bucketName, @NonNull String objectName)
            throws IOException, InvalidKeyException,
            NoSuchAlgorithmException, InsufficientDataException, InvalidArgumentException, InternalException,
            NoResponseException, InvalidBucketNameException, XmlPullParserException, ErrorResponseException, InvalidResponseException {

        return minioClient.getObject(bucketName, objectName);
    }

    public void deleteFile(@NonNull String bucketName, @NonNull String objectName) throws IOException, InvalidKeyException,
            NoSuchAlgorithmException, InsufficientDataException, InvalidArgumentException, InvalidResponseException,
            InternalException, NoResponseException, InvalidBucketNameException, XmlPullParserException, ErrorResponseException {
          minioClient.removeObject(bucketName, objectName);
    }
}
