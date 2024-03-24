package com.coffee.americanote.user.service;

import com.coffee.americanote.common.validator.CommonValidator;
import com.coffee.americanote.user.domain.entity.User;
import com.coffee.americanote.user.domain.request.UserRequest;
import com.coffee.americanote.user.domain.response.UserResponse;
import com.coffee.americanote.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    public List<UserResponse> getAllUser() {
        List<UserResponse> allUser = new ArrayList<>();
        for (User user : userRepository.findAll()) {
            allUser.add(new UserResponse(user));
        }
        return allUser;
    }

    @Transactional
    public User save(UserRequest userRequest) {
        CommonValidator.notNullOrThrow(userRequest, "사용자 등록 요청");

        User user = User.toUserEntity(userRequest);
        return userRepository.save(user);
    }
}