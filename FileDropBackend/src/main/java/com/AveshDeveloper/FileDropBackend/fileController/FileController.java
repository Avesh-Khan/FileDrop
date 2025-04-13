package com.AveshDeveloper.FileDropBackend.fileController;

import com.AveshDeveloper.FileDropBackend.fileEntity.FileEntity;
import com.AveshDeveloper.FileDropBackend.fileModel.FileModel;
import com.AveshDeveloper.FileDropBackend.fileService.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/file-drop")
public class FileController {

    @Autowired
    private FileService fileService;

    @GetMapping("/files")
    public List<FileModel> ListFiles(){
        return fileService.getAllFile();
    }

    /*@PostMapping("/files")
    public ResponseEntity<?> uploadFiles(@RequestParam("file") MultipartFile file,
                                         @RequestParam("uploadedBy") String uploadedBy) throws IOException {
        return fileService.uploadFile(file, uploadedBy);
    }*/

    @PostMapping("/files")
    public ResponseEntity<?> uploadFiles(
            @RequestParam("fileName") String fileName,
            @RequestParam("uploadedBy") String uploadedBy,
            @RequestPart("fileData") MultipartFile fileData) throws IOException {

        // Call the service to handle the file upload
        return fileService.uploadFile(fileName, uploadedBy, fileData);
    }


    @GetMapping("/share/{id}")
    public ResponseEntity<?> shareFile(@PathVariable int id) throws FileNotFoundException {
        Map<String, Object> response = fileService.shareFile(id);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<?> downloadFile(@PathVariable("id") int id) throws FileNotFoundException{
        return fileService.downloadFile(id);
    }



    @DeleteMapping("/files/{id}")
    public ResponseEntity<?> deleteFile(@PathVariable int id) throws FileNotFoundException {
        return fileService.deleteFile(id);
    }


}
