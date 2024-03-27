package com.coffee.americanote.security.service;

import com.coffee.americanote.common.entity.ErrorCode;
import com.coffee.americanote.common.exception.UserException;
import com.coffee.americanote.user.domain.entity.User;
import com.coffee.americanote.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        return userRepository.findById(Long.valueOf(userId))
                .map(this::createUser)
                .orElseThrow(() -> new UserException(ErrorCode.NOT_FOUND_USER));
    }

    private org.springframework.security.core.userdetails.User createUser(User user) {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().name());
        return new org.springframework.security.core.userdetails.User(
                String.valueOf(user.getId()), "", Collections.singleton(authority));
    }
}
