package com.justjournal.services;

import com.justjournal.model.User;
import com.justjournal.model.api.PublicMember;
import com.justjournal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lucas Holt
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;


    @Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
    @Cacheable(cacheNames = "members", value="members")
    public List<PublicMember> getPublicMembers() {
        final List<User> users = userRepository.getPublicUsers();
        final List<PublicMember> publicMembers = new ArrayList<>();
        for (final User user : users) {
            publicMembers.add(new PublicMember(user.getUsername(), user.getFirstName(), user.getSince()));
        }
        return publicMembers;
    }
}
