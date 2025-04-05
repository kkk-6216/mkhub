package com.pro.mkhub_backend.auth.service;

import com.pro.mkhub_backend.auth.dto.PasswordChangeDto;
import com.pro.mkhub_backend.auth.dto.RegistrationRequest;
import com.pro.mkhub_backend.auth.model.entity.User;
import com.pro.mkhub_backend.auth.model.enums.Role;
import com.pro.mkhub_backend.auth.repository.UserRepository;
import com.pro.mkhub_backend.user.model.entity.UserInfo;
import com.pro.mkhub_backend.user.repository.UserInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserInfoRepository userInfoRepository;

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Transactional
    public User createUser(RegistrationRequest registrationRequest) {
        User user = new User();
        user.setUsername(registrationRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        user.setRole(Role.ROLE_STUDENT);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        User savedUser = userRepository.save(user);

        UserInfo userInfo = new UserInfo();
        userInfo.setUsername(savedUser.getUsername());
        userInfo.setEmail(registrationRequest.getEmail());

        savedUser.setUserInfo(userInfo);
        userInfo.setUser(savedUser);

        userInfoRepository.save(userInfo);

        return savedUser;
    }


    @Transactional
    public void changePassword(PasswordChangeDto request) {
        System.out.println(request);
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        System.out.println(user);
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new RuntimeException("Старый пароль неверен");
        }

        if (!request.getNewPassword().equals(request.getConfirmNewPassword())) {
            throw new RuntimeException("Новый пароль и подтверждение не совпадают");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        System.out.println(user);
        userRepository.save(user);
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new RuntimeException("User: " + id + " not found")
        );
    }

}