package com.pro.mkhub_backend.file_storage.model.entity;

import com.pro.mkhub_backend.file_storage.listener.UserFileMetadataListener;
import com.pro.mkhub_backend.user.model.entity.UserInfo;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_files")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(UserFileMetadataListener.class)
public class UserFileMetadata extends FileMetadata {

    @OneToOne(mappedBy = "userFileMetadata")
    private UserInfo userInfo;

    private String userFolderPath;

}
