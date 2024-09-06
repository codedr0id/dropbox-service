package com.dropbox.app.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.dropbox.app.model.FileMetadata;
import com.dropbox.app.repository.FileMetadataRepository;

@Service
public class FileService {

	@Autowired
	private FileMetadataRepository fileMetadataRepository;

	@Value("${file.storage.path}")
	private String storagePath;

	public FileMetadata storeFile(MultipartFile file) throws IOException {
		// Save the file locally
		Path path = Paths.get(storagePath + File.separator + file.getOriginalFilename());
		Files.write(path, file.getBytes());

		// Save metadata to DB
		FileMetadata metadata = new FileMetadata();
		metadata.setFileName(file.getOriginalFilename());
		metadata.setFileType(file.getContentType());
		metadata.setSize(file.getSize());
		metadata.setCreatedAt(LocalDateTime.now());

		return fileMetadataRepository.save(metadata);
	}

}
