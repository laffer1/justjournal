package com.justjournal.services;

import com.justjournal.model.User;
import com.justjournal.model.UserLink;
import com.justjournal.model.api.UserLinkTo;
import com.justjournal.repository.UserLinkRepository;
import com.justjournal.repository.UserRepository;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Lucas Holt
 */
@Service
public class UserLinkService {
    private final UserLinkRepository userLinkRepository;

    private final UserRepository userRepository;

    public UserLinkService(final UserLinkRepository userLinkRepository, final UserRepository userRepository) {
        this.userLinkRepository = userLinkRepository;
        this.userRepository = userRepository;
    }

    public Optional<UserLinkTo> get(final int linkId) {
        return userLinkRepository.findById(linkId).map(this::transform);
    }

    public List<UserLinkTo> getByUser(@NonNull final String username) {
        return userLinkRepository.findByUsernameOrderByTitleTitleAsc(username)
                .stream()
                .map(this::transform)
                .collect(Collectors.toList());
    }

    public void delete(final int linkId) {
        userLinkRepository.deleteById(linkId);
    }

    public UserLinkTo create(final UserLinkTo link) {
        return transform(save(link));
    }

    protected UserLink save(final UserLinkTo link) {
        return userLinkRepository.save(transform(link));
    }

    protected UserLinkTo transform(final UserLink userLink) {
        if (userLink == null)
            return null;

        return UserLinkTo.builder()
                .id(userLink.getId())
                .title(userLink.getTitle())
                .uri(userLink.getUri())
                .userId(userLink.getUser().getId())
                .build();
    }

    protected UserLink transform(final UserLinkTo userLink) {
        final UserLink link = UserLink.builder()
                .id(userLink.getId())
                .title(userLink.getTitle())
                .uri(userLink.getUri())
                .build();
        final Optional<User> user = userRepository.findById(userLink.getUserId());
        user.ifPresent(link::setUser);
        return link;
    }
}
