package com.AveshDeveloper.FileDropBackend.fileService;

import com.AveshDeveloper.FileDropBackend.fileEntity.FileEntity;
import com.AveshDeveloper.FileDropBackend.fileModel.FileModel;
import com.AveshDeveloper.FileDropBackend.fileRepo.FileRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.swing.text.html.Option;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FileServiceImplements implements FileService{

    @Autowired
    private FileRepository fileRepository;

    private FileModel convertToModel(FileEntity entity){
        FileModel model = new FileModel();
        BeanUtils.copyProperties(entity, model);
        return model;
    }

    @Override
    public List<FileModel> getAllFile() {
       List<FileEntity> entities = fileRepository.findAll();
       return entities.stream().map(this::convertToModel).collect(Collectors.toList());
    }

//    @Override
//    public ResponseEntity<?> uploadFile(MultipartFile file, String uploadedBy) throws IOException {
//        if (file.isEmpty() || uploadedBy == null || uploadedBy.isEmpty()) {
//            return ResponseEntity.badRequest().body("File or uploadedBy cannot be empty");
//        }
//
//        FileEntity entity = new FileEntity();
//        entity.setFileName(file.getOriginalFilename());
//        entity.setUploadedBy(uploadedBy);
//        entity.setUploadTime(LocalDateTime.now());
//        entity.setExpireTime(LocalDateTime.now().plusDays(1));
//        entity.setFileData(file.getBytes());
//
//        fileRepository.save(entity);
//
//        return ResponseEntity.ok().body(convertToModel(entity));
//    }

    @Override
    public ResponseEntity<?> uploadFile(String fileName, String uploadedBy, MultipartFile file) throws IOException {
        if (file.isEmpty() || uploadedBy == null || uploadedBy.isEmpty() || fileName == null || fileName.isEmpty()) {
            return ResponseEntity.badRequest().body("File, uploadedBy, or fileName cannot be empty");
        }

        FileEntity entity = new FileEntity();
        entity.setFileName(fileName);
        entity.setUploadedBy(uploadedBy);
        entity.setUploadTime(LocalDateTime.now());
        entity.setExpireTime(LocalDateTime.now().plusDays(1));
        entity.setFileData(file.getBytes());

        fileRepository.save(entity);

        return ResponseEntity.ok().body(convertToModel(entity));
    }

    @Override
    public Map<String, Object> shareFile(int id) throws FileNotFoundException {
        Optional<FileEntity> fileOptional = fileRepository.findById(id);

        if(fileOptional.isPresent()){
            FileModel fileModel = convertToModel(fileOptional.get());


            String frontBaseUrl = "http://localhost:5173";
            String shareUrl = frontBaseUrl + "/share/" + id;

            Map<String, Object> response= new HashMap<>();
            response.put("file", fileModel);
            response.put("shareUrl", shareUrl);

            return response;
        }else{
            throw new FileNotFoundException("File Not Found!");
        }
    }

    @Override
    public ResponseEntity<?> deleteFile(int id) throws FileNotFoundException {
        Optional <FileEntity> fileEntity = fileRepository.findById(id);

        if(fileEntity.isPresent()){
            fileRepository.delete(fileEntity.get());
            return ResponseEntity.ok().body("File Deleted Successfully");
        }else{
            throw new FileNotFoundException("File Not Found");
        }
    }

    @Override
    public Optional<FileEntity> getFileById(int id) {
        return fileRepository.findById(id);
    }

    @Override
    public ResponseEntity<?> downloadFile(int id) throws FileNotFoundException{
        Optional<FileEntity> fileEntityOptional = fileRepository.findById(id);

        if(fileEntityOptional.isPresent()){
          FileEntity fileEntity = fileEntityOptional.get();

          return ResponseEntity.ok()
                  .header(HttpHeaders.CONTENT_DISPOSITION,
                          "attachment; fileName=\"" + fileEntity.getFileData() + "\"")
                  .body(fileEntity.getFileData());
        }else {
           throw new FileNotFoundException("file not found");
        }
    }

    @Override
    @Scheduled(cron = "0 0 * * * *")
    public void deleteExpireFiles() {
        List<FileEntity> entities = fileRepository.findByExpireTimeBefore(LocalDateTime.now());
        entities.forEach(fileRepository::delete);
        System.out.println("Files deleted successfully " + LocalDateTime.now());
    }
}
