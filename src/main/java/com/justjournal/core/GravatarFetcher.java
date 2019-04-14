package com.justjournal.core;

import com.justjournal.model.AvatarSource;
import com.justjournal.services.ImageStorageService;
import com.justjournal.model.User;
import com.justjournal.model.UserPic;
import com.justjournal.repository.UserPicRepository;
import com.justjournal.repository.UserRepository;
import com.justjournal.services.ImageService;
import com.justjournal.services.ServiceException;
import com.timgroup.jgravatar.Gravatar;
import com.timgroup.jgravatar.GravatarDefaultImage;
import com.timgroup.jgravatar.GravatarRating;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * @author Lucas Holt
 */
@Slf4j
@Component
public class GravatarFetcher {


    @Autowired
    ImageStorageService imageStorageService;

    @Autowired
    private ImageService imageService;

    @Autowired
    UserPicRepository userPicRepository;

    @Autowired
    private UserRepository userRepository;

    @Scheduled(fixedDelay = 1000 * 60 * 60 * 24, initialDelay = 30000)
    public void run() throws ServiceException, IOException {
        for (final User user : userRepository.findAll()) {
            try {
                if (!userPicRepository.existsById(user.getId())) {
                    log.info("Attempting fetch of gravatar for " + user.getUsername());

                    final Gravatar gravatar = new Gravatar(100,GravatarRating.GENERAL_AUDIENCES ,
                            GravatarDefaultImage.IDENTICON);
                    byte[] jpg = gravatar.download(user.getUserContact().getEmail());

                    if (jpg != null) {
                        imageStorageService.uploadAvatar(user.getId(), MediaType.IMAGE_JPEG_VALUE,
                                AvatarSource.GRAVATAR,
                                new ByteArrayInputStream(jpg));
                    }

                    Thread.sleep(2000);
                }
            } catch (final Exception e) {
                log.warn("Could not fetch gravatar for user {}", user.getUsername(), e);
            }

            /*   convert existing db files   */
            for (final UserPic userPic : userPicRepository.findAll()) {
                if (userPic.getFilename() != null)
                    continue;
                if (userPic.getSource() != AvatarSource.UPLOAD)
                    continue;
                
                try {
                    imageStorageService.uploadAvatar(userPic.getId(), userPic.getMimeType(),  AvatarSource.UPLOAD,

                            new ByteArrayInputStream(
                                    userPic.getImage()
                                    //        ((DataBufferByte) (imageService.resizeAvatar(userPic.getImage())).getRaster().getDataBuffer()).getData()
                            ));
                } catch (final Exception e) {
                    log.error("Could not save image", e);
                }
            }
        }
    }
}
