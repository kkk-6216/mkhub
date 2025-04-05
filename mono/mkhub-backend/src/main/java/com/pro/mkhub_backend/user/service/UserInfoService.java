package com.pro.mkhub_backend.user.service;

import com.pro.mkhub_backend.auth.model.entity.User;
import com.pro.mkhub_backend.auth.repository.UserRepository;
import com.pro.mkhub_backend.file_storage.util.GoogleDriveLinkBuilder;
import com.pro.mkhub_backend.user.dto.UserAbout;
import com.pro.mkhub_backend.user.dto.UserData;
import com.pro.mkhub_backend.user.dto.UserDetailsDto;
import com.pro.mkhub_backend.user.model.entity.UserInfo;
import com.pro.mkhub_backend.user.repository.UserInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserInfoService {

    private final UserInfoRepository userInfoRepository;
    private final UserRepository userRepository;
    private final GoogleDriveLinkBuilder googleDriveLinkBuilder;

    @Transactional
    public void updateAbout(UserAbout userAbout, Long id) {
        UserInfo userInfo = userInfoRepository.findById(id).orElseThrow(
                () -> new RuntimeException("User with id: " + id + " not found")
        );

        userInfo.setAbout(userAbout.getAbout());

        userInfoRepository.save(userInfo);
    }

    @Transactional
    public void updateData(UserData userData, Long id) {
        UserInfo userInfo = userInfoRepository.findById(id).orElseThrow(
                () -> new RuntimeException("User with id: " + id + " not found")
        );

        userInfo.setEmail(userData.getEmail());
        userInfo.setFirstName(userData.getFirstName());
        userInfo.setLastName(userData.getLastName());
        userInfo.setPhoneNumber(userData.getPhone());
        userInfo.setInstagramLink(userData.getInstagram());
        userInfo.setGithubLink(userData.getGithub());

        userInfoRepository.save(userInfo);
    }

    public UserAbout getUserAboutById(Long id) {
        UserInfo userInfo = userInfoRepository.findById(id).orElseThrow(
                () -> new RuntimeException("User with id: " + id + " not found")
        );
        return new UserAbout(userInfo.getAbout());
    }

    public UserData getUserDataById(Long id) {
        UserInfo userInfo = userInfoRepository.findById(id).orElseThrow(
                () -> new RuntimeException("User with id: " + id + " not found")
        );
        return new UserData(userInfo);
    }

    public UserDetailsDto getUserDetailsById(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new RuntimeException("User with id: " + id + " not found")
        );
        UserInfo userInfo = userInfoRepository.findById(id).orElseThrow(
                () -> new RuntimeException("User with id: " + id + " not found")
        );

        return new UserDetailsDto(userInfo, user.getRole(), googleDriveLinkBuilder);
    }
}
