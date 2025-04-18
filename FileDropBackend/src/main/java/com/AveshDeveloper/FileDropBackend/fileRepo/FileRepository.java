package com.AveshDeveloper.FileDropBackend.fileRepo;

import com.AveshDeveloper.FileDropBackend.fileEntity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<FileEntity, Integer> {
    List<FileEntity> findByExpireTimeBefore(LocalDateTime now);


}
