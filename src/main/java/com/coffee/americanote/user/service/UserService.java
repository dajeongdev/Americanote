package com.coffee.americanote.user.service;

import com.coffee.americanote.user.domain.entity.User;
import com.coffee.americanote.user.domain.request.UserRequest;
import com.coffee.americanote.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public User save(UserRequest userRequest) {
        User user = User.toUserEntity(userRequest);
        return userRepository.save(user);
    }
}