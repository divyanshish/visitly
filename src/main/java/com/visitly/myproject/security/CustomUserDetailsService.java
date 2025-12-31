package com.visitly.myproject.security;

import com.visitly.myproject.entity.User;
import com.visitly.myproject.repository.UserRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor

public class CustomUserDetailsService implements UserDetailsService {


    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException
    {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.warn("User not found with email: {}", email);
                    return new UsernameNotFoundException("User not found with email: " + email);
                });

        if (!user.getIsActive()) {
            log.warn("User account is disabled: {}", email);
            throw new UsernameNotFoundException("User account is disabled");
        }

        UserBuilder userBuilder = org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPasswordHash())
                .accountLocked(false)
                .accountExpired(false)
                .credentialsExpired(false)
                .disabled(!user.getIsActive());

        if (user.getRoles() != null && !user.getRoles().isEmpty()) {
            var authorities = user.getRoles().stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName().toString()))
                    .collect(Collectors.toList());
            userBuilder.authorities(authorities);
        } else {
            userBuilder.authorities(new SimpleGrantedAuthority("ROLE_USER"));
        }

        log.debug("User loaded successfully: {}", email);
        return userBuilder.build();
    }
}
