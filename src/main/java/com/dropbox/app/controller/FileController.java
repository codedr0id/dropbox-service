package com.dropbox.app.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import org.springframework.http.HttpHeaders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;

import com.dropbox.app.model.FileMetadata;
import com.dropbox.app.service.FileService;

@RestController
@RequestMapping("/files")
public class FileController {

    @Autowired
    private FileService fileService;

    // Upload File API
    @PostMapping("/upload")
    public ResponseEntity<FileMetadata> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        FileMetadata metadata = fileService.storeFile(file);
        return new ResponseEntity<>(metadata, HttpStatus.OK);
    }

    // GET File API
    @GetMapping("/{fileId}")
    public ResponseEntity<Resource> readFile(@PathVariable Long fileId)
            throws FileNotFoundException, MalformedURLException {
        Resource file = fileService.getFile(fileId);

        FileMetadata metadata = fileService.getFileMetadata(fileId);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(metadata.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }

    // LIST Files API
    @GetMapping
    public ResponseEntity<Iterable<FileMetadata>> listAllFiles() {
        Iterable<FileMetadata> files = fileService.listAllFiles();
        return new ResponseEntity<>(files, HttpStatus.OK);
    }

}
