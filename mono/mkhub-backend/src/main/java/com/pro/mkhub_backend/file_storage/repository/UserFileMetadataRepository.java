package com.pro.mkhub_backend.file_storage.repository;

import com.pro.mkhub_backend.file_storage.model.entity.UserFileMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserFileMetadataRepository extends JpaRepository<UserFileMetadata, Long> {

    Optional<UserFileMetadata> findByUserInfoId(Long userId);

}
