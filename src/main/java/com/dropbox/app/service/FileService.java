package com.dropbox.app.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import com.dropbox.app.model.FileMetadata;
import com.dropbox.app.repository.FileMetadataRepository;

@Service
public class FileService {

	@Autowired
	private FileMetadataRepository fileMetadataRepository;

	@Value("${file.storage.path}")
	private String storagePath;

	public FileMetadata storeFile(MultipartFile file) throws IOException {
		Path path = Paths.get(storagePath + File.separator + file.getOriginalFilename());
		Files.write(path, file.getBytes());

		// Save metadata to DB
		FileMetadata metadata = new FileMetadata();
		metadata.setFileName(file.getOriginalFilename());
		metadata.setFileType(file.getContentType());
		metadata.setSize(file.getSize());
		metadata.setCreatedAt(LocalDateTime.now());
		metadata.setUpdatedAt(LocalDateTime.now());

		return fileMetadataRepository.save(metadata);
	}

	public FileMetadata getFileMetadata(Long fileId) throws FileNotFoundException {
		return fileMetadataRepository.findById(fileId)
			.orElseThrow(() -> new FileNotFoundException("File not found with id: " + fileId));
	}

	public Resource getFile(Long fileId) throws FileNotFoundException, MalformedURLException {
		FileMetadata metadata = fileMetadataRepository.findById(fileId)
			.orElseThrow(() -> new FileNotFoundException("File not found"));

		Path filePath = Paths.get(storagePath + File.separator + metadata.getFileName());
		return new UrlResource(filePath.toUri());
	}

	public Iterable<FileMetadata> listAllFiles() {
		return fileMetadataRepository.findAll();
	}

	public FileMetadata updateFile(Long fileId, MultipartFile newFile) throws IOException {
		FileMetadata existingMetadata = fileMetadataRepository.findById(fileId)
			.orElseThrow(() -> new FileNotFoundException("File not found with id: " + fileId));

		Path filePath = Paths.get(storagePath + File.separator + existingMetadata.getFileName());
		Files.deleteIfExists(filePath);

		Path newFilePath = Paths.get(storagePath + File.separator + newFile.getOriginalFilename());
		Files.write(newFilePath, newFile.getBytes());

		// Update file metadata in DB
		existingMetadata.setFileName(newFile.getOriginalFilename());
		existingMetadata.setFileType(newFile.getContentType());
		existingMetadata.setSize(newFile.getSize());
		existingMetadata.setUpdatedAt(LocalDateTime.now());

		return fileMetadataRepository.save(existingMetadata);
	}

	public void deleteFile(Long fileId) throws IOException {
		FileMetadata fileMetadata = fileMetadataRepository.findById(fileId)
			.orElseThrow(() -> new FileNotFoundException("File not found with id: " + fileId));

		Path filePath = Paths.get(storagePath + File.separator + fileMetadata.getFileName());
		Files.deleteIfExists(filePath);

		fileMetadataRepository.delete(fileMetadata);
	}

}
