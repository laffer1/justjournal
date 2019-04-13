package com.justjournal.model;

import com.justjournal.repository.UserPicRepository;
import com.justjournal.services.ServiceException;
import com.justjournal.services.UserImageService;
import io.minio.MinioClient;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidArgumentException;
import io.minio.errors.InvalidBucketNameException;
import io.minio.errors.NoResponseException;
import io.minio.errors.RegionConflictException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.xmlpull.v1.XmlPullParserException;

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

    @Value("${bucket.avatar}")
    private String avatarBucket;

    @Value("${bucket.image}")
    private String imageBucket;

    @Autowired
    private MinioClient minioClient;

    @Autowired
    private UserImageService userImageService;

    @Autowired
    private UserPicRepository userPicRepository;

    public void uploadAvatar(int userId, @NonNull String mimeType, @NonNull InputStream is)
            throws ServiceException {
        try {
            Optional<UserPic> userPic = userPicRepository.findById(userId);
            if (!userPic.isPresent()) {
                userPic = Optional.of(new UserPic());
            }

            userPic.get().setMimeType(mimeType);
            userPic.get().setModified(Calendar.getInstance().getTime());

            userPicRepository.save(userPic.get());

            uploadFile(avatarBucket, getAvatarFileName(userId, mimeType), mimeType, is);
        } catch (Exception e) {
            log.error("Could not upload avatar", e);
            throw new ServiceException("Unable to save avatar");
        }
    }

    public InputStream downloadAvatar(int userId) throws ServiceException {
        try {
            Optional<UserPic> userPic = userPicRepository.findById(userId);
            return downloadFile(avatarBucket, getAvatarFileName(userId, userPic.get().getMimeType()));
        } catch (final Exception e) {
            log.error("Could not fetch avatar", e);
            throw new ServiceException("Unable to download avatar");
        }
    }

    private String getAvatarFileName(int id, @NonNull String mimeType) {
        final String name = "avatar_" + id;

        if (mimeType.contains("jpeg") || mimeType.contains("jpg")) {
            return name + ".jpg";
        } else if (mimeType.contains("png") || mimeType.contains("ping")) {
            return name + ".png";
        }

        return name;
    }

    public void uploadFile(@NonNull String bucketName,
                           @NonNull String objectName,
                           @NonNull String mimeType,
                           @NonNull InputStream is) throws IOException, InvalidKeyException,
            NoSuchAlgorithmException, InsufficientDataException, InvalidArgumentException, InternalException,
            NoResponseException, InvalidBucketNameException, XmlPullParserException, ErrorResponseException,
            RegionConflictException {

        if (!minioClient.bucketExists(bucketName)) {
            minioClient.makeBucket(bucketName);
        }

        minioClient.putObject(bucketName, objectName, is, mimeType);
    }

    public InputStream downloadFile(@NonNull String bucketName, @NonNull String objectName)
            throws IOException, InvalidKeyException,
            NoSuchAlgorithmException, InsufficientDataException, InvalidArgumentException, InternalException,
            NoResponseException, InvalidBucketNameException, XmlPullParserException, ErrorResponseException {

        return minioClient.getObject(bucketName, objectName);
    }
}
