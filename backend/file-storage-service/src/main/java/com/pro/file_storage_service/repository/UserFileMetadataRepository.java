package com.pro.file_storage_service.repository;

import com.pro.file_storage_service.model.entity.UserFileMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserFileMetadataRepository extends JpaRepository<UserFileMetadata, Long> {

    Optional<UserFileMetadata> findByUserId(Long userId);

}
