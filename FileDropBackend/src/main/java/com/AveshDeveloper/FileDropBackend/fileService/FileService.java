package com.AveshDeveloper.FileDropBackend.fileService;

import com.AveshDeveloper.FileDropBackend.fileEntity.FileEntity;
import com.AveshDeveloper.FileDropBackend.fileModel.FileModel;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
public interface FileService {
    public List<FileModel> getAllFile();
    public ResponseEntity<?> uploadFile(String fileName, String uploadedBy, MultipartFile fileData) throws IOException;
    public Map<String, Object> shareFile(int id) throws FileNotFoundException;
    public ResponseEntity<?> deleteFile(int id) throws FileNotFoundException;
    public Optional<FileEntity> getFileById(int id);
    public ResponseEntity<?> downloadFile(int id) throws FileNotFoundException;
    public void deleteExpireFiles();
}
