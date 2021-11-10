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


import com.justjournal.model.User;
import com.justjournal.model.UserLink;
import com.justjournal.model.api.UserLinkTo;
import com.justjournal.repository.UserLinkRepository;
import com.justjournal.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.NonNull;
import org.springframework.stereotype.Service;

/** @author Lucas Holt */
@Service
public class UserLinkService {
  private final UserLinkRepository userLinkRepository;

  private final UserRepository userRepository;

  public UserLinkService(
      final UserLinkRepository userLinkRepository, final UserRepository userRepository) {
    this.userLinkRepository = userLinkRepository;
    this.userRepository = userRepository;
  }

  public Optional<UserLinkTo> get(final int linkId) {
    return userLinkRepository.findById(linkId).map(this::transform);
  }

  public List<UserLinkTo> getByUser(@NonNull final String username) {
    return userLinkRepository.findByUsernameOrderByTitleTitleAsc(username).stream()
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
    if (userLink == null) return null;

    return UserLinkTo.builder()
        .id(userLink.getId())
        .title(userLink.getTitle())
        .uri(userLink.getUri())
        .userId(userLink.getUser().getId())
        .build();
  }

  protected UserLink transform(final UserLinkTo userLink) {
    final UserLink link =
        UserLink.builder()
            .id(userLink.getId())
            .title(userLink.getTitle())
            .uri(userLink.getUri())
            .build();
    final Optional<User> user = userRepository.findById(userLink.getUserId());
    user.ifPresent(link::setUser);
    return link;
  }
}
