package com.pro.file_storage_service.client;

import com.pro.file_storage_service.config.FeignClientConfig;
import com.pro.file_storage_service.dto.UserInfoDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "auth-service", configuration = FeignClientConfig.class)
public interface AuthClient {

    @GetMapping("/auth/validate")
    ResponseEntity<UserInfoDto> validateToken(@RequestHeader("Authorization") String accessToken);

}
