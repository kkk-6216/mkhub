package com.pro.mkhub_backend.auth.service;

import com.pro.mkhub_backend.auth.dto.PasswordChangeDto;
import com.pro.mkhub_backend.auth.dto.RegistrationRequest;
import com.pro.mkhub_backend.auth.model.entity.User;
import com.pro.mkhub_backend.auth.model.enums.Role;
import com.pro.mkhub_backend.auth.repository.UserRepository;
import com.pro.mkhub_backend.user.dto.SecureStatus;
import com.pro.mkhub_backend.user.dto.UserCreationDto;
import com.pro.mkhub_backend.user.dto.UserItem;
import com.pro.mkhub_backend.user.model.entity.UserInfo;
import com.pro.mkhub_backend.user.repository.UserInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
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
        userInfo.setEmail(registrationRequest.getEmail());

        savedUser.setUserInfo(userInfo);
        userInfo.setUser(savedUser);

        userInfoRepository.save(userInfo);

        return savedUser;
    }

    @Transactional
    public User createUser(UserCreationDto userCreationDto) {
        User user = createUser(new RegistrationRequest(
                userCreationDto.getUsername(),
                userCreationDto.getEmail(),
                userCreationDto.getPassword()
        ));
        user.setRole(userCreationDto.getRole());
        return user;
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

    public List<UserItem> findAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(UserItem::new)
                .toList();
    }

    @Transactional
    public void deleteUserById(Long id) {
        User user = findById(id);
        userRepository.delete(user);
    }

    @Transactional
    public void updateUsername(Long id, String username) {
        User user = findById(id);
        user.setUsername(username);
        User updated = userRepository.save(user);
    }

    @Transactional
    public void updateUserRole(Long id, Role role) {
        User user = findById(id);
        user.setRole(role);
        userRepository.save(user);
    }

    @Transactional
    public void updateSecureStatus(Long id, SecureStatus secureStatus) {
        User user = findById(id);
        user.setEnabled(secureStatus.isEnabled());
        user.setAccountNonExpired(secureStatus.isAccountNonExpired());
        user.setAccountNonLocked(secureStatus.isAccountNonLocked());
        user.setCredentialsNonExpired(secureStatus.isCredentialsNonExpired());
        userRepository.save(user);
    }

}